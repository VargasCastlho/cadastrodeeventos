package org.vargascastlho.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vargascastlho.application.dtos.usuario.AdicionarUsuarioDTO;
import org.vargascastlho.application.dtos.usuario.UsuarioDTO;
import org.vargascastlho.domain.entities.Usuario;

@Mapper(componentModel = "cdi")
public interface UsuarioMapper {

    UsuarioDTO toDTO(Usuario Usuario);

    Usuario toEntity(UsuarioDTO UsuarioDTO);

    @Mapping(target = "idUsuario", ignore = true)
    Usuario adicionarDtoToEntity(AdicionarUsuarioDTO adicionarUsuarioDTO);

}
