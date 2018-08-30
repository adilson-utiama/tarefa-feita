package com.asuprojects.tarefafeita.domain;

import android.graphics.Color;

public enum Prioridade {

    BAIXA(1, "BAIXA", Color.rgb(31, 186, 34)),
    MEDIA(2, "MEDIA", Color.rgb(255,165,48)),
    ALTA(3, "ALTA", Color.rgb(255,48,48));

    private Integer cod;
    private String descricao;
    private Integer cor;

    Prioridade(Integer cod, String descricao, Integer cor){
        this.cod = cod;
        this.descricao = descricao;
        this.cor = cor;
    }

    public Integer getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getCor() {
        return cor;
    }
}
