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
import android.widget.TextView;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;
import com.asuprojects.tarefafeita.domain.viewmodel.TarefaViewModel;
import com.asuprojects.tarefafeita.util.DataFormatterUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class ListaDoDiaFragment extends Fragment {

    private TextView dataAtual;
    private int listSize;

    public ListaDoDiaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_lista_do_dia, container, false);

        dataAtual = view.findViewById(R.id.dataAtual);
        setaDataAtual();

        TarefaViewModel viewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);
        viewModel.getTarefas(Calendar.getInstance(), Prioridade.NENHUM).observe(ListaDoDiaFragment.this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(@Nullable List<Tarefa> tasks) {
                listSize = tasks.size();

                if(listSize > 0){
                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                    RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
                    recyclerViewFragment.setTipoLista(TipoLista.LISTA_DO_DIA);
                    tx.replace(R.id.frameLayout, recyclerViewFragment);
                    tx.commit();

                } else {
                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                    BlankFragment blankFragment = new BlankFragment();
                    tx.replace(R.id.frameLayout, blankFragment);
                    tx.commit();
                }
            }
        });

        return view;

    }

    private void setaDataAtual() {
        Calendar atual = Calendar.getInstance();
        String dataExtenso = DataFormatterUtil.formataDataExtenso(atual);
        dataAtual.setText(dataExtenso);
    }


}
