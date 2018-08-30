package com.asuprojects.tarefafeita.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.asuprojects.tarefafeita.MainActivity;
import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.adapter.RecyclerViewAdapter;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.util.RecyclerViewItemListener;

import java.util.List;


public class MainFragment extends Fragment {

    private List<Tarefa> tarefas;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        adapter = new RecyclerViewAdapter(tarefas);
        recyclerView = view.findViewById(R.id.recyclerViewSelecionados);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerViewItemListener(
                getContext(),
                recyclerView,
                new RecyclerViewItemListener.OnClickItemListener() {
                    @Override
                    public void onClickItem(View view, int position) {
                        Toast.makeText(getContext(), "onClickItem: " + position, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onClickItemLongo(View view, int position) {
                        Toast.makeText(getContext(), "onClickItemLongo: " + position , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));
        return view;

    }

    public void setLista(List<Tarefa> tarefas){
        this.tarefas = tarefas;
    }

}
