package com.asuprojects.tarefafeita.broadcastreceiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.asuprojects.tarefafeita.R;
import com.asuprojects.tarefafeita.activity.DetalhesActivity;
import com.asuprojects.tarefafeita.domain.Tarefa;

public class AlarmReceiver extends BroadcastReceiver {

    private Tarefa tarefa = new Tarefa();

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("tarefa")){
            tarefa = (Tarefa) intent.getSerializableExtra("tarefa");
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "tarefa-id")
                        .setSmallIcon(R.drawable.ic_aviso)
                        .setContentTitle("Tarefa a ser realizada!")
                        .setContentText(tarefa.getTitulo())
                        .setSubText("Clique para mais detalhes")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVibrate(new long[]{ 100,250,100,500 })
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(context, DetalhesActivity.class);
        resultIntent.putExtra("tarefa_detalhe", tarefa);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, resultIntent, 0);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

        Notification notification = mBuilder.build();

        notificationManager.notify(android.R.drawable.ic_menu_camera, notification);
    }
}
