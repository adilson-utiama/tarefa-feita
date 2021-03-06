package com.asuprojects.tarefafeita.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;

import java.util.List;

public class ListaGeralFragment extends Fragment {

    private int listSize;

    public ListaGeralFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_geral, container, false);

        TarefaViewModel viewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);
        viewModel.getTarefasOrdenadoPorData().observe(this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(@Nullable List<Tarefa> tasks) {
                listSize = tasks.size();

                if(listSize > 0){
                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                    RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
                    recyclerViewFragment.setTipoLista(TipoLista.LISTA_GERAL);
                    tx.replace(R.id.frameLayoutListaGeral, recyclerViewFragment);
                    tx.commit();
                } else {
                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                    BlankFragment blankFragment = new BlankFragment();
                    tx.replace(R.id.frameLayoutListaGeral, blankFragment);
                    tx.commit();
                }
            }
        });

        return view;
    }

}
