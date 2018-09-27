package com.asuprojects.tarefafeita.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.asuprojects.tarefafeita.database.converters.CalendarTypeConverter;
import com.asuprojects.tarefafeita.database.converters.PrioridadeTypeConverter;
import com.asuprojects.tarefafeita.database.converters.StatusTypeConverter;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.enums.Status;

import java.io.Serializable;
import java.util.Calendar;

@Entity(tableName = "tabela_tarefa")
public class Tarefa implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String titulo;

    @TypeConverters(CalendarTypeConverter.class)
    private Calendar dataIncluida;

    @TypeConverters(CalendarTypeConverter.class)
    private Calendar dataConlusao;

    private String anotacao;

    @TypeConverters(PrioridadeTypeConverter.class)
    private Prioridade prioridade;

    @TypeConverters(StatusTypeConverter.class)
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ID: ").append(getId()).append("Titulo: ").append(getTitulo())
                .append("Data Conclusao: ").append(getDataConlusao().getTime())
                .append("Prioridade: ").append(getPrioridade().getDescricao());
        return builder.toString();
    }
}
