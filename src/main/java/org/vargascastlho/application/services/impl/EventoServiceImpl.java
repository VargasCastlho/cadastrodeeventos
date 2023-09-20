package org.vargascastlho.application.services.impl;

import org.vargascastlho.application.dtos.evento.AdicionarEventoDTO;
import org.vargascastlho.application.dtos.evento.AlterarEventoDTO;
import org.vargascastlho.application.dtos.evento.EventoDTO;
import org.vargascastlho.application.mappers.EventoMapper;
import org.vargascastlho.application.services.interfaces.EventoService;
import org.vargascastlho.domain.entities.Evento;
import org.vargascastlho.domain.entities.Usuario;
import org.vargascastlho.domain.exceptions.DataException;
import org.vargascastlho.domain.exceptions.ValidacaoException;
import org.vargascastlho.domain.repositories.EventoRepository;
import org.vargascastlho.domain.repositories.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.vargascastlho.domain.enums.MensagemErroValidacaoEnum.*;

@ApplicationScoped
public class EventoServiceImpl implements EventoService {

    @Inject
    EventoRepository eventoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    EventoMapper mapper;


    @Override
    @Transactional
    public void excluirPorId(Integer idEvento) {
        eventoRepository.deleteById(idEvento);
    }

    @Override
    @Transactional
    public EventoDTO adicionar(AdicionarEventoDTO adicionarEventoDTO) throws ValidacaoException, DataException {
        Evento evento = validarAdicionaDTO(adicionarEventoDTO);
        eventoRepository.persistAndFlush(evento);
        return entitytoDTO(evento);
    }

    @Override
    @Transactional
    public EventoDTO buscarPorId(Integer idEvento) throws ValidacaoException {
        Evento evento = buscarEvento(idEvento);
        return mapper.toDTO(evento);
    }

    @Override
    @Transactional
    public EventoDTO alterar(Integer idEvento, AlterarEventoDTO eventoDTO) throws ValidacaoException, DataException {
        Evento evento = buscarEvento(idEvento);
        Usuario responsavel = buscarResponsavel(eventoDTO.getIdRresponsavel());
        List<Usuario> participantes = usuarioRepository.buscarUsuariosPorId(eventoDTO.getParticipantes());

//        evento.alterar(eventoDTO.getTitulo(), eventoDTO.getDescricao(), validarData(eventoDTO.getData()),
//                responsavel, participantes);
        eventoRepository.persistAndFlush(evento);
        return mapper.toDTO(evento);
    }

    private EventoDTO entitytoDTO(Evento evento) {
        EventoDTO eventoDTO = mapper.toDTO(evento);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        eventoDTO.setData(evento.getData().format(formatter));
        return eventoDTO;
    }

    private Evento validarAdicionaDTO(AdicionarEventoDTO eventoDTO) throws ValidacaoException, DataException {
        String titulo = eventoDTO.getTitulo();
        String descricao = eventoDTO.getDescricao();
        String data = eventoDTO.getData();
        Integer idResponsavel = eventoDTO.getIdResponsavel();
        List<Integer> idsParticipantes = eventoDTO.getIdsParticipantes();
        Usuario responsavel;
        List<Usuario> participantes;

        if (titulo == null || titulo.length() < 50)
            throw new ValidacaoException(TITULO_MININO_CARACTERES.getMensagemErro());
        if (descricao != null && descricao.length() > 1000)
            throw new ValidacaoException(DESCRICAO_MAXIMO_CARACTERES.getMensagemErro());
        if (!isDataValida(data))
            throw new DataException(DATA_INVALIDA.getMensagemErro());
        if (idResponsavel == null)
            throw new ValidacaoException(RESPONSAVEL_OBRIGATORIO.getMensagemErro());
        if (idsParticipantes == null || idsParticipantes.isEmpty())
            throw new ValidacaoException(IDS_PARTICIPANTES_OBRIGATORIO.getMensagemErro());
        responsavel = buscarResponsavel(idResponsavel);
        participantes = buscarParticipantes(idsParticipantes);

        return Evento.builder().titulo(titulo).descricao(descricao).data(dataParaLocalDate(data))
                .responsavel(responsavel).participantes(participantes).build();
    }

    private Evento buscarEvento(Integer idEvento) throws ValidacaoException {
        Optional<Evento> eventoOptional = eventoRepository.findByIdOptional(idEvento);
        if (eventoOptional.isPresent())
            return eventoOptional.get();
        else
            throw new ValidacaoException(EVENTO_NAO_ENCONTRADO.getMensagemErro());
    }

    private Usuario buscarResponsavel(Integer idUsuario) throws ValidacaoException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByIdOptional(idUsuario);
        if (usuarioOptional.isPresent())
            return usuarioOptional.get();
        else
            throw new ValidacaoException(RESPONSAVEL_NAO_ENCONTRADO.getMensagemErro());
    }

    private List<Usuario> buscarParticipantes(List<Integer> participantes) throws ValidacaoException {
        List<Usuario> usuariosParticipantes = new ArrayList<>();
        for (Integer p : participantes) {
            usuariosParticipantes.add(usuarioRepository.findByIdOptional(p).orElseThrow(
                    () -> new ValidacaoException(PARTICIPANTE_NAO_ENCONTRADO.getMensagemErro() + p)));
        }
        return usuariosParticipantes;
    }

    private boolean isDataValida(String data) {
        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    private LocalDate dataParaLocalDate(String data) {
        int dia = Integer.parseInt(data.substring(0, 2));
        int mes = Integer.parseInt(data.substring(3, 5));
        int ano = Integer.parseInt(data.substring(6));

        return LocalDate.of(ano, mes, dia);
    }
}
