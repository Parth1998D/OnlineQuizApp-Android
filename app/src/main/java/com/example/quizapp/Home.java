package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.quizapp.Common.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //BroadcastReceiver mRegistrationBroadcastReceiver;

//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }

//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,new IntentFilter("registration complete"));
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,new IntentFilter(Common.STR_PUSH));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //registrationNotification();

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            String frag="category";
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                Fragment selectedFragment=null;
                switch (menuItem.getItemId())
                {
                    case R.id.action_category:
                        if(frag.equals("ranking"))
                        {
                            frag="category";
                            selectedFragment = CategoryFragment.newInstance();
                            bottomNavigationView.setBackgroundColor(Color.parseColor("#2d2f3c"));
                            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout,selectedFragment);
                            transaction.commit();
                        }
                        break;
                    case R.id.action_ranking:
                        if(frag.equals("category"))
                        {
                            frag="ranking";
                            selectedFragment = RankingFragment.newInstance();
                            bottomNavigationView.setBackgroundColor(Color.parseColor("#822d2f3c"));
                            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame_layout,selectedFragment);
                            transaction.commit();
                        }
                }
//                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_layout,selectedFragment);
//                transaction.commit();
                return true;
            }
//            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        });
        setDefaultFragment();
    }

//    private void registrationNotification()
//    {
//        mRegistrationBroadcastReceiver=new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(intent.getAction().equals(Common.STR_PUSH))
//                {
//                    String message=intent.getStringExtra("message");
//                    showNotification("efwf",message);
//                }
//            }
//        };
//    }

//    private void showNotification(String title, String message) {
//        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//        PendingIntent contentIntent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
//        builder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setContentIntent(contentIntent);
//        NotificationManager notificationManager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(new Random().nextInt(),builder.build());
//    }

    private void setDefaultFragment()
    {
      FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
      transaction.replace(R.id.frame_layout,CategoryFragment.newInstance());
      transaction.commit();
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder b;
        b = new AlertDialog.Builder(Home.this);
        b.setMessage("Are you sure you want to exit?")
                .setPositiveButton("exit", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface di, int i)
                        {
                           finish();
                        }
                    })
                .setNegativeButton("cancel",null);
        AlertDialog ad=b.create();
        ad.show();
    }
}
