package com.asuprojects.tarefafeita.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Status;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;

import java.util.List;

public class ResumoFragment extends Fragment {

    private TextView tarefasTotais;
    private TextView tarefasConcluidas;
    private TextView tarefasNaoConcluidas;
    private TextView tarefasCanceladas;
    private TextView tarefasDataIndefinida;

    private List<Tarefa> listaTarefas;

    public ResumoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumo, container, false);

        tarefasTotais = view.findViewById(R.id.totalTarefas);
        tarefasConcluidas = view.findViewById(R.id.tarefasConcluidas);
        tarefasNaoConcluidas  = view.findViewById(R.id.tarefasNaoConcluidas);
        tarefasCanceladas = view.findViewById(R.id.tarefasCanceladas);
        tarefasDataIndefinida = view.findViewById(R.id.semDataIndefinida);

        TarefaViewModel viewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);
        viewModel.getTarefas().observe(ResumoFragment.this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(@Nullable List<Tarefa> tarefas) {
                listaTarefas = tarefas;

                int total = listaTarefas.size();
                int concluidos = totalFromStatus(Status.CONCLUIDO);
                int naoConcluidos = totalFromStatus(Status.ADICIONADO);
                int cancelados = totalFromStatus(Status.CANCELADO);
                int tarefasLivre = totalTarefasLivre();

                tarefasTotais.setText(String.valueOf(total));
                tarefasConcluidas.setText(String.valueOf(concluidos));
                tarefasNaoConcluidas.setText(String.valueOf(naoConcluidos));
                tarefasCanceladas.setText(String.valueOf(cancelados));
                tarefasDataIndefinida.setText(String.valueOf(tarefasLivre));
            }
        });

        return view;
    }

    private int totalFromStatus(Status status) {
        int total = 0;
        for(Tarefa t : listaTarefas){
            if(t.getStatus().equals(status)){
                total++;
            }
        }
        return total;
    }

    private int totalTarefasLivre(){
        int total = 0;
        for(Tarefa t : listaTarefas){
            if(!t.isDataDefinida()){
                total++;
            }
        }
        return total;
    }

}
