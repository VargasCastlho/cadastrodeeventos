package org.vargascastlho.application.dtos.evento;

import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.vargascastlho.application.dtos.usuario.UsuarioNomeDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class EventoDTO {
    @NotNull
    @Schema(required = true, example = "1")
    private Integer idEvento;

    @Min(50)
    @NotNull
    @Schema(required = true, example = "Café da manhã na IndustriALL")
    private String titulo;

    @Max(1000)
    @Schema(example = "Venha tomar um delicioso café da manhã! É neste sábado!")
    private String descricao;

    @Schema(example = "26/07/2021")
    private String data;

    @Schema(required = true, example = "3")
    private UsuarioNomeDTO responsavel;

    @Schema(required = true, example = "{1, 5, 3}")
    @NotEmpty
    @NotNull
    private List<UsuarioNomeDTO> participantes;
}
