package org.vargascastlho.application.services.interfaces;

import org.vargascastlho.application.dtos.evento.AdicionarEventoDTO;
import org.vargascastlho.application.dtos.evento.AlterarEventoDTO;
import org.vargascastlho.application.dtos.evento.EventoDTO;
import org.vargascastlho.domain.exceptions.DataException;
import org.vargascastlho.domain.exceptions.ValidacaoException;

public interface EventoService {

    public void excluirPorId(Integer idEvento);

    public EventoDTO adicionar(AdicionarEventoDTO adicionarEventoDTO) throws ValidacaoException, DataException;

    public EventoDTO buscarPorId(Integer idEvento) throws ValidacaoException;

    public EventoDTO alterar(Integer idEvento, AlterarEventoDTO eventoDTO) throws ValidacaoException, DataException;

}
