package com.asuprojects.tarefafeita.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.asuprojects.tarefafeita.domain.Tarefa;

import java.util.Calendar;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TarefaDao {

    @Query("SELECT * FROM tabela_tarefa")
    LiveData<List<Tarefa>> listarTodos();

    @Insert(onConflict = REPLACE)
    void adiciona(Tarefa tarefa);

    @Update(onConflict = REPLACE)
    void atualiza(Tarefa tarefa);

    @Delete
    void remove(Tarefa tarefa);

    @Query("SELECT * FROM tabela_tarefa ORDER BY dataConlusao DESC")
    LiveData<List<Tarefa>> listaOrdenadaPorDataConclusao();

    @Query("SELECT * FROM  tabela_tarefa WHERE date(dataConlusao) = date(:data) " +
            "AND dataDefinida = 1 ORDER BY dataConlusao ASC")
    LiveData<List<Tarefa>> listaDoDia(Calendar data);

    @Query("SELECT * FROM tabela_tarefa WHERE dataDefinida = 0")
    LiveData<List<Tarefa>> listaTarefasSemDataDefinida();

    @Query("DELETE FROM tabela_tarefa WHERE date(dataConlusao) < date(:data)")
    void apagarTarefasAntigas(Calendar data);

    @Query("SELECT count() FROM tabela_tarefa WHERE date(dataConlusao) < date(:data)")
    int quantidadeTarefasAntigas(Calendar data);

}
