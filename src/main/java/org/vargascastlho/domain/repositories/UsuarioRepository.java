package org.vargascastlho.domain.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.vargascastlho.domain.entities.Usuario;

import java.util.List;

public interface UsuarioRepository extends PanacheRepositoryBase<Usuario, Integer> {

    public List<Usuario> buscarUsuariosPorId(List<Integer> idsUsuarios);
}
