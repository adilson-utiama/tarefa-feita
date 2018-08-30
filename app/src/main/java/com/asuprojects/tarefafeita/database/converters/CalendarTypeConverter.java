package com.asuprojects.tarefafeita.database.converters;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;
import java.util.Date;

public class CalendarTypeConverter {

    @TypeConverter
    public static Calendar toCalendar(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    @TypeConverter
    public static Calendar toCalendar(String data){
        Date date = new Date();
        date.setTime(Long.parseLong(data));
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    @TypeConverter
    public static String toString(Calendar value){
        return value.getTime().toString();
    }
}
