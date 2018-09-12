package com.asuprojects.tarefafeita.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DrawableUtils;
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
import com.asuprojects.tarefafeita.domain.enums.Status;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Tarefa> tarefas;

    private Drawable concluido;
    private Drawable naoConcluido;

    public RecyclerViewAdapter(List<Tarefa> tarefas){
        this.tarefas = tarefas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        concluido = parent.getResources().getDrawable(R.drawable.ic_done);
        naoConcluido = parent.getResources().getDrawable(R.drawable.ic_time);
        return new TarefaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_tarefa, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Tarefa tarefa = tarefas.get(position);
        TarefaViewHolder viewholder = (TarefaViewHolder) holder;

        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        viewholder.titulo.setText(tarefa.getTitulo());
        viewholder.dataConclusao.setText(dataFormat.format(tarefa.getDataConlusao().getTime()));
        if(tarefa.getPrioridade().equals(Prioridade.ALTA)){
            viewholder.prioridade.setTextColor(Prioridade.ALTA.getCor());
        } else if(tarefa.getPrioridade().equals(Prioridade.MEDIA)){
            viewholder.prioridade.setTextColor(Prioridade.MEDIA.getCor());
        } else {
            viewholder.prioridade.setTextColor(Prioridade.BAIXA.getCor());
        }
        viewholder.prioridade.setText(tarefa.getPrioridade().getDescricao());

        if(tarefa.getStatus().getDescricao().equals(Status.CONCLUIDO.getDescricao())){
            viewholder.status.setTextColor(Color.GREEN);
            viewholder.iconStatus.setImageDrawable(concluido);
        }else{
            viewholder.status.setTextColor(Color.GRAY);
            viewholder.iconStatus.setImageDrawable(naoConcluido);
        }
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
        private TextView prioridade;
        private TextView status;
        private ImageView iconStatus;

        public TarefaViewHolder(View itemView) {
            super(itemView);

            dataConclusao = itemView.findViewById(R.id.tarefa_data_conclusao);
            horario = itemView.findViewById(R.id.tarefa_horario);
            titulo = itemView.findViewById(R.id.tarefa_titulo);
            prioridade = itemView.findViewById(R.id.tarefa_prioridade);
            status = itemView.findViewById(R.id.tarefa_status);
            iconStatus = itemView.findViewById(R.id.icon_status);
        }
    }
}
