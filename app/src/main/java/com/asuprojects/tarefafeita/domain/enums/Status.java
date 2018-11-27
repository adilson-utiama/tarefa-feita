package com.asuprojects.tarefafeita.domain.enums;

import android.graphics.Color;

public enum Status {

    ADICIONADO(1,"Adicionado", Color.rgb(255,165,48)),
    CONCLUIDO(2,"Concluido", Color.rgb(31, 186, 34)),
    CANCELADO(3,"Cancelado", Color.rgb(255,48,48));

    private Integer codigo;
    private String descricao;
    private Integer color;

    Status(Integer codigo, String descricao, Integer color){
        this.codigo = codigo;
        this.descricao = descricao;
        this.color = color;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public Integer getColor() {
        return color;
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
