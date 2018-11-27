package com.asuprojects.tarefafeita.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataFormatterUtil {

    public static final String PATTERN_DATA_BR = "dd/MM/yy";
    public static final String PATTERN_DATA_US = "yyyy/MM/dd";
    public static final String PATTERN_HORA = "HH:mm";
    public static final String PATTERN_DATA_HORA_BR = "dd/MM/yy - HH:mm";
    public static final String PATTERN_DATA_HORA_US = "yyyy/MM/dd - HH:mm";
    public static final String PATTERN_DATA_EXTENSO_BR = "EEEE, dd 'de' MMMM";
    public static final String PATTERN_DATA_EXTENSO_US = "EEEE, MMMM dd";

    private static final String DATA = "DATA";
    private static final String DATAHORA = "DATAHORA";
    private static final String DATA_EXTENSO = "DATA_EXTENSO";

    public static String formatarData(Calendar data){
        return getDateFormatter(DATA).format(data.getTime());
    }

    public static String formataHora(Calendar data){
        return new SimpleDateFormat(PATTERN_HORA).format(data.getTime());
    }

    public static  String formataDataHora(Calendar data){
        return getDateFormatter(DATAHORA).format(data.getTime());
    }

    public static String formataDataExtenso(Calendar data){
        return getDateFormatter(DATA_EXTENSO).format(data.getTime());
    }

    public static String formataHorario(int hora, int minutos){
        String horaS = String.valueOf(hora);
        String minutosS = String.valueOf(minutos);
        if(hora < 10) horaS = "0" + horaS;
        if(minutos < 10) minutosS = "0" + minutos;
        return horaS.concat(":").concat(minutosS);
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
            case DATA_EXTENSO:
                if(isBrasil()){
                    format = new SimpleDateFormat(PATTERN_DATA_EXTENSO_BR);
                }else{
                    format = new SimpleDateFormat(PATTERN_DATA_EXTENSO_US);
                }
                break;
        }

        return format;
    }
}
