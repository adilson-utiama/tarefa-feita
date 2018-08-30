package com.asuprojects.tarefafeita.util;

import com.asuprojects.tarefafeita.domain.Prioridade;
import com.asuprojects.tarefafeita.domain.Status;
import com.asuprojects.tarefafeita.domain.Tarefa;

import java.util.Calendar;
import java.util.Random;

public class GeradorTarefa {

    private Random rand = new Random();

    static String[] titulos = {
            "Fazer Faxina no Quarto", "Fazer Compras no Supermercado", "Comprar Tinta para pintar parede",
            "Levar Pet para Banho em Petshop", "Arrumar Sujeira no Quintal"
    };

    private String[] anotacoes = {
            "Fazer com cautela", "Nada a constar", "Anotacoes desnecessarias", "Nao esquecer coisas",
            "So o Necessario"
    };

    private Prioridade[] prioridades = {
            Prioridade.BAIXA, Prioridade.MEDIA, Prioridade.ALTA
    };

    private Status[] status = {
            Status.ADICIONADO, Status.EXECUTANDO, Status.CANCELADO, Status.CONCLUIDO
    };

    public Tarefa gerar(){
        Tarefa tr = new Tarefa();
        tr.setTitulo(titulos[rand.nextInt(titulos.length)]);
        tr.setAnotacao(anotacoes[rand.nextInt(anotacoes.length)]);
        tr.setDataIncluida(gerarAleatorio());
        tr.setDataConlusao(gerarAleatorio());
        tr.setPrioridade(prioridades[rand.nextInt(prioridades.length)]);
        tr.setStatus(status[rand.nextInt(status.length)]);
        return tr;
    }

    private Calendar gerarAleatorio(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, rand.nextInt(12), rand.nextInt(28));
        return calendar;
    }
}
