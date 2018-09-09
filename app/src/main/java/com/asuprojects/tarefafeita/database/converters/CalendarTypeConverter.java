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

    @TypeConverter
    public static Calendar toCalendar(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    @TypeConverter
    public static Calendar toCalendar(long data){
        Date date = new Date();
        date.setTime(data);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance;
    }

    @TypeConverter
    public static long toLong(Calendar value){
        return value.getTime().getTime();
    }
}
