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
public class AdicionarUsuarioDTO {
    @NotNull
    @Schema(required = true, example = "Fernando Vargas")
    private String nome;

    @NotNull
    @Schema(required = true, example = "batatinhaFrita123@")
    private String senha;
}
