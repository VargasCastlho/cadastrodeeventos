package org.vargascastlho.infra.repositories;

import org.vargascastlho.domain.entities.Usuario;
import org.vargascastlho.domain.repositories.UsuarioRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UsuarioRepositoryImpl implements UsuarioRepository {
    @Override
    public List<Usuario> buscarUsuariosPorId(List<Integer> idsUsuarios) {
        return list("id in ?1", idsUsuarios);
    }

}
