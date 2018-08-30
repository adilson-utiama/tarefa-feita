package com.asuprojects.tarefafeita.domain;

import java.io.Serializable;
import java.util.Calendar;

public class Tarefa implements Serializable {

    private long id;
    private String titulo;
    private Calendar dataIncluida;
    private Calendar dataConlusao;
    private String anotacao;
    private Prioridade prioridade;
    private Status status;

    public Tarefa() {
        this.dataIncluida = Calendar.getInstance();
        this.status = Status.ADICIONADO;
    }

    public Tarefa(String titulo, Calendar dataConlusao, String anotacao, Prioridade prioridade) {
        this();
        this.titulo = titulo;
        this.dataConlusao = dataConlusao;
        this.anotacao = anotacao;
        this.prioridade = prioridade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Calendar getDataIncluida() {
        return dataIncluida;
    }

    public void setDataIncluida(Calendar dataIncluida) {
        this.dataIncluida = dataIncluida;
    }

    public Calendar getDataConlusao() {
        return dataConlusao;
    }

    public void setDataConlusao(Calendar dataConlusao) {
        this.dataConlusao = dataConlusao;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
