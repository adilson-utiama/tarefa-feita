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

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.activity.TarefaActivity;
import com.asuprojects.tarefafeita.adapter.RecyclerViewAdapter;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;
import com.asuprojects.tarefafeita.util.RecyclerViewItemListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RecyclerViewFragment extends Fragment {

    public static final String TIPO_LISTA = "TIPO";
    public static final String EDITAR_TAREFA = "EDITAR_TAREFA";
    private RecyclerViewAdapter adapter;

    private TarefaViewModel viewModel;

    private Tarefa tarefa;

    private TipoLista tipoLista;

    public RecyclerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(TIPO_LISTA)){
                tipoLista = (TipoLista) savedInstanceState.getSerializable(TIPO_LISTA);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.adapter = new RecyclerViewAdapter(new ArrayList<Tarefa>());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSelecionados);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.adapter);

        viewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);

        if(tipoLista != null){
            verificaTipoLista();
        } else {
            throw new RuntimeException("Necessario informar tipo de lista");
        }


        recyclerView.addOnItemTouchListener(new RecyclerViewItemListener(
                getContext(),
                recyclerView,
                new RecyclerViewItemListener.OnClickItemListener() {
                    @Override
                    public void onClickItem(View view, int position) {
                        tarefa = RecyclerViewFragment.this.adapter.getTarefa(position);
                        mostraDialogStatus();
                    }

                    @Override
                    public void onClickItemLongo(View view, int position) {
                        tarefa = RecyclerViewFragment.this.adapter.getTarefa(position);

                        CharSequence[] opcoes = new CharSequence[3];
                        opcoes[0] = "Editar";
                        opcoes[1] = "Deletar";
                        opcoes[2] = "Cancelar";

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.escolher_opcao);
                        builder.setItems(opcoes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int selecao) {
                                if(selecao == 0){
                                    Intent intent = new Intent(getContext(), TarefaActivity.class);
                                    intent.putExtra(EDITAR_TAREFA, tarefa);
                                    startActivity(intent);
                                }
                                if(selecao == 1){
                                    mostraDialogRemocao(tarefa);
                                }
                                if(selecao == 2){
                                    mostrarDialogCancelarTarefa(tarefa);

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

    private void mostrarDialogCancelarTarefa(final Tarefa tarefa) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.cancelar_tarefa)
                .setMessage(tarefa.getTitulo())
                .setPositiveButton(getString(R.string.opcao_sim), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tarefa.setStatus(Status.CANCELADO);
                        viewModel.atualiza(tarefa);
                    }
                })
                .setNegativeButton(getString(R.string.opcao_nao), null)
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(TIPO_LISTA, tipoLista);
        super.onSaveInstanceState(outState);
    }

    private void verificaTipoLista() {
        switch(tipoLista.getCode()){
            case 1:
                viewModel.getTarefas(Calendar.getInstance(), Prioridade.NENHUM).observe(RecyclerViewFragment.this, new Observer<List<Tarefa>>() {
                    @Override
                    public void onChanged(@Nullable List<Tarefa> tasks) {
                        RecyclerViewFragment.this.adapter.setListaTarefas(tasks);
                    }
                });
                break;
            case 2:
                viewModel.getTarefas(Prioridade.NENHUM).observe(RecyclerViewFragment.this, new Observer<List<Tarefa>>() {
                    @Override
                    public void onChanged(@Nullable List<Tarefa> tasks) {
                        RecyclerViewFragment.this.adapter.setListaTarefas(tasks);
                    }
                });
                break;
            case 3:
                viewModel.getTarefasOrdenadoPorData().observe(RecyclerViewFragment.this, new Observer<List<Tarefa>>() {
                    @Override
                    public void onChanged(@Nullable List<Tarefa> tasks) {
                        RecyclerViewFragment.this.adapter.setListaTarefas(tasks);
                    }
                });
                break;
        }
    }

    public void setTipoLista(TipoLista tipoLista) {
        this.tipoLista = tipoLista;
    }

    private void mostraDialogStatus() {
        View view = preencherTarefaDetalheDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        if(!tarefa.getStatus().equals(Status.CONCLUIDO)){
            builder.setTitle(R.string.tarefa_esta_concluida);
        }else{
            builder.setTitle(R.string.desmarcar_tarefa_concluida);
        }
        builder.setPositiveButton(getString(R.string.opcao_sim), new DialogInterface.OnClickListener() {
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
        builder.setNeutralButton(getString(R.string.opcao_nao), null);
        builder.setIcon(R.drawable.ic_question);
        builder.show();
    }

    private View preencherTarefaDetalheDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_tarefa_detalhes, null);

        TextView dataInclusao = view.findViewById(R.id.detalhe_dataInclusao);
        TextView dataConclusao = view.findViewById(R.id.detalhe_dataConclusao);
        dataInclusao.setText(DataFormatterUtil.formatarData(tarefa.getDataIncluida()));

        TextView horario = view.findViewById(R.id.detalhe_horario);
        TextView titulo = view.findViewById(R.id.detalhe_titulo);

        titulo.setText(tarefa.getTitulo());
        TextView anotacao = view.findViewById(R.id.detalhe_anotacao);

        anotacao.setText(tarefa.getAnotacao());
        TextView prioridade = view.findViewById(R.id.detalhe_prioridade);

        defineTextoDataConclusao(dataConclusao, horario);
        defineCorTextPrioridade(prioridade);

        return view;
    }

    private void defineTextoDataConclusao(TextView dataConclusao, TextView horario) {
        if(!tarefa.getPrioridade().equals(Prioridade.NENHUM)){
            dataConclusao.setText(DataFormatterUtil.formatarData(tarefa.getDataConlusao()));
            horario.setText(DataFormatterUtil.formataHora(tarefa.getDataConlusao()));
        } else {
            dataConclusao.setText(getString(R.string.rotulo_data_indefinida));
            horario.setText("");
        }
    }

    private void defineCorTextPrioridade(TextView prioridade) {
        if(tarefa.getPrioridade().equals(Prioridade.ALTA)){
            prioridade.setTextColor(Prioridade.ALTA.getCor());
        } else if(tarefa.getPrioridade().equals(Prioridade.MEDIA)){
            prioridade.setTextColor(Prioridade.MEDIA.getCor());
        } else {
            prioridade.setTextColor(Prioridade.BAIXA.getCor());
        }
        prioridade.setText(tarefa.getPrioridade().getDescricao());
    }

    private void mostraDialogRemocao(final Tarefa tarefa) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.deletar_tarefa);
        dialog.setMessage(tarefa.getTitulo());
        dialog.setPositiveButton(R.string.opcao_deletar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewModel.remove(tarefa);
            }
        });
        dialog.setNegativeButton(R.string.opcao_cancelar, null);
        dialog.show();
    }

}
