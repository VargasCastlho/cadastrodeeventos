package org.vargascastlho.application.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.vargascastlho.application.dtos.PagedResult;
import org.vargascastlho.application.dtos.evento.AdicionarEventoDTO;
import org.vargascastlho.application.dtos.evento.AlterarEventoDTO;
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

    @Operation(summary = "Busca evento pelo seu id", description = "Deve ser informado o ID do evento no campo <i>id_evento</i>. <br>")
    @APIResponse(responseCode = "200", description = "Busca realizada com sucesso!")
    @APIResponse(responseCode = "404", description = "O evento não foi encontrado")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @GET
    @Path("/buscar/{id_evento}")
    public Response buscarEventoPorId(@PathParam("id_evento") Integer idEvento) {
        try {
            EventoDTO eventoDTO = service.buscarPorId(idEvento);
            return Response.status(200).entity(eventoDTO).build();
        } catch (ValidacaoException ex) {
            return Response.status(404).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Lista evento paginado", description = "Deve ser informado o número da página inicial e quantidade de registros por página. <br>" +
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
            PagedResult<EventoDTO> eventoDTOPagedResult = service.listarPaginado(page, size);
            return Response.status(200).entity(eventoDTOPagedResult).build();
        } catch (ValidacaoException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Adiciona evento")
    @APIResponse(responseCode = "201", description = "Evento adicionado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @POST
    @Path("/adicionar")
    public Response adicionarEvento(@RequestBody AdicionarEventoDTO adicionarEventoDTO) {
        try {
            EventoDTO eventoDTO = service.adicionar(adicionarEventoDTO);
            return Response.status(201).entity(eventoDTO).build();
        } catch (ValidacaoException | DataException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Altera evento")
    @APIResponse(responseCode = "200", description = "Evento alterado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @Path("/alterar/{id_evento}")
    @PUT
    public Response alterarEvento(@PathParam("id_evento") Integer idEvento, @RequestBody AlterarEventoDTO alterarEventoDTO) {
        try {
            EventoDTO eventoDTO = service.alterar(idEvento, alterarEventoDTO);
            return Response.status(200).entity(eventoDTO).build();
        } catch (ValidacaoException | DataException ex) {
            return Response.status(400).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }

    @Operation(summary = "Exclui evento")
    @APIResponse(responseCode = "200", description = "Evento alterado com sucesso!")
    @APIResponse(responseCode = "400", description = "Erro de validação")
    @APIResponse(responseCode = "500", description = "Erro no servidor")
    @Path("/excluir/{id_evento}")
    @DELETE
    public Response deletarEvento(@PathParam("id_evento") Integer idEvento) {
        try {
            service.excluirPorId(idEvento);
            return Response.status(200).build();
        } catch (Exception ex) {
            return Response.status(500).entity(ex.getMessage()).build();
        }
    }
}
