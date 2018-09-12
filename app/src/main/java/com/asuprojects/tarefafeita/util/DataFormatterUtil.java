package com.asuprojects.tarefafeita.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataFormatterUtil {

    public static String formatarData(Calendar data){
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dataFormat.format(data.getTime());
    }

    public static String formataHora(Calendar data){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(data.getTime());
    }
}
