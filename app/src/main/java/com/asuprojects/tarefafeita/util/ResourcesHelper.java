package com.asuprojects.tarefafeita.util;

import android.content.Context;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.domain.enums.Prioridade;

public class ResourcesHelper {

    public static String getTextoPrioridade(Context ctx, Prioridade pri){
        String prioridade = null;
        switch(pri.getCod()){
            case 1:
                prioridade = ctx.getResources().getString(R.string.prioridade_baixa);
                break;
            case 2:
                prioridade = ctx.getResources().getString(R.string.prioridade_media);
                break;
            case 3:
                prioridade = ctx.getResources().getString(R.string.prioridade_alta);
                break;
            case 4:
                prioridade = ctx.getResources().getString(R.string.prioridade_nenhum);
        }
        return prioridade;
    }
}
