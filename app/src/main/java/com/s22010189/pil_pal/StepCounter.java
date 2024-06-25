package com.s22010189.pil_pal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepCounter extends AppCompatActivity implements SensorEventListener, StepListener {
    private static final String TAG = "StepCounter";
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView stepCountTextView;
    private CurvedProgressBar curvedProgressBar;
    private CardView walkingTips;
    private static final int STEP_GOAL = 6000;
    static final String CHANNEL_ID = "step_goal_channel";
    private static final String PREFS_NAME = "StepCounterPrefs";
    private static final String LAST_RESET_DATE_KEY = "last_reset_date";
    private SharedPreferences sharedPreferences;
    private int stepCount = 0;

    private StepDetector stepDetector;
    //calculation for 24hours in milliseconds
    private static final long RESET_INTERVAL_MS = 24 * 60 * 60 * 1000;
    private long lastResetTimeMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        setStatusBarColor();

        stepCountTextView = findViewById(R.id.stepCountTextView);
        curvedProgressBar = findViewById(R.id.curvedProgressBar);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        walkingTips = findViewById(R.id.tipsWalk);


        // Loading step count from SharedPreferences
        stepCount = sharedPreferences.getInt("stepCount", 0);
        //checking availability of sensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else {
            Log.e(TAG, "SensorManager is null.");
            Toast.makeText(this, "SensorManager is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        stepDetector = new StepDetector();
        stepDetector.registerListener(this);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            Log.d(TAG, "Accelerometer sensor registered.");
        } else {
            Log.e(TAG, "Accelerometer sensor is not available.");
            Toast.makeText(this, "Accelerometer sensor is not available", Toast.LENGTH_SHORT).show();
        }

        createNotificationChannel();

        // Update UI with loaded step count
        updateUI(stepCount);

        walkingTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open YouTube link
                String youtubeLink = "https://www.youtube.com/watch?v=AdSQaIh94ME";
                Uri uri = Uri.parse(youtubeLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void checkAndResetStepCount() {
        String lastResetDate = sharedPreferences.getString(LAST_RESET_DATE_KEY, "");
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (!todayDate.equals(lastResetDate)) {
            sharedPreferences.edit().putString(LAST_RESET_DATE_KEY, todayDate).apply();
            stepCount = 0; // Reset step count to 0
            updateUI(stepCount);
        }

        // Load last reset time
        lastResetTimeMillis = sharedPreferences.getLong("lastResetTimeMillis", 0);
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastResetTimeMillis >= RESET_INTERVAL_MS) {
            // Reset step count if 24 hours passed since last reset
            resetStepCount();
        }
    }

    private void resetStepCount() {
        stepCount = 0;
        lastResetTimeMillis = System.currentTimeMillis();
        updateUI(stepCount);
        saveStepCount();
    }

    private void saveStepCount() {
        sharedPreferences.edit()
                .putInt("stepCount", stepCount)
                .putLong("lastResetTimeMillis", lastResetTimeMillis)
                .apply();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "Accelerometer data received: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);
            stepDetector.updateAccel(event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onStep() {
        stepCount++;
        updateUI(stepCount);
        Log.d(TAG, "Step detected. Step count: " + stepCount);
    }

    private void updateUI(int stepCount) {
        stepCountTextView.setText(String.valueOf(stepCount));
        curvedProgressBar.setProgress((float) stepCount / STEP_GOAL * 100);

        if (stepCount >= STEP_GOAL) {
            sendGoalReachedNotification();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private void sendGoalReachedNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Goal Reached")
                .setContentText("Congratulations! You have reached your step goal.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent resultIntent = new Intent(this, StepCounter.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Step Goal Channel";
            String description = "Channel for step goal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);

        // Save step count when activity is destroyed
        saveStepCount();
    }
    //statusbar color
    private void setStatusBarColor() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int statusBarColor = ContextCompat.getColor(this, R.color.statusbar);
            window.setStatusBarColor(statusBarColor);

            //fixing all get white issue.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (isColorDark(statusBarColor)) {
                    window.getDecorView().setSystemUiVisibility(0); // Light text
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Dark text
                }
            }
        }
    }
    private boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}
