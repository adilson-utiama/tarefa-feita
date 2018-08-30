package com.asuprojects.tarefafeita.database.converters;

import android.arch.persistence.room.TypeConverter;

import com.asuprojects.tarefafeita.domain.enums.Status;

public class StatusTypeConverter {

    @TypeConverter
    public static Status toEnum(String value){
        return Status.toEnum(value);
    }

    @TypeConverter
    public static String toString(Status value){
        return (value == null) ? null : value.getDescricao();
    }
}
