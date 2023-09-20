package org.vargascastlho.domain.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.vargascastlho.domain.entities.Evento;

public interface EventoRepository extends PanacheRepositoryBase<Evento, Integer> {
}
