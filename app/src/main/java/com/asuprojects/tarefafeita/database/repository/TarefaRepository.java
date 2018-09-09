package com.asuprojects.tarefafeita.database.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.asuprojects.tarefafeita.database.TarefaRoomDatabase;
import com.asuprojects.tarefafeita.database.dao.TarefaDao;
import com.asuprojects.tarefafeita.domain.Tarefa;

import java.util.Calendar;
import java.util.List;

public class TarefaRepository {

    private TarefaDao tarefaDao;
    private LiveData<List<Tarefa>> tarefas;

    public TarefaRepository(Application application){
        TarefaRoomDatabase db = TarefaRoomDatabase.getDatabase(application);
        tarefaDao = db.getTarefaDao();
        tarefas = tarefaDao.listarTodos();
    }

    public LiveData<List<Tarefa>> getTodasTarefas() {
        return tarefas;
    }

    public LiveData<List<Tarefa>> getTarefasOrdenadasPorData() {
        return tarefaDao.listaOrdenadaPorDataConclusao();
    }

    public LiveData<List<Tarefa>> getTarefasDoDia(Calendar data) {
        return tarefaDao.listaDoDia(data);
    }

    public void adiciona (Tarefa tarefa) {
        new insertAsyncTask(tarefaDao).execute(tarefa);
    }

    public void deleta(Tarefa tarefa){
        new removeAsyncTask(tarefaDao).execute(tarefa);
    }

    public void atualiza(Tarefa tarefa){
        tarefaDao.atualiza(tarefa);
    }

    private static class insertAsyncTask extends AsyncTask<Tarefa, Void, Void> {
        private TarefaDao asyncTaskDao;
        insertAsyncTask(TarefaDao dao) {
            asyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Tarefa... params) {
            Log.i("TAREFA", "doInBackground: " + params[0]);
            asyncTaskDao.adiciona(params[0]);
            return null;
        }
    }

    private static class removeAsyncTask extends AsyncTask<Tarefa, Void, Void>{

        private TarefaDao asyncTaskDao;
        removeAsyncTask(TarefaDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Tarefa... tarefas) {
            asyncTaskDao.remove(tarefas[0]);
            return null;
        }
    }
}
