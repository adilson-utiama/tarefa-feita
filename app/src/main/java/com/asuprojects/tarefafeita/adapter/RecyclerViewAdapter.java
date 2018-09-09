package com.asuprojects.tarefafeita.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.Tarefa;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Tarefa> tarefas;

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
        final TarefaViewHolder viewholder = (TarefaViewHolder) holder;

        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

        viewholder.titulo.setText(tarefa.getTitulo());
        viewholder.anotacao.setText(tarefa.getAnotacao());
        viewholder.dataConclusao.setText(dataFormat.format(tarefa.getDataConlusao().getTime()));
        if(tarefa.getPrioridade().equals(Prioridade.ALTA)){
            viewholder.prioridade.setTextColor(Prioridade.ALTA.getCor());
        } else if(tarefa.getPrioridade().equals(Prioridade.MEDIA)){
            viewholder.prioridade.setTextColor(Prioridade.MEDIA.getCor());
        } else {
            viewholder.prioridade.setTextColor(Prioridade.BAIXA.getCor());
        }
        viewholder.prioridade.setText(tarefa.getPrioridade().getDescricao());
        viewholder.status.setText(tarefa.getStatus().getDescricao());
        viewholder.horario.setText(timeFormat.format(tarefa.getDataConlusao().getTime()));
    }

    @Override
    public int getItemCount() {
        return (tarefas != null) ? tarefas.size() : 0;
    }

    public void setListaTarefas(List<Tarefa> listaTarefas) {
        this.tarefas = listaTarefas;
        notifyDataSetChanged();
    }

    public Tarefa getTarefa(int posicao){
        return this.tarefas.get(posicao);
    }

    class TarefaViewHolder extends RecyclerView.ViewHolder{

        private TextView dataConclusao;
        private TextView horario;
        private TextView titulo;
        private TextView anotacao;
        private TextView prioridade;
        private TextView status;

        public TarefaViewHolder(View itemView) {
            super(itemView);

            dataConclusao = itemView.findViewById(R.id.tarefa_data_conclusao);
            horario = itemView.findViewById(R.id.tarefa_horario);
            titulo = itemView.findViewById(R.id.tarefa_titulo);
            anotacao = itemView.findViewById(R.id.tarefa_anotacao);
            prioridade = itemView.findViewById(R.id.tarefa_prioridade);
            status = itemView.findViewById(R.id.tarefa_status);
        }
    }
}
