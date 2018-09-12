package com.asuprojects.tarefafeita.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.adapter.RecyclerViewAdapter;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;
import com.asuprojects.tarefafeita.util.GeradorTarefa;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends Fragment {

    private List<Tarefa> tarefas = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private TarefaViewModel viewModel;

    public ListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        GeradorTarefa gerador = new GeradorTarefa();
        tarefas = new ArrayList<>();
        tarefas.add(gerador.gerar());
        tarefas.add(gerador.gerar());
        tarefas.add(gerador.gerar());
        tarefas.add(gerador.gerar());
        tarefas.add(gerador.gerar());
        tarefas.add(gerador.gerar());
        tarefas.add(gerador.gerar());
        tarefas.add(gerador.gerar());
        tarefas.add(gerador.gerar());

        adapter = new RecyclerViewAdapter(tarefas);
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

        return view;
    }

    public void setLista(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
}
