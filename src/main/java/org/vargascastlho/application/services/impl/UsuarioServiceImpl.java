package org.vargascastlho.application.services.impl;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import org.vargascastlho.application.dtos.PagedResult;
import org.vargascastlho.application.dtos.usuario.AdicionarUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.AlterarNomeUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.AlterarSenhaUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.UsuarioDTO;
import org.vargascastlho.application.mappers.UsuarioMapper;
import org.vargascastlho.application.services.interfaces.UsuarioService;
import org.vargascastlho.domain.entities.Usuario;
import org.vargascastlho.domain.exceptions.ValidacaoException;
import org.vargascastlho.domain.repositories.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.vargascastlho.domain.enums.MensagemErroValidacaoEnum.*;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    UsuarioRepository repository;

    @Inject
    UsuarioMapper mapper;


    @Override
    @Transactional
    public void excluirPorId(Integer idUsuario) throws ValidacaoException {
        repository.deleteById(idUsuario);
    }

    @Override
    @Transactional
    public UsuarioDTO adicionar(AdicionarUsuarioDTO adicionarUsuarioDTO) throws ValidacaoException {
        adicionarUsuarioDTO.setNome(adicionarUsuarioDTO.getNome().trim());
        validarDTO(adicionarUsuarioDTO);
        Usuario usuario = mapper.adicionarDtoToEntity(adicionarUsuarioDTO);
        repository.persistAndFlush(usuario);
        return mapper.toDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO buscarPorId(Integer idUsuario) throws ValidacaoException {
        Usuario usuario = buscarUsuario(idUsuario);
        return mapper.toDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO alterarNome(Integer idUsuario, AlterarNomeUsuarioDTO usuarioDTO) throws ValidacaoException {
        Usuario usuario = buscarUsuario(idUsuario);
        String nome = usuarioDTO.getNome().trim();
        validarNome(nome);
        usuario.alterarNome(nome);
        repository.persistAndFlush(usuario);
        return mapper.toDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO alterarSenha(Integer idUsuario, AlterarSenhaUsuarioDTO usuarioDTO) throws ValidacaoException {
        Usuario usuario = buscarUsuario(idUsuario);
        validarSenha(usuarioDTO.getSenha());
        usuario.alterarSenha(usuarioDTO.getSenha());
        repository.persistAndFlush(usuario);
        return mapper.toDTO(usuario);
    }

    @Override
    @Transactional
    public PagedResult<UsuarioDTO> listarPaginado(Integer page, Integer size) throws ValidacaoException {
        if (page == null)
            page = 0;
        if (size == null || size == 0)
            throw new ValidacaoException(SIZE_OBRIGATORIO.getMensagemErro());

        PagedResult<UsuarioDTO> pagedResult = new PagedResult<>();

        PanacheQuery<Usuario> usuarios = repository.findAll();
        Stream<Usuario> pagina = usuarios.page(Page.of(page, size)).stream();

        pagedResult.results = pagina.map(e -> mapper.toDTO(e)).collect(Collectors.toList());
        pagedResult.total = usuarios.count();

        return pagedResult;
    }

    private Usuario buscarUsuario(Integer idUsuario) throws ValidacaoException {
        Optional<Usuario> usuarioOptional = repository.findByIdOptional(idUsuario);
        if (usuarioOptional.isPresent())
            return usuarioOptional.get();
        else
            throw new ValidacaoException(USUARIO_NAO_ENCONTRADO.getMensagemErro());

    }

    private void validarDTO(AdicionarUsuarioDTO usuarioDTO) throws ValidacaoException {
        String nome = usuarioDTO.getNome();
        String senha = usuarioDTO.getSenha();

        validarNome(nome);
        validarSenha(senha);
    }

    private void validarSenha(String senha) throws ValidacaoException {
        if (senha == null || senha.isEmpty())
            throw new ValidacaoException(SENHA_OBRIGATORIA.getMensagemErro());
        if (senha.length() < 6)
            throw new ValidacaoException(SENHA_MINIMO_CARACTERES.getMensagemErro());
        if (!contemNumero(senha))
            throw new ValidacaoException(SENHA_MINIMO_NUMERO.getMensagemErro());
    }

    private void validarNome(String nome) throws ValidacaoException {
        if (nome == null || nome.isEmpty())
            throw new ValidacaoException(NOME_OBRIGATORIO.getMensagemErro());
    }

    private static boolean contemNumero(String str) {
        for (int i = 0; i < 10; i++) {
            if (str.contains(String.valueOf(i)))
                return true;
        }
        return false;
    }


}
