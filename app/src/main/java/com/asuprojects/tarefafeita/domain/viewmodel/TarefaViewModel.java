package com.asuprojects.tarefafeita.domain.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.asuprojects.tarefafeita.database.repository.TarefaRepository;
import com.asuprojects.tarefafeita.domain.Tarefa;

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

    public void adiciona(Tarefa tarefa){
        repository.adiciona(tarefa);
    }

    public void atualiza(Tarefa tarefa){
        repository.atualiza(tarefa);
    }

    public void remove(Tarefa tarefa){
        repository.deleta(tarefa);
    }
}
