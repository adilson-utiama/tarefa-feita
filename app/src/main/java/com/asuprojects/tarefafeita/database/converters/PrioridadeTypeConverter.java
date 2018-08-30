package com.asuprojects.tarefafeita.database.converters;

import android.arch.persistence.room.TypeConverter;

import com.asuprojects.tarefafeita.domain.enums.Prioridade;

public class PrioridadeTypeConverter {

    @TypeConverter
    public static Prioridade toEnum(String value){
        return Prioridade.toEnum(value);
    }

    @TypeConverter
    public static String toString(Prioridade value){
        return (value == null) ? null : value.getDescricao();
    }
}
