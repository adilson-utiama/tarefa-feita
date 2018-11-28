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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.activity.TarefaActivity;
import com.asuprojects.tarefafeita.adapter.ClickMenuListener;
import com.asuprojects.tarefafeita.adapter.RecyclerViewAdapter;
import com.asuprojects.tarefafeita.adapter.SwitthChangeListener;
import com.asuprojects.tarefafeita.database.repository.TarefaRepository;
import com.asuprojects.tarefafeita.domain.Tarefa;
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

    private RecyclerView recyclerView;

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
        this.adapter = new RecyclerViewAdapter(getActivity(), new ArrayList<Tarefa>());
        recyclerView = view.findViewById(R.id.recyclerViewSelecionados);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.adapter);

        viewModel = ViewModelProviders.of(this).get(TarefaViewModel.class);

        if(tipoLista != null){
            verificaTipoLista();
        } else {
            throw new RuntimeException("Necessario informar tipo de lista");
        }

        adicionaListenerSwitchAdapter();
        //adicionaClickListener();

        return view;
    }

    private void adicionaListenerSwitchAdapter() {
        adapter.setListener(new SwitthChangeListener() {
            @Override
            public void onChecked(int position, CompoundButton buttonView, boolean isChecked) {
                adapter.setListener(null);

                Log.i("CHECKED", "Checked: " + isChecked + " | Position: " + position);
                Tarefa tarefa = RecyclerViewFragment.this.adapter.getTarefa(position);
                tarefa.setConcluido(isChecked);
                viewModel.atualiza(tarefa);

                adapter.setListener(this);
            }
        });
        adapter.setMenuListener(new ClickMenuListener() {
            @Override
            public void onClickMenu(View view,final int position) {
                tarefa = adapter.getTarefa(position);
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater()
                        .inflate(R.menu.menu_item, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.details_task:
                                mostraDialogStatus();
                                return true;
                            case R.id.edit_task:
                                Intent editarTarefa = new Intent(getActivity(), TarefaActivity.class);
                                editarTarefa.putExtra(TarefaActivity.EDITAR_TAREFA, tarefa);
                                startActivity(editarTarefa);
                                return true;
                            case R.id.delete_task:
                                mostraDialogRemocao(tarefa);
                                return true;
                         }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void adicionaClickListener() {
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.escolher_opcao);
                        builder.setItems(R.array.menu_onlong_click_dialog, new DialogInterface.OnClickListener() {
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
                viewModel.getTarefas(Calendar.getInstance()).observe(RecyclerViewFragment.this, new Observer<List<Tarefa>>() {
                    @Override
                    public void onChanged(@Nullable List<Tarefa> tasks) {
                        RecyclerViewFragment.this.adapter.setListaTarefas(tasks);
                    }
                });
                break;
            case 2:
                viewModel.getTarefasSemDataDefinida().observe(RecyclerViewFragment.this, new Observer<List<Tarefa>>() {
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
        builder.setTitle("Detalhes da Tarefa");
        builder.setPositiveButton("OK", null);
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
        TextView status = view.findViewById(R.id.textView_status);
        if(tarefa.isConcluido()){
            status.setText("Concluido");
            status.setTextColor(getResources().getColor(R.color.prioridadeBaixa));
        }

        defineTextoDataConclusao(dataConclusao, horario);

        return view;
    }

    private void defineTextoDataConclusao(TextView dataConclusao, TextView horario) {
        if(tarefa.isDataDefinida()){
            dataConclusao.setText(DataFormatterUtil.formatarData(tarefa.getDataConlusao()));
            horario.setText(DataFormatterUtil.formataHora(tarefa.getDataConlusao()));
        } else {
            dataConclusao.setText(getString(R.string.rotulo_data_indefinida));
            horario.setText("");
        }
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
