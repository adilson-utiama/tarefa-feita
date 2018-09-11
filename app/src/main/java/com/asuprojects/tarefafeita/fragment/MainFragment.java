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
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.asuprojects.tarefafeita.MainActivity;
import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.activity.TarefaActivity;
import com.asuprojects.tarefafeita.adapter.RecyclerViewAdapter;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;
import com.asuprojects.tarefafeita.util.GeradorTarefa;
import com.asuprojects.tarefafeita.util.RecyclerViewItemListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private TarefaViewModel viewModel;

    private Tarefa tarefa;
    private TextView dataAtual;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        dataAtual = view.findViewById(R.id.dataAtual);

        setaDataAtual();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.adapter = new RecyclerViewAdapter(new ArrayList<Tarefa>());
        recyclerView = view.findViewById(R.id.recyclerViewSelecionados);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.adapter);

        viewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);
        viewModel.getTarefasOrdenadoPorData().observe(MainFragment.this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(@Nullable List<Tarefa> tasks) {
                MainFragment.this.adapter.setListaTarefas(tasks);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerViewItemListener(
                getContext(),
                recyclerView,
                new RecyclerViewItemListener.OnClickItemListener() {
                    @Override
                    public void onClickItem(View view, int position) {
                        tarefa = MainFragment.this.adapter.getTarefa(position);
                        mostrDialogStatus();

                    }

                    @Override
                    public void onClickItemLongo(View view, int position) {
                        tarefa = MainFragment.this.adapter.getTarefa(position);

                        CharSequence[] opcoes = new CharSequence[2];
                        opcoes[0] = "Editar";
                        opcoes[1] = "Deletar";

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Escolha uma Opção");
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

    private void setaDataAtual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd 'de' MMMM");
        Calendar atual = Calendar.getInstance();
        String dataExtenso = dateFormat.format(atual.getTime());
        dataAtual.setText(dataExtenso);

    }

    private void mostrDialogStatus() {
        final CharSequence[] sequences = statusCharSequence();
        int id = 0;
        for(int i = 0; i < sequences.length; i++){
            if(tarefa.getStatus().getDescricao().equals(sequences[i])){
                id = i;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Mudar Status")
                .setSingleChoiceItems(sequences, id ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tarefa.setStatus(Status.toEnum(sequences[i].toString()));
                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                viewModel.atualiza(tarefa);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private CharSequence[] statusCharSequence() {
        CharSequence[] s = new CharSequence[Status.values().length];
        Status[] status = Status.values();
        for(int i = 0; i < status.length; i++){
            s[i] = status[i].getDescricao();
        }
        return s;
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


}
