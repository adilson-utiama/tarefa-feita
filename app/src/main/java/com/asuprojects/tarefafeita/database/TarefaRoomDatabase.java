package com.asuprojects.tarefafeita.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.asuprojects.tarefafeita.database.converters.CalendarTypeConverter;
import com.asuprojects.tarefafeita.database.converters.StatusTypeConverter;
import com.asuprojects.tarefafeita.database.dao.TarefaDao;
import com.asuprojects.tarefafeita.domain.Tarefa;

@Database(entities = {Tarefa.class}, version = 1, exportSchema = false)
@TypeConverters({
        StatusTypeConverter.class,
        CalendarTypeConverter.class})
public abstract class TarefaRoomDatabase extends RoomDatabase {

    public static final String NOME_BANCO = "tarefas_db";
    private static TarefaRoomDatabase INSTANCIA;

    public abstract TarefaDao getTarefaDao();

    public static TarefaRoomDatabase getDatabase(final Context context){
        if(INSTANCIA == null){
            synchronized (TarefaRoomDatabase.class){
                if(INSTANCIA == null){
                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(),
                            TarefaRoomDatabase.class, NOME_BANCO)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCIA;
    }

}
