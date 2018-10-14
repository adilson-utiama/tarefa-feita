package com.asuprojects.tarefafeita.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataFormatterUtil {

    public static final String PATTERN_DATA_BR = "dd/MM/yyyy";
    public static final String PATTERN_HORA = "HH:mm";
    public static final String PATTERN_DATA_HORA = "dd/MM/yyyy - HH:mm";

    public static String formatarData(Calendar data){
        SimpleDateFormat dataFormat = new SimpleDateFormat(PATTERN_DATA_BR);
        return dataFormat.format(data.getTime());
    }

    public static String formataHora(Calendar data){
        SimpleDateFormat timeFormat = new SimpleDateFormat(PATTERN_HORA);
        return timeFormat.format(data.getTime());
    }

    public static  String formataDataHora(Calendar data){
        SimpleDateFormat dataHoraFormat = new SimpleDateFormat(PATTERN_DATA_HORA);
        return dataHoraFormat.format(data.getTime());
    }
}
