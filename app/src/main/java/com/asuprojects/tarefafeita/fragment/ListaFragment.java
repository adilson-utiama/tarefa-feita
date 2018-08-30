package com.asuprojects.tarefafeita.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.adapter.RecyclerViewAdapter;
import com.asuprojects.tarefafeita.domain.Tarefa;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListaFragment extends Fragment {

    private List<Tarefa> tarefas = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    public ListaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        adapter = new RecyclerViewAdapter(tarefas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerViewLista);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void setLista(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }
}
