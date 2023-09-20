package org.vargascastlho.application.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.vargascastlho.application.dtos.evento.AdicionarEventoDTO;
import org.vargascastlho.application.dtos.evento.EventoDTO;
import org.vargascastlho.application.services.interfaces.EventoService;
import org.vargascastlho.domain.exceptions.DataException;
import org.vargascastlho.domain.exceptions.ValidacaoException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/evento")
@Tag(name = "Endpoints do Evento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventoResource {

    @Inject
    EventoService service;

    @Operation(summary = "Busca evento pelo seu id")
    @APIResponse(responseCode = "200", description = "Busca realizada com sucesso!")
    @APIResponse(responseCode = "401", description = "Usuário não esta autorizado/autenticado")
    @APIResponse(responseCode = "404", description = "O evento não foi encontrado")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @GET
    @Path("/{id_evento}")
    public Response buscarEventoPorId(@PathParam("id_evento") Integer idEvento) {
        try {
            EventoDTO eventoDTO = service.buscarPorId(idEvento);
            return Response.status(200).entity(eventoDTO).build();
        } catch (ValidacaoException ex) {
            return Response.status(404).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity("Ocorreu um erro na requisição.").build();
        }
    }

    @Operation(summary = "Adiciona Evento")
    @APIResponse(responseCode = "201", description = "Evento adicionado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "401", description = "Usuário não esta autorizado/autenticado")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @POST
    public Response adicionarUsuario(@RequestBody AdicionarEventoDTO adicionarEventoDTO) {
        try {
            EventoDTO eventoDTO = service.adicionar(adicionarEventoDTO);
            return Response.status(201).entity(eventoDTO).build();
        } catch (ValidacaoException | DataException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity("Ocorreu um erro na requisição." + ex.getMessage()).build();
        }
    }
}
