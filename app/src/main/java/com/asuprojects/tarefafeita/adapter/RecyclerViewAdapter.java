package com.asuprojects.tarefafeita.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Tarefa> tarefas;

    private Drawable concluido;
    private Drawable naoConcluido;
    private Drawable cancelado;

    public RecyclerViewAdapter(List<Tarefa> tarefas){
        this.tarefas = tarefas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        concluido = parent.getResources().getDrawable(R.drawable.vc_concluido);
        naoConcluido = parent.getResources().getDrawable(R.drawable.vc_nao_concluido);
        cancelado = parent.getResources().getDrawable(R.drawable.vc_cancelado);

        return new TarefaViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_tarefa, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Tarefa tarefa = tarefas.get(position);
        TarefaViewHolder viewholder = (TarefaViewHolder) holder;

        viewholder.titulo.setText(tarefa.getTitulo());
        viewholder.prioridade.setText(tarefa.getPrioridade().getDescricao());

        defineTextoData(tarefa, viewholder);
        defineCorTextoPrioridade(tarefa, viewholder);
        definiIconeStatus(tarefa, viewholder);

    }

    private void defineTextoData(Tarefa tarefa, TarefaViewHolder viewholder) {
        if(tarefa.getStatus().equals(Status.CANCELADO)){
            viewholder.dataConclusao.setText(R.string.rotulo_tarefa_cancelada);
            viewholder.horario.setText("");
        } else {
            if(!tarefa.getPrioridade().equals(Prioridade.NENHUM)){
                viewholder.dataConclusao.setText(DataFormatterUtil.formatarData(tarefa.getDataConlusao()));
                viewholder.horario.setText(DataFormatterUtil.formataHora(tarefa.getDataConlusao()));
            } else {
                viewholder.dataConclusao.setText(R.string.rotulo_data_indefinida);
                viewholder.horario.setText("");
            }
        }
    }

    private void defineCorTextoPrioridade(Tarefa tarefa, TarefaViewHolder viewholder) {
        switch(tarefa.getPrioridade().getCod()){
            case 1:
                viewholder.prioridade.setTextColor(Prioridade.BAIXA.getCor());
                break;
            case 2:
                viewholder.prioridade.setTextColor(Prioridade.MEDIA.getCor());
                break;
            case 3:
                viewholder.prioridade.setTextColor(Prioridade.ALTA.getCor());
                break;
            default:
                viewholder.prioridade.setTextColor(Prioridade.NENHUM.getCor());
        }
    }

    private void definiIconeStatus(Tarefa tarefa, TarefaViewHolder viewholder) {
        if(tarefa.getStatus().equals(Status.CONCLUIDO)){
            viewholder.iconStatus.setImageDrawable(concluido);
            viewholder.tarjaStatus.setBackgroundColor(Prioridade.BAIXA.getCor());
        }else{
            viewholder.iconStatus.setImageDrawable(naoConcluido);
            viewholder.tarjaStatus.setBackgroundColor(Prioridade.MEDIA.getCor());
        }
        if(tarefa.getStatus().equals(Status.CANCELADO)){
            viewholder.iconStatus.setImageDrawable(cancelado);
            viewholder.tarjaStatus.setBackgroundColor(Prioridade.ALTA.getCor());
        }
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
        private ImageView iconStatus;
        private ImageView tarjaStatus;

        public TarefaViewHolder(View itemView) {
            super(itemView);

            dataConclusao = itemView.findViewById(R.id.tarefa_data_conclusao);
            horario = itemView.findViewById(R.id.tarefa_horario);
            titulo = itemView.findViewById(R.id.tarefa_titulo);
            prioridade = itemView.findViewById(R.id.tarefa_prioridade);
            iconStatus = itemView.findViewById(R.id.icon_status);
            tarjaStatus = itemView.findViewById(R.id.tarja_status);
        }
    }
}
