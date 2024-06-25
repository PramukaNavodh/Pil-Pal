package com.s22010189.pil_pal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class PilTracker extends AppCompatActivity {
    PilTrackerBackup myDb;
    EditText editID, editTitle, editDoze;
    Button addButton, viewButton, updateButton, deleteButton;
    Spinner timeMinutes, timeHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pil_tracker);
        setStatusBarColor();

        myDb = new PilTrackerBackup(this);
        editID = findViewById(R.id.drugID);
        editTitle = findViewById(R.id.drugName);
        editDoze = findViewById(R.id.drugDoze);
        addButton = findViewById(R.id.enterDrug);
        viewButton = findViewById(R.id.viewData);
        updateButton = findViewById(R.id.updateData);
        deleteButton = findViewById(R.id.deleteData);
        timeMinutes = findViewById(R.id.timeMinute);
        timeHours = findViewById(R.id.timeHour);

        ArrayAdapter<CharSequence> adapterm = ArrayAdapter.createFromResource(this,
                R.array.hour_array, android.R.layout.simple_spinner_item);
        adapterm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeHours.setAdapter(adapterm);

        ArrayAdapter<CharSequence> adaptern = ArrayAdapter.createFromResource(this,
                R.array.minute_array, android.R.layout.simple_spinner_item);
        adaptern.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeMinutes.setAdapter(adaptern);

        insertData();
        viewAll();
        updateData();
        deleteData();
    }

    // Implementing to add data
    public void insertData() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dname = editTitle.getText().toString().trim();
                String ddoze = editDoze.getText().toString().trim();
                String dhour = timeHours.getSelectedItem().toString().trim();
                String dminute = timeMinutes.getSelectedItem().toString().trim();
                if (dname.isEmpty()) {
                    editTitle.setError("Pharmaceutical Name Cannot be empty");
                } else if (ddoze.isEmpty()) {
                    editDoze.setError("The dose cannot be empty");
                } else {
                    Log.d("PilTracker", "Inserting data: " + dname + ", " + ddoze + ", " + dhour + ", " + dminute);
                    boolean isDataInserted = myDb.insertData(dname, ddoze, dhour, dminute);

                    if (isDataInserted) {
                        Toast.makeText(PilTracker.this, "Pharmaceutical Added to Schedule", Toast.LENGTH_LONG).show();
                        // Schedule notification for the selected time
                        scheduleNotification(Integer.parseInt(dhour), Integer.parseInt(dminute));
                    } else {
                        Toast.makeText(PilTracker.this, "No Data Added. Please Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void scheduleNotification(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        Intent notificationIntent = new Intent(PilTracker.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                PilTracker.this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
            Log.d("PilTracker", "Alarm set for: " + calendar.getTime());
        } else {
            Log.e("PilTracker", "AlarmManager is null");
        }
    }


    // Implementing view all data
    public void viewAll() {
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing Found in Database");
                    return;
                }

                StringBuilder buffer = new StringBuilder();
                while (res.moveToNext()) {
                    buffer.append("Id :").append(res.getString(0)).append("\n");
                    buffer.append("Drug Name :").append(res.getString(1)).append("\n");
                    buffer.append("Drug Dose :").append(res.getString(2)).append("\n");
                    buffer.append("Hour :").append(res.getString(3)).append("\n");
                    buffer.append("Minute :").append(res.getString(4)).append("\n\n");
                }

                showMessage("Pharmaceutical Schedule", buffer.toString());
            }
        });
    }

    // Implementing updating data
    public void updateData() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isUpdate = myDb.updateData(editID.getText().toString(),
                        editTitle.getText().toString(),
                        editDoze.getText().toString(),
                        timeHours.getSelectedItem().toString(),
                        timeMinutes.getSelectedItem().toString());

                if (isUpdate) {
                    Toast.makeText(PilTracker.this, "Data Update Successful", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PilTracker.this, "Data Update Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Implementing deleting data
    public void deleteData() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer deletedRows = myDb.deleteData(editID.getText().toString());
                if (deletedRows > 0) {
                    Toast.makeText(PilTracker.this, "Data Deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PilTracker.this, "Data Not Deleted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Method to show message
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
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
