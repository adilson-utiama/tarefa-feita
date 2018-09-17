package com.asuprojects.tarefafeita.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainFragment extends Fragment {

    private TarefaViewModel viewModel;

    private TextView dataAtual;

    private int listSize;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        dataAtual = view.findViewById(R.id.dataAtual);

        viewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);

        setaDataAtual();

        viewModel.getTarefasDoDia(Calendar.getInstance()).observe(MainFragment.this, new Observer<List<Tarefa>>() {
            @Override
            public void onChanged(@Nullable List<Tarefa> tasks) {
                listSize = tasks.size();

                if(listSize > 0){
                    FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                    RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd 'de' MMMM");
        Calendar atual = Calendar.getInstance();
        String dataExtenso = dateFormat.format(atual.getTime());
        dataAtual.setText(dataExtenso);
    }


}
