package com.asuprojects.tarefafeita.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.Tarefa;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Tarefa> tarefas;
    private List<Tarefa> listaTarefas;

    public RecyclerViewAdapter(List<Tarefa> tarefas){
        this.tarefas = tarefas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TarefaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_tarefa, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Tarefa tarefa = tarefas.get(position);
        TarefaViewHolder viewholder = (TarefaViewHolder) holder;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        viewholder.titulo.setText(tarefa.getTitulo());
        viewholder.anotacao.setText(tarefa.getAnotacao());
        viewholder.dataConclusao.setText(format.format(tarefa.getDataConlusao().getTime()));
        if(tarefa.getPrioridade().equals(Prioridade.ALTA)){
            viewholder.prioridade.setTextColor(Prioridade.ALTA.getCor());
        } else if(tarefa.getPrioridade().equals(Prioridade.MEDIA)){
            viewholder.prioridade.setTextColor(Prioridade.MEDIA.getCor());
        } else {
            viewholder.prioridade.setTextColor(Prioridade.BAIXA.getCor());
        }
        viewholder.prioridade.setText(tarefa.getPrioridade().getDescricao());

    }

    @Override
    public int getItemCount() {
        return (tarefas != null) ? tarefas.size() : 0;
    }

    public void setListaTarefas(List<Tarefa> listaTarefas) {
        this.listaTarefas = listaTarefas;
    }


    class TarefaViewHolder extends RecyclerView.ViewHolder{

        private TextView dataConclusao;
        private TextView titulo;
        private TextView anotacao;
        private TextView prioridade;

        public TarefaViewHolder(View itemView) {
            super(itemView);

            dataConclusao = itemView.findViewById(R.id.tarefa_data_conclusao);
            titulo = itemView.findViewById(R.id.tarefa_titulo);
            anotacao = itemView.findViewById(R.id.tarefa_anotacao);
            prioridade = itemView.findViewById(R.id.tarefa_prioridade);
        }
    }
}
