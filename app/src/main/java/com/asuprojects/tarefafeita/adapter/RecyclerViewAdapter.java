package com.asuprojects.tarefafeita.adapter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.database.repository.TarefaRepository;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter
                implements SwitthChangeListener, ClickMenuListener{

    private List<Tarefa> tarefas;
    private Context ctx;

    private Drawable concluido;
    private Drawable naoConcluido;
    private Drawable cancelado;

    private SwitthChangeListener listener;
    private ClickMenuListener menuListener;

    public RecyclerViewAdapter(Context ctx, List<Tarefa> tarefas){
        this.ctx = ctx;
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

        defineTextoData(tarefa, viewholder);;
        //definiIconeStatus(tarefa, viewholder);

        viewholder.desativaSwitch();

        if(tarefa.isConcluido()){
            switchAtivado(viewholder);
        } else {
            switchDesativado(viewholder);
        }

        viewholder.ativaSwitch();

        Log.i("VIEWHOLDER", "onBindViewHolder: " + position);
    }

    private void switchDesativado(TarefaViewHolder viewholder) {
        viewholder.switchConclusao.setChecked(false);
        viewholder.switchConclusao.setText("Pendente");
        //viewholder.iconStatus.setImageDrawable(naoConcluido);
        viewholder.tarjaStatus.setBackgroundColor(Status.ADICIONADO.getColor());
        viewholder.tarjaStatus.setImageDrawable(naoConcluido);
    }

    private void switchAtivado(TarefaViewHolder viewholder) {
        viewholder.switchConclusao.setChecked(true);
        viewholder.switchConclusao.setText("Concluido");
        //viewholder.iconStatus.setImageDrawable(concluido);
        viewholder.tarjaStatus.setBackgroundColor(Status.CONCLUIDO.getColor());
        viewholder.tarjaStatus.setImageDrawable(concluido);
    }

    private void defineTextoData(Tarefa tarefa, TarefaViewHolder viewholder) {
        if(tarefa.getStatus().equals(Status.CANCELADO)){
            viewholder.dataConclusao.setText(R.string.rotulo_tarefa_cancelada);
            viewholder.horario.setText("");
        } else {
            if(tarefa.isDataDefinida()){
                viewholder.dataConclusao.setText(DataFormatterUtil.formatarData(tarefa.getDataConlusao()));
                viewholder.horario.setText(DataFormatterUtil.formataHora(tarefa.getDataConlusao()));
            } else {
                viewholder.dataConclusao.setText(R.string.rotulo_data_indefinida);
                viewholder.horario.setText("");
            }
        }
    }

//    private void definiIconeStatus(Tarefa tarefa, TarefaViewHolder viewholder) {
//        if(tarefa.getStatus().equals(Status.CONCLUIDO)){
//            viewholder.iconStatus.setImageDrawable(concluido);
//            viewholder.tarjaStatus.setBackgroundColor(Status.CONCLUIDO.getColor());
//        }else{
//            viewholder.iconStatus.setImageDrawable(naoConcluido);
//            viewholder.tarjaStatus.setBackgroundColor(Status.ADICIONADO.getColor());
//        }
//        if(tarefa.getStatus().equals(Status.CANCELADO)){
//            viewholder.iconStatus.setImageDrawable(cancelado);
//            viewholder.tarjaStatus.setBackgroundColor(Status.CANCELADO.getColor());
//        }
//    }

    @Override
    public int getItemCount() {
        return (tarefas != null) ? tarefas.size() : 0;
    }

    public void setListaTarefas(List<Tarefa> listaTarefas) {
        this.tarefas = listaTarefas;
        notifyDataSetChanged();
    }

    public void setListener(SwitthChangeListener listener) {
        this.listener = listener;
    }

    public void setMenuListener(ClickMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public Tarefa getTarefa(int posicao){
        return this.tarefas.get(posicao);
    }

    @Override
    public void onChecked(int position, CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onClickMenu(View view, int position) {

    }


    class TarefaViewHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

        private TextView dataConclusao;
        private TextView horario;
        private TextView titulo;
        private ImageView iconMenu;
        private ImageView tarjaStatus;
        private SwitchCompat switchConclusao;

        public TarefaViewHolder(View itemView) {
            super(itemView);

            dataConclusao = itemView.findViewById(R.id.tarefa_data_conclusao);
            horario = itemView.findViewById(R.id.tarefa_horario);
            titulo = itemView.findViewById(R.id.tarefa_titulo);
            iconMenu = itemView.findViewById(R.id.icon_status);
            iconMenu.setOnClickListener(this);
            tarjaStatus = itemView.findViewById(R.id.tarja_status);
            switchConclusao = itemView.findViewById(R.id.switch_conclusao);
            switchConclusao.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                switchConclusao.setText("Concluido");
                //iconStatus.setImageDrawable(concluido);
                tarjaStatus.setBackgroundColor(Status.CONCLUIDO.getColor());
                tarjaStatus.setImageDrawable(concluido);
            } else {
                switchConclusao.setText("Pendente");
                //iconStatus.setImageDrawable(naoConcluido);
                tarjaStatus.setBackgroundColor(Status.ADICIONADO.getColor());
                tarjaStatus.setImageDrawable(naoConcluido);
            }
            if(listener != null){
                listener.onChecked(getAdapterPosition(), buttonView, isChecked);
            }
        }

        @Override
        public void onClick(View view) {
            if(menuListener != null){
                menuListener.onClickMenu(view, getAdapterPosition());
            }
        }

        public void desativaSwitch(){
            switchConclusao.setOnCheckedChangeListener(null);
        }

        public void ativaSwitch(){
            switchConclusao.setOnCheckedChangeListener(this);
        }
    }
}
