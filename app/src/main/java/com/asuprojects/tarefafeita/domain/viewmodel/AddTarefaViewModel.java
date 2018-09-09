package com.asuprojects.tarefafeita.domain.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.asuprojects.tarefafeita.database.repository.TarefaRepository;
import com.asuprojects.tarefafeita.domain.Tarefa;

public class AddTarefaViewModel extends AndroidViewModel {

    private TarefaRepository repo;

    public AddTarefaViewModel(@NonNull Application application) {
        super(application);

        repo = new TarefaRepository(getApplication());
    }

    public void adiciona(Tarefa tarefa){
        repo.adiciona(tarefa);
    }

    public void atualiza(Tarefa tarefa){
        repo.atualiza(tarefa);
    }
}
