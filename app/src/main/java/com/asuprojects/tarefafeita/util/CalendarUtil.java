package com.asuprojects.tarefafeita.util;

import java.util.Calendar;

public class CalendarUtil {

    public static Calendar buildCalendarFrom(String data, String horario) {
        Calendar calendar = Calendar.getInstance();
        int dia, mes, ano, hora, minuto = 0;
        String[] arrayData = data.split("/");
        String[] arrayHorario = horario.split(":");
        if(data.matches("\\d{4}\\/\\d{1,2}\\/\\d{1,2}")){
            ano = Integer.parseInt(arrayData[0]);
            mes = Integer.parseInt(arrayData[1]);
            dia = Integer.parseInt(arrayData[2]);
            hora = Integer.parseInt(arrayHorario[0]);
            minuto = Integer.parseInt(arrayHorario[1]);
            calendar.set(ano, mes - 1, dia, hora, minuto);
        }
        if(data.matches("\\d{1,2}\\/\\d{1,2}\\/\\d{4}")){
            dia = Integer.parseInt(arrayData[0]);
            mes = Integer.parseInt(arrayData[1]);
            ano = Integer.parseInt(arrayData[2]);
            hora = Integer.parseInt(arrayHorario[0]);
            minuto = Integer.parseInt(arrayHorario[1]);
            calendar.set(ano, mes - 1, dia, hora, minuto);
        }
        return calendar;
    }
}
