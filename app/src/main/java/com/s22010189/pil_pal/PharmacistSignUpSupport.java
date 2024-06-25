package com.s22010189.pil_pal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log; // Import Log class
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PharmacistSignUpSupport extends AppCompatActivity {

    private EditText licenseId, expiryDate, lngLtd;
    private Spinner spinnerTimeFrom, spinnerTimeTo;
    private Button infoSubmitter;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacistsignupsupport);
        setStatusBarColor();

        // Initializing Firebase
        database = FirebaseDatabase.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = database.getReference("Pharmacists").child(userId);

        licenseId = findViewById(R.id.licenseId);
        expiryDate = findViewById(R.id.expiryDate);
        lngLtd = findViewById(R.id.LngLtd);
        spinnerTimeFrom = findViewById(R.id.timeFrom);
        spinnerTimeTo = findViewById(R.id.timeTo);
        infoSubmitter = findViewById(R.id.infoSubmitter);

        // Setting up the spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeFrom.setAdapter(adapter);
        spinnerTimeTo.setAdapter(adapter);

        infoSubmitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Debugging statements
                String timeFromData = spinnerTimeFrom.getSelectedItem().toString().trim();
                String timeToData = spinnerTimeTo.getSelectedItem().toString().trim();
                Log.d("Spinner", "Time From: " + timeFromData + " Time To: " + timeToData);

                submitAdditionalDetails();
            }
        });
    }

    private void submitAdditionalDetails() {
        String licenseIdData = licenseId.getText().toString().trim();
        String expiryDateData = expiryDate.getText().toString().trim();
        String timeFromData = spinnerTimeFrom.getSelectedItem().toString().trim();
        String timeToData = spinnerTimeTo.getSelectedItem().toString().trim();
        String locData = lngLtd.getText().toString().trim();

        // Validate fields
        if (licenseIdData.isEmpty()) {
            licenseId.setError("License ID is required");
            licenseId.requestFocus();
            return;
        }

        if (expiryDateData.isEmpty()) {
            expiryDate.setError("Expiry Date is required");
            expiryDate.requestFocus();
            return;
        }

        if (timeFromData.isEmpty()) {
            Toast.makeText(this, "Please select a start time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (timeToData.isEmpty()) {
            Toast.makeText(this, "Please select an end time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (locData.isEmpty()) {
            lngLtd.setError("Location is required");
            lngLtd.requestFocus();
            return;
        }

        // If all fields are valid, proceed to update database and navigate to DashboardPharmacist
        Map<String, Object> additionalDetails = new HashMap<>();
        additionalDetails.put("licenseId", licenseIdData);
        additionalDetails.put("expiryDate", expiryDateData);
        additionalDetails.put("timeFrom", timeFromData);
        additionalDetails.put("timeTo", timeToData);
        additionalDetails.put("LngLtd", locData);

        dbRef.updateChildren(additionalDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Details Added", Toast.LENGTH_SHORT).show();
                // Navigate to DashboardPharmacist activity
                startActivity(new Intent(PharmacistSignUpSupport.this, DashboardPharmacist.class));
                finish(); // Optional: Close current activity
            } else {
                Toast.makeText(this, "Something went wrong. Details not updated", Toast.LENGTH_SHORT).show();
            }
        });
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
