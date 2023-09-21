package org.vargascastlho.application.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.vargascastlho.application.dtos.PagedResult;
import org.vargascastlho.application.dtos.usuario.AdicionarUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.AlterarNomeUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.AlterarSenhaUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.UsuarioDTO;
import org.vargascastlho.application.services.interfaces.UsuarioService;
import org.vargascastlho.domain.exceptions.ValidacaoException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/usuario")
@Tag(name = "Endpoints do Usuário")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService service;

    @Operation(summary = "Busca usuário pelo seu id", description = "Deve ser informado o ID do usuário no campo <i>id_usuario</i>. <br>")
    @APIResponse(responseCode = "200", description = "Busca realizada com sucesso!")
    @APIResponse(responseCode = "404", description = "O usuário não foi encontrado")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @GET
    @Path("/{id_usuario}")
    public Response buscarUsuarioPorId(@PathParam("id_usuario") Integer idUsuario) {
        try {
            UsuarioDTO usuarioDTO = service.buscarPorId(idUsuario);
            return Response.status(200).entity(usuarioDTO).build();
        } catch (ValidacaoException ex) {
            return Response.status(404).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Lista usuário paginado", description = "Deve ser informado o número da página inicial e quantidade de registros por página. <br>" +
            "<br>Deve ser informado a página inicial no campo <i>page</i>. Por padrão a página inicia em 0." +
            "<br>Deve ser informado a quantidade de registros no campo <i>size</i>. " +
            "Representa a quantidade de registros que devem retornar na pesquisa. " +
            "Deve ser informado no mínimo 1 registro")
    @APIResponse(responseCode = "200", description = "Busca realizada com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "404", description = "O evento não foi encontrado")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @GET
    @Path("/buscar")
    public Response listarEventos(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
        try {
            PagedResult<UsuarioDTO> usuarioDTOPagedResult = service.listarPaginado(page, size);
            return Response.status(200).entity(usuarioDTOPagedResult).build();
        } catch (ValidacaoException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Adiciona usuário")
    @APIResponse(responseCode = "201", description = "Usuário adicionado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @POST
    @Path("/adicionar")
    public Response adicionarUsuario(@RequestBody AdicionarUsuarioDTO adicionarUsuarioDTO) {
        try {
            UsuarioDTO usuarioDTO = service.adicionar(adicionarUsuarioDTO);
            return Response.status(201).entity(usuarioDTO).build();
        } catch (ValidacaoException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Altera nome do usuário")
    @APIResponse(responseCode = "200", description = "Nome do usuário alterado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @Path("/alterar/nome/{id_usuario}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public Response alterarNomeUsuario(@PathParam("id_usuario") Integer idUsuario, @RequestBody AlterarNomeUsuarioDTO alterarNomeUsuarioDTO) {
        try {
            service.alterarNome(idUsuario, alterarNomeUsuarioDTO);
            return Response.status(200).entity("Nome do usuário " + idUsuario + " alterado com sucesso").build();
        } catch (ValidacaoException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Altera senha do usuário")
    @APIResponse(responseCode = "200", description = "Nome do usuário alterado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @Path("/alterar/senha/{id_usuario}")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public Response alterarSenhaUsuario(@PathParam("id_usuario") Integer idUsuario, @RequestBody AlterarSenhaUsuarioDTO alterarSenhaUsuarioDTO) {
        try {
            service.alterarSenha(idUsuario, alterarSenhaUsuarioDTO);
            return Response.status(200).entity("Senha do usuário " + idUsuario + " alterado com sucesso").build();
        } catch (ValidacaoException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Exclui usuário")
    @APIResponse(responseCode = "200", description = "Evento alterado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @Path("/excluir/{id_usuario}")
    @DELETE
    public Response deletarUsuario(@PathParam("id_usuario") Integer idUsuario) {
        try {
            service.excluirPorId(idUsuario);
            return Response.status(200).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
