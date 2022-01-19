package com.example.thermometer2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "temperature_notification";
    boolean buttonPressed = false;
    private TextView tv1;
    private Button bt1;
    private ScheduledFuture<?> runningTask;

    GetThermometerValueAlt ConnectionThread = new GetThermometerValueAlt("http://192.168.0.120/");
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        tv1 = findViewById(R.id.textView);
        bt1 = findViewById(R.id.buttonAlt);

        if (savedInstanceState != null) {
            Log.i("Main", "Instance recreated");
            tv1.setText(savedInstanceState.getString("textViewText"));
//            if (savedInstanceState.getBoolean("buttonPressed")) {
//                Log.d("MainActivity", "Button pressed");
//                boolean buttonPressed = true;
//                bt1.setText(R.string.refreshButtonStop);
//            }
        }

//      Log.d("MainActivity", "Hello World");
//      .d debug, e for Error, w for Warn, and i for Info. writes into logcat
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        if (buttonPressed) {
            savedState.putBoolean("buttonPressed", true);
        }
        savedState.putString("textViewText",tv1.getText().toString());
    }

    public void RefreshTextView(TextView tv, String value) {
        tv.setText(value);
    }

//    public void checkTemperatureForNotification() {
//        if (Integer.parseInt(ConnectionThread.readStringHtml()) >= 50 && !messageSend) {
//            messageSend = true;
//            Log.d("MainActivity", "Message sent, flag is true");
//        }
//        if (Integer.parseInt(ConnectionThread.readStringHtml()) < 50 && messageSend) {
//            messageSend = false;
//            Log.d("MainActivity", "Flag is false");
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        runningTask.cancel(true);
    }

    public void startRefreshingButton(View view) {
        if (buttonPressed) {
            buttonPressed = false;
            bt1.setText(R.string.refreshButtonStart);
            runningTask.cancel(true);
            ConnectionThread = new GetThermometerValueAlt("http://192.168.0.120/");
        } else {
            buttonPressed = true;
            bt1.setText(R.string.refreshButtonStop);
            runningTask = scheduledExecutorService.scheduleAtFixedRate(new ExecutorAction(tv1, ConnectionThread, this), 0, 5, TimeUnit.SECONDS);
        }
    }

    public void sendNotificationButton(View view) {
        Log.d("Notification button", "Notification button was pressed");
        try {
//            createNotification("Температура", "Температура печки  " + tv1.getText() + " градусов", 1212);
            staticCreateNotification("Температура", "Температура печки  " + tv1.getText() + " градусов", 1212, CHANNEL_ID, this);
        } catch (Exception e) {
            Log.e("Notification button", e.toString());
        }


    }

    public void createNotification(String textTitle, String textContent, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

    public static void staticCreateNotification(String textTitle, String textContent, int notificationId, String CHANNEL_ID, MainActivity activity) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}