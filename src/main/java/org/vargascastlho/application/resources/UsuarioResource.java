package org.vargascastlho.application.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.vargascastlho.application.dtos.usuario.AdicionarUsuarioDTO;
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

    @Operation(summary = "Busca usuário pelo seu id do keycloak")
    @APIResponse(responseCode = "200", description = "Busca realizada com sucesso!")
    @APIResponse(responseCode = "401", description = "Usuário não esta autorizado/autenticado")
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
            return Response.status(500).entity("Ocorreu um erro na requisição.").build();
        }
    }

    @Operation(summary = "Adiciona usuário")
    @APIResponse(responseCode = "201", description = "Usuário adicionado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "401", description = "Usuário não esta autorizado/autenticado")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @POST
    public Response adicionarUsuario(@RequestBody AdicionarUsuarioDTO adicionarUsuarioDTO) {
        try {
            UsuarioDTO usuarioDTO = service.adicionar(adicionarUsuarioDTO);
            return Response.status(201).entity(usuarioDTO).build();
        } catch (ValidacaoException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity("Ocorreu um erro na requisição.").build();
        }
    }
}
