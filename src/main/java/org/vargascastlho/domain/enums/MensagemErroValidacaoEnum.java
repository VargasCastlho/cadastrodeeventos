package org.vargascastlho.domain.enums;

import lombok.Getter;

public enum MensagemErroValidacaoEnum {

    //Usuario
    USUARIO_NAO_ENCONTRADO("O Usuario não foi encontrado."),
    PARTICIPANTE_NAO_ENCONTRADO("O participante com o seguinte id não foi encontrado: "),
    RESPONSAVEL_NAO_ENCONTRADO("O usuário responsável não foi encontrado"),
    RESPONSAVEL_OBRIGATORIO("O campo idResponsável é obrigatório"),
    IDS_PARTICIPANTES_OBRIGATORIO("O campo idsParticipantes é obrigatório"),
    NOME_OBRIGATORIO("O campo nome é obrigatório"),
    SENHA_OBRIGATORIA("O campo senha é obrigatório"),
    SENHA_MINIMO_CARACTERES("O campo senha deve conter ao menos 6 caracteres"),
    SENHA_MINIMO_NUMERO("O campo senha deve conter ao menos 1 número"),

    //evento
    EVENTO_NAO_ENCONTRADO("O Evento não foi encontrado."),
    TITULO_MININO_CARACTERES("O campo título deve conter ao menos 50 caracteres."),
    DESCRICAO_MAXIMO_CARACTERES("O campo descrição deve conter no máximo 1000 caracteres"),

    //data
    DATA_INVALIDA("Data em formato inválido"),

    //page size
    SIZE_OBRIGATORIO("O campo size é obrigatório");

    private @Getter String mensagemErro;

    private MensagemErroValidacaoEnum(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }
}
