package com.asuprojects.tarefafeita.domain.enums;

public enum Status {

    ADICIONADO("Adicionado"),
    EXECUTANDO("Executando"),
    CANCELADO("Cancelado"),
    CONCLUIDO("Concluido");

    private String descricao;

    Status(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Status toEnum(String value){
        for(Status s : Status.values()){
            if(s.getDescricao().equals(value)){
                return s;
            }
        }
        throw new RuntimeException("Objeto não encontrado");
    }
}
