package com.asuprojects.tarefafeita.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.activity.DetalhesActivity;
import com.asuprojects.tarefafeita.domain.Tarefa;
import com.asuprojects.tarefafeita.util.ByteArrayHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

    public static final int FEED_NUMBER = 2000;
    public static final String TAREFA_ALARM = "tarefa";
    public static final String TAREFA_NOTIFICACAO = "tarefa_notificacao";

    private Tarefa tarefa;

    private final int notifyId = 100;
    private final String CHANNEL_ID = "channelIdTarefa";
    private final String CHANNEL_NAME = "channelTarefaFeita";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("NOTIFICACAO", "onReceive: inicio");
        if(intent.hasExtra(TAREFA_ALARM)){
            Log.i("NOTIFICACAO", "onReceive: Verificando intent TAREFA_ALARM");
            byte[] data = intent.getByteArrayExtra(TAREFA_ALARM);
            tarefa = (Tarefa) ByteArrayHelper.toObject(data);
        }
        if(tarefa != null){
            Log.i("NOTIFICACAO", "onReceive: Tarefa NAO esta NULO");
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_aviso)
                    .setContentTitle(context.getString(R.string.tarefa_a_realizar))
                    .setContentText(tarefa.getTitulo())
                    .setSubText(context.getString(R.string.notificacao_subtexto))
                    .setAutoCancel(true);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel nChannel =
                        new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                nChannel.enableLights(true);
                nChannel.setLightColor(Color.RED);
                nChannel.setShowBadge(true);
                nChannel.enableVibration(true);
                mBuilder.setChannelId(CHANNEL_ID);

                if(notificationManager != null){
                    notificationManager.createNotificationChannel(nChannel);
                }
            } else {
                mBuilder.setDefaults(Notification.DEFAULT_SOUND |
                                        Notification.DEFAULT_LIGHTS |
                                        Notification.DEFAULT_VIBRATE);
            }

//            int nextInt = geraNumeroAleatorio();

            Intent resultIntent = new Intent(context, DetalhesActivity.class);
            resultIntent.putExtra(TAREFA_NOTIFICACAO, tarefa);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);

//            PendingIntent resultPendingIntent =
//                    PendingIntent.getActivity(context, nextInt, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            mBuilder.setContentIntent(resultPendingIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(notifyId, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(pendingIntent);

            if(notificationManager != null){
                notificationManager.notify(notifyId, mBuilder.build());
            }
        } else {
            Log.i("NOTIFICACAO", "onReceive: TArefa ESTA NULO");
        }
    }

    private int geraNumeroAleatorio() {
        Random random = new Random();
        return random.nextInt(FEED_NUMBER);
    }


}
