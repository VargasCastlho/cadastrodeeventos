package org.vargascastlho.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotNull
    private String nome;

    @NotNull
    @NotEmpty
    @Min(value = 6)
    private String senha;

    @Override
    public boolean equals(Object obj) {
        Usuario usuario = (Usuario) obj;
        return Objects.equals(usuario.getIdUsuario(), this.idUsuario);
    }

    public void alterarNome(String nome) {
        this.nome = nome;
    }

    public void alterarSenha(String senha) {
        this.senha = senha;
    }
}
