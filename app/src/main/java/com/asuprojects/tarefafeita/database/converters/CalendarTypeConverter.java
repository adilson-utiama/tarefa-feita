package com.asuprojects.tarefafeita.database.converters;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarTypeConverter {

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
            Locale.getDefault());

    @TypeConverter
    public static Calendar toCalendar(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    @TypeConverter
    public static Calendar toCalendar(String data){
        Calendar instance = Calendar.getInstance();
        try {
            //TODO corrigir possivel bug na conversao de datas
            Date date = format.parse(data);
            instance.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return instance;
    }

    @TypeConverter
    public static String toString(Calendar value){
        return format.format(value.getTime());
    }
}
