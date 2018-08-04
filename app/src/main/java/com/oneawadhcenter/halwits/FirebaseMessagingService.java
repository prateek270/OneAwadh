package com.oneawadhcenter.halwits;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.oneawadhcenter.halwits.R;
import com.google.firebase.messaging.RemoteMessage;
 public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

     String title,msg,subtitle;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("msg is ",remoteMessage.getData().toString());
        msg=remoteMessage.getData().get("message");
        title=remoteMessage.getData().get("title");
        subtitle=remoteMessage.getData().get("subtitle");

        showNotification(msg,title);
    }

    private void showNotification(String message,String title) {
        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
        int value=sp.getInt("Login",0);
        Intent i;
        if(value==1) {


            if (subtitle.equalsIgnoreCase("movie")) {
                i = new Intent(this, movies.class);

            } else if (subtitle.equalsIgnoreCase("Event")) {
                i = new Intent(this, MainActivity.class);
            } else {
                i = new Intent(this, Promotions.class);

            }
        }
        else
        {
            i=new Intent(this,login.class);
        }


        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.logo)
                .setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }


}