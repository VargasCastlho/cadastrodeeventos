package org.vargascastlho.application.services.interfaces;

import org.vargascastlho.application.dtos.usuario.AdicionarUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.AlterarNomeUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.AlterarSenhaUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.UsuarioDTO;
import org.vargascastlho.domain.exceptions.ValidacaoException;


public interface UsuarioService {

    public void excluirPorId(Integer idUsuario) throws ValidacaoException;

    public UsuarioDTO adicionar(AdicionarUsuarioDTO adicionarUsuarioDTO) throws ValidacaoException;

    public UsuarioDTO buscarPorId(Integer idUsuario) throws ValidacaoException;

    public UsuarioDTO alterarNome(Integer idUsuario, AlterarNomeUsuarioDTO usuarioDTO) throws ValidacaoException;

    public UsuarioDTO alterarSenha(Integer idUsuario, AlterarSenhaUsuarioDTO usuarioDTO) throws ValidacaoException;
}
