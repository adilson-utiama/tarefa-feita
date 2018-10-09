package com.asuprojects.tarefafeita.fragment;

public enum TipoLista {

    LISTA_DO_DIA(1),
    LISTA_TAREFAS_INDEFINIDAS(2),
    LISTA_GERAL(3);

    private int code;

    TipoLista(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
