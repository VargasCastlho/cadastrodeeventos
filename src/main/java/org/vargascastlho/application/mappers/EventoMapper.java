package org.vargascastlho.application.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vargascastlho.application.dtos.evento.AdicionarEventoDTO;
import org.vargascastlho.application.dtos.evento.EventoDTO;
import org.vargascastlho.domain.entities.Evento;

@Mapper(componentModel = "cdi")
public interface EventoMapper {

    @Mapping(target = "data", ignore = true)
    EventoDTO toDTO(Evento evento);


    @Mapping(target = "responsavel", ignore = true)
    @Mapping(target = "participantes", ignore = true)
    @Mapping(target = "idEvento", ignore = true)
    Evento adicionarDtoToEntity(AdicionarEventoDTO adicionarEventoDTO);
}
