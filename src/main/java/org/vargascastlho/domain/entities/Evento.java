package org.vargascastlho.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Integer idEvento;

    @NotNull
    @NotEmpty
    @Min(value = 50)
    private String titulo;

    @Max(value = 1000)
    @Column(length = 1000)
    private String descricao;

    @NotNull
    private LocalDate data;

    @NotNull
    @ManyToOne
    private Usuario responsavel;

    @NotNull
    @NotEmpty
    @ManyToMany
    private List<Usuario> participantes;

    public void alterar(String titulo, String descricao, LocalDate data, Usuario responsavel,
                        List<Usuario> participantes) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.responsavel = responsavel;
        this.participantes = participantes;
    }

    public void adicionarParticipante(Usuario participante) {
        participantes.add(participante);
    }

    public void excluirParticipante(Usuario participante) {
        participantes.remove(participante);
    }
}
