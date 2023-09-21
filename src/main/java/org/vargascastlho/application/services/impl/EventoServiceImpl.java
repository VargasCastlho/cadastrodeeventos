package org.vargascastlho.application.services.impl;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.vargascastlho.application.dtos.PagedResult;
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
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        try {
            validarDTO(adicionarEventoDTO.getTitulo(), adicionarEventoDTO.getDescricao(),
                    adicionarEventoDTO.getData(), adicionarEventoDTO.getIdResponsavel(),
                    adicionarEventoDTO.getIdsParticipantes());

            Usuario responsavel = buscarResponsavel(adicionarEventoDTO.getIdResponsavel());
            List<Usuario> participantes = buscarParticipantes(adicionarEventoDTO.getIdsParticipantes());
            Evento evento = Evento.builder().titulo(adicionarEventoDTO.getTitulo())
                    .descricao(adicionarEventoDTO.getDescricao()).data(dataParaLocalDate(adicionarEventoDTO.getData()))
                    .responsavel(responsavel).participantes(participantes).build();
            eventoRepository.persistAndFlush(evento);
            return entitytoDTO(evento);
        } catch (DateTimeException e) {
            if (e.getMessage().contains("Invalid date"))
                throw new DataException(DATA_INVALIDA.getMensagemErro());
        }

        return null;
    }

    @Override
    @Transactional
    public EventoDTO alterar(Integer idEvento, AlterarEventoDTO alterarEventoDTO) throws ValidacaoException, DataException {
        try {
            String titulo = alterarEventoDTO.getTitulo();
            String descricao = alterarEventoDTO.getDescricao();
            String data = alterarEventoDTO.getData();
            Integer idResponsavel = alterarEventoDTO.getIdResponsavel();
            List<Integer> idsParticipantes = alterarEventoDTO.getIdsParticipantes();

            validarDTO(titulo, descricao, data, idResponsavel, idsParticipantes);
            Usuario responsavel = buscarResponsavel(idResponsavel);
            List<Usuario> participantes = buscarParticipantes(idsParticipantes);
            Evento evento = buscarEvento(idEvento);

            evento.setTitulo(titulo);
            evento.setDescricao(descricao);
            evento.setData(dataParaLocalDate(data));
            evento.setResponsavel(responsavel);
            evento.setParticipantes(participantes);

            eventoRepository.persistAndFlush(evento);
            return entitytoDTO(evento);
        } catch (DateTimeException e) {
            if (e.getMessage().contains("Invalid date"))
                throw new DataException(DATA_INVALIDA.getMensagemErro());
        }

        return null;
    }

    @Override
    @Transactional
    public PagedResult<EventoDTO> listarPaginado(Integer page, Integer size) throws ValidacaoException {
        if (page == null)
            page = 0;
        if (size == null || size == 0)
            throw new ValidacaoException(SIZE_OBRIGATORIO.getMensagemErro());

        PagedResult<EventoDTO> pagedResult = new PagedResult<>();

        PanacheQuery<Evento> eventos = eventoRepository.findAll();
        Stream<Evento> pagina = eventos.page(Page.of(page, size)).stream();

        pagedResult.results = pagina.map(e -> entitytoDTO(e)).collect(Collectors.toList());
        pagedResult.total = eventos.count();

        return pagedResult;
    }

    @Override
    @Transactional
    public EventoDTO buscarPorId(Integer idEvento) throws ValidacaoException {
        Evento evento = buscarEvento(idEvento);
        return entitytoDTO(evento);
    }

    private EventoDTO entitytoDTO(Evento evento) {
        EventoDTO eventoDTO = mapper.toDTO(evento);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        eventoDTO.setData(evento.getData().format(formatter));
        return eventoDTO;
    }

    private void validarDTO(String titulo, String descricao, String data, Integer idResponsavel, List<Integer> idsParticipantes) throws ValidacaoException, DataException {

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
