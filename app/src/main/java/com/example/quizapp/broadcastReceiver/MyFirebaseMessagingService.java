package com.example.quizapp.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.quizapp.Common.Common;
import com.example.quizapp.MainActivity;
import com.example.quizapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onNewToken(String s) {
        Log.d("new token",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        handleNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
    }

    private void handleNotification(String body,String Title)
    {
//        Intent pushNotification=new Intent(Common.STR_PUSH);
//        pushNotification.putExtra("message",body);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
//                PendingIntent.FLAG_ONE_SHOT);
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,0);



//                Intent ni=new Intent(getApplicationContext(),MainActivity.class);
//                ni.setAction(Intent.ACTION_MAIN);
//                ni.addCategory(Intent.CATEGORY_LAUNCHER);
//                ni.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,
//                new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP),
//                PendingIntent.FLAG_CANCEL_CURRENT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_stat_q)
                        //.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round))
                       .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ico23))
                        .setContentTitle(Title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(contentIntent);

        Notification n=notificationBuilder.build();
        //n.flags |=Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }
}
