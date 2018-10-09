package com.asuprojects.tarefafeita.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.asuprojects.tarefafeita.database.converters.CalendarTypeConverter;
import com.asuprojects.tarefafeita.database.converters.DateTypeConverter;
import com.asuprojects.tarefafeita.database.converters.PrioridadeTypeConverter;
import com.asuprojects.tarefafeita.database.converters.StatusTypeConverter;
import com.asuprojects.tarefafeita.database.dao.TarefaDao;
import com.asuprojects.tarefafeita.domain.Tarefa;

@Database(entities = {Tarefa.class}, version = 1)
@TypeConverters({
        DateTypeConverter.class,
        PrioridadeTypeConverter.class,
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
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCIA;
    }


    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCIA).execute();
                }
            };

    //Popula banco em processo assincrono
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TarefaDao dao;

        PopulateDbAsync(TarefaRoomDatabase db) {
            dao = db.getTarefaDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            return null;
        }
    }

}
