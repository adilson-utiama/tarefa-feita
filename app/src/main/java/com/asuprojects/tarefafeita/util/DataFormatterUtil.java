package com.asuprojects.tarefafeita.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataFormatterUtil {

    public static final String PATTERN_DATA_BR = "dd/MM/yyyy";
    public static final String PATTERN_DATA_US = "yyyy/MM/dd";
    public static final String PATTERN_HORA = "HH:mm";
    public static final String PATTERN_DATA_HORA_BR = "dd/MM/yyyy - HH:mm";
    public static final String PATTERN_DATA_HORA_US = "yyyy/MM/dd - HH:mm";

    private static final String DATA = "DATA";
    private static final String DATAHORA = "DATAHORA";

    public static String formatarData(Calendar data){
        return getDateFormatter(DATA).format(data.getTime());
    }

    public static String formataHora(Calendar data){
        return new SimpleDateFormat(PATTERN_HORA).format(data.getTime());
    }

    public static  String formataDataHora(Calendar data){
        if(data != null){
            Log.i("CALENDAR", "formataDataHora: " + data);
        }else{
            Log.i("CALENDAR", "formataDataHora: Calendar esta NULO");
        }
        return getDateFormatter(DATAHORA).format(data.getTime());
    }

    private static boolean isBrasil(){
        String country = Locale.getDefault().getCountry();
        return country.contentEquals("BR");
    }

    private static SimpleDateFormat getDateFormatter(String tipo){
        SimpleDateFormat format = null;
        switch(tipo){
            case DATA:
                if(isBrasil()){
                    format = new SimpleDateFormat(PATTERN_DATA_BR);
                }else{
                    format = new SimpleDateFormat(PATTERN_DATA_US);
                }
                break;
            case DATAHORA:
                if(isBrasil()){
                    format = new SimpleDateFormat(PATTERN_DATA_HORA_BR);
                }else{
                    format = new SimpleDateFormat(PATTERN_DATA_HORA_US);
                }
                break;
        }

        return format;
    }
}
