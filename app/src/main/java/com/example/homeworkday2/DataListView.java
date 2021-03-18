package com.example.homeworkday2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataListView extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ListView dataView;
    ArrayList dataArray;
    ArrayAdapter dataAdapter;
    static int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list_view);

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        dataView = findViewById(R.id.data_view);
        dataArray = new ArrayList();

        dataArray.add("Name: "+sharedPreferences.getString("name", ""));
        dataArray.add("Date of Birth: "+sharedPreferences.getString("dob", ""));
        dataArray.add("Gender: "+sharedPreferences.getString("gender", ""));
        dataArray.add("Blood Group: "+sharedPreferences.getString("blood_group", ""));
        dataArray.add("Address: "+sharedPreferences.getString("address", ""));
        dataArray.add("Mobile: "+sharedPreferences.getString("mobile", ""));
        dataArray.add("Website: "+sharedPreferences.getString("website", ""));

        dataAdapter = new ArrayAdapter(DataListView.this, android.R.layout.simple_list_item_1, dataArray);
        dataView.setAdapter(dataAdapter);

        dataView.setOnItemClickListener((parent, view, position, id) -> {
            SmsManager sender = SmsManager.getDefault();
            String message = "You've got some royal blood! " + sharedPreferences.getString("blood_group", "");
            sender.sendTextMessage(sharedPreferences.getString("mobile", ""),
                    null, message, null, null);

            send_notification("Family Tree Notice", message);

            Toast.makeText(DataListView.this, message, Toast.LENGTH_LONG).show();
        });

        ActivityCompat.requestPermissions(DataListView.this,
                new String[]{Manifest.permission.SEND_SMS},
                1);
    }

    public void send_notification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) DataListView.this.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(DataListView.this, channelId)
                .setSmallIcon(android.R.drawable.ic_media_play).setContentTitle(title).setContentText(message);

        Intent intent = new Intent(DataListView.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(DataListView.this, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(notificationId++, mBuilder.build());
    }
}