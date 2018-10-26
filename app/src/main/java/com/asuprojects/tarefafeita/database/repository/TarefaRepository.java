package com.asuprojects.tarefafeita.database.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.asuprojects.tarefafeita.database.TarefaRoomDatabase;
import com.asuprojects.tarefafeita.database.dao.TarefaDao;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public LiveData<List<Tarefa>> getTarefasDoDia(Calendar data, Prioridade prioridade) {
        return tarefaDao.listaDoDia(data, prioridade);
    }

    public LiveData<List<Tarefa>> getTarefasPela(Prioridade prioridade){
        return tarefaDao.listarPor(prioridade);
    }

    public Integer quantidadeTarefasAntigas(Calendar data){
        Integer quant = 0;
        try {
            quant = new quantidadeTarefasAntigas(tarefaDao).execute(data).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return quant;
    }

    public void adiciona (Tarefa tarefa) {
        new insertAsyncTask(tarefaDao).execute(tarefa);
    }

    public void deleta(Tarefa tarefa){
        new removeAsyncTask(tarefaDao).execute(tarefa);
    }

    public void atualiza(Tarefa tarefa){
        new atualizaAsyncTask(tarefaDao).execute(tarefa);
    }

    public void apagarTarefasAntigas(Calendar data) {
        new apagarTarefasAntigas(tarefaDao).execute(data);
    }

    private static class insertAsyncTask extends AsyncTask<Tarefa, Void, Void> {
        private TarefaDao asyncTaskDao;
        insertAsyncTask(TarefaDao dao) {
            asyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Tarefa... params) {
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

    private static class atualizaAsyncTask extends AsyncTask<Tarefa, Void, Void>{
        private TarefaDao asyncTaskDao;
        atualizaAsyncTask(TarefaDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Tarefa... tarefas) {
            asyncTaskDao.atualiza(tarefas[0]);
            return null;
        }
    }

    private static class apagarTarefasAntigas extends AsyncTask<Calendar, Void, Void>{
        private TarefaDao asyncTaskDao;

        apagarTarefasAntigas(TarefaDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Calendar... data) {
            asyncTaskDao.apagarTarefasAntigas(data[0]);
            return null;
        }
    }

    private static class quantidadeTarefasAntigas extends AsyncTask<Calendar, Void, Integer>{
        private TarefaDao asyncTaskDao;

        quantidadeTarefasAntigas(TarefaDao dao){
            asyncTaskDao = dao;
        }
        
        @Override
        protected Integer doInBackground(Calendar... data) {
            int quant = asyncTaskDao.quantidadeTarefasAntigas(data[0]);
            return quant;
        }

    }

}
