package com.asuprojects.tarefafeita.domain.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.asuprojects.tarefafeita.database.repository.TarefaRepository;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;

import java.util.Calendar;
import java.util.List;

public class TarefaViewModel extends AndroidViewModel {

    private TarefaRepository repository;

    private LiveData<List<Tarefa>> tarefas;

    public TarefaViewModel(@NonNull Application application) {
        super(application);
        repository = new TarefaRepository(application);
        tarefas = repository.getTodasTarefas();
    }

    public LiveData<List<Tarefa>> getTodasTarefas(){
        return tarefas;
    }

    public LiveData<List<Tarefa>> getTarefasOrdenadoPorData() {
        return repository.getTarefasOrdenadasPorData();
    }

    public LiveData<List<Tarefa>> getTarefasDoDia(Calendar data, Prioridade prioridade) {
        return repository.getTarefasDoDia(data, prioridade);
    }

    public LiveData<List<Tarefa>> getTarefasPelaPela(Prioridade prioridade) {
        return repository.getTarefasPela(prioridade);
    }

    public void adiciona(Tarefa tarefa){
        repository.adiciona(tarefa);
    }

    public void atualiza(Tarefa tarefa){
        repository.atualiza(tarefa);
    }

    public void remove(Tarefa tarefa){
        repository.deleta(tarefa);
    }

    public void apagarTarefasAntigas(Calendar data){
        repository.apagarTarefasAntigas(data);
    }
}
