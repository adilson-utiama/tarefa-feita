package com.asuprojects.tarefafeita.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.activity.TarefaActivity;
import com.asuprojects.tarefafeita.adapter.RecyclerViewAdapter;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;
import com.asuprojects.tarefafeita.util.GeradorTarefa;
import com.asuprojects.tarefafeita.util.RecyclerViewItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends Fragment {

    private List<Tarefa> tarefas = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private Tarefa tarefa;
    private TarefaViewModel viewModel;

    public ListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);


        adapter = new RecyclerViewAdapter(new ArrayList<Tarefa>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewLista);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.adapter);

        viewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);
        viewModel.getTarefasOrdenadoPorData().observe(this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(@Nullable List<Tarefa> tarefas) {
                adapter.setListaTarefas(tarefas);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerViewItemListener(
                getContext(),
                recyclerView,
                new RecyclerViewItemListener.OnClickItemListener() {
                    @Override
                    public void onClickItem(View view, int position) {
                        tarefa = ListaFragment.this.adapter.getTarefa(position);
                        mostrDialogStatus();

                    }

                    @Override
                    public void onClickItemLongo(View view, int position) {
                        tarefa = ListaFragment.this.adapter.getTarefa(position);

                        CharSequence[] opcoes = new CharSequence[3];
                        opcoes[0] = "Editar";
                        opcoes[1] = "Deletar";
                        opcoes[2] = "Cancelar";

                        //View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.dialog_menu_opcoes, null, false);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Escolha uma Opção");
                        //builder.setView(viewDialog);
                        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selecao) {
                                if(selecao == 0){
                                    Intent intent = new Intent(getContext(), TarefaActivity.class);
                                    intent.putExtra("EDITAR_TAREFA", tarefa);
                                    startActivity(intent);
                                }
                                if(selecao == 1){
                                    mostraDialogRemocao(tarefa);
                                }
                                if(selecao == 2){
                                    tarefa.setStatus(Status.CANCELADO);
                                    viewModel.atualiza(tarefa);
                                }
                            }
                        });
                        builder.show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    }
                }
        ));

        return view;
    }

    private void mostrDialogStatus() {
        View view = preencherTarefaDetalheDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        if(!tarefa.getStatus().equals(Status.CONCLUIDO)){
            builder.setTitle("Tarefa Concluida?");
        }else{
            builder.setTitle("Desmarcar Concluir?");
        }
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!tarefa.getStatus().equals(Status.CONCLUIDO)){
                    tarefa.setStatus(Status.CONCLUIDO);
                }else{
                    tarefa.setStatus(Status.ADICIONADO);
                }
                viewModel.atualiza(tarefa);
            }
        });
        builder.setNeutralButton("NÂO", null);
        builder.setIcon(R.drawable.ic_question);
        builder.show();
    }

    private View preencherTarefaDetalheDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tarefa_detalhes, null);
        TextView dataInclusao = view.findViewById(R.id.detalhe_dataInclusao);
        dataInclusao.setText(DataFormatterUtil.formatarData(tarefa.getDataIncluida()));
        TextView dataConclusao = view.findViewById(R.id.detalhe_dataConclusao);
        dataConclusao.setText(DataFormatterUtil.formatarData(tarefa.getDataConlusao()));
        TextView horario = view.findViewById(R.id.detalhe_horario);
        horario.setText(DataFormatterUtil.formataHora(tarefa.getDataConlusao()));
        TextView titulo = view.findViewById(R.id.detalhe_titulo);
        titulo.setText(tarefa.getTitulo());
        TextView anotacao = view.findViewById(R.id.detalhe_anotacao);
        anotacao.setText(tarefa.getAnotacao());
        TextView prioridade = view.findViewById(R.id.detalhe_prioridade);
        if(tarefa.getPrioridade().equals(Prioridade.ALTA)){
            prioridade.setTextColor(Prioridade.ALTA.getCor());
        } else if(tarefa.getPrioridade().equals(Prioridade.MEDIA)){
            prioridade.setTextColor(Prioridade.MEDIA.getCor());
        } else {
            prioridade.setTextColor(Prioridade.BAIXA.getCor());
        }
        prioridade.setText(tarefa.getPrioridade().getDescricao());
        return view;
    }

    private void mostraDialogRemocao(final Tarefa tarefa) {
        StringBuilder builder = new StringBuilder();
        builder.append("Remover Tarefa ")
                .append("'").append(tarefa.getTitulo()).append("'").append(" ?");
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("DELETAR TAREFA");
        dialog.setMessage(builder.toString());
        dialog.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewModel.remove(tarefa);
            }
        });
        dialog.setNegativeButton("Cancelar", null);
        dialog.show();
    }

    public void setLista(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
}
