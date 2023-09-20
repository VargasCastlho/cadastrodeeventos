package org.vargascastlho.application.dtos.usuario;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class UsuarioNomeDTO {

    @NotNull
    @Schema(required = true, example = "1")
    private Integer idUsuario;

    @NotNull
    @Schema(required = true, example = "Fernando Vargas")
    private String nome;
}
