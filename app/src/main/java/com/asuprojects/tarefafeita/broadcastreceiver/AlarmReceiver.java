package com.asuprojects.tarefafeita.broadcastreceiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.activity.DetalhesActivity;
import com.asuprojects.tarefafeita.domain.Tarefa;

import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

    public static final int FEED_NUMBER = 2000;
    private NotificationManagerCompat notificationManager;

    private Tarefa tarefa = new Tarefa();

    private final int notifyId = 1;
    private final String CHANNEL_ID = "channelIdTarefa";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("tarefa")){
            tarefa = (Tarefa) intent.getSerializableExtra("tarefa");
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_aviso)
                        .setContentTitle(context.getString(R.string.tarefa_a_realizar))
                        .setContentText(tarefa.getTitulo())
                        .setSubText("Clique para mais detalhes")
                        .setVibrate(new long[]{ 100,250,100,500 })
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(context, DetalhesActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        resultIntent.putExtra("tarefa_notificacao", tarefa);

        Random random = new Random();
        int nextInt = random.nextInt(FEED_NUMBER);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, nextInt, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager = NotificationManagerCompat.from(context);

        Notification notification = mBuilder.build();

        notificationManager.notify(notifyId, notification);
    }


}
