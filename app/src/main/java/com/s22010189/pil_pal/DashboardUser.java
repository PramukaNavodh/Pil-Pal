package com.s22010189.pil_pal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class DashboardUser extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    Button searchButton;
    CardView mapDir,pilTracker,stepCount,imNumbers,savedDrugs;
    AutoCompleteTextView textView;
    FirebaseFirestore db;
    ImageView profileUser;
    private static final String[] DRUGS = new String[] {
            "Aspirin", "Amitone", "Aero Spacer Plus", "Asthafen Tab", "Beclohale","Beclomate",
            "Beclovent","Belarmin Expectorant Syrup","Brilinta","Cetrine","Disprin","Flusal 250hfa Inhaler",
            "Flutihale", "Flutimate","Flutimate 50mcg Nasal Spray", "Flutivent","Foracort 100mcg Inhaler",
            "Halothane", "Montiget","Plavix","Foracort Dp Caps","Rupa"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_user);
        setStatusBarColor();
        //Setting up Autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, DRUGS);
        textView = findViewById(R.id.drugSearch);
        textView.setAdapter(adapter);
        //Setting Firestore
        db = FirebaseFirestore.getInstance();

        searchButton = findViewById(R.id.searchButtonD);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDrug = textView.getText().toString().trim();
                if (selectedDrug.isEmpty()) {
                    Toast.makeText(DashboardUser.this, "Please enter a pharmaceutical name", Toast.LENGTH_SHORT).show();
                    return;
                }
                fetchDrugDetails(selectedDrug);
            }
        });

        // Creating a link to the MapPage activity
        mapDir = findViewById(R.id.mapButton);
        mapDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUser.this, MapPage.class);
                startActivity(intent);
            }
        });
        // Creating a link to the ProfileViewUser activity
        profileUser = findViewById(R.id.userProfile);
        profileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUser.this, ProfileViewUser.class);
                startActivity(intent);
            }
        });

        // Creating a link to the PilTracker activity
        pilTracker = findViewById(R.id.drugButton);
        pilTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUser.this, PilTracker.class);
                startActivity(intent);
            }
        });

        // Creating a link to the StepCounter activity
        stepCount = findViewById(R.id.stepCounterOpt);
        stepCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUser.this, StepCounter.class);
                startActivity(intent);
            }
        });
        // Creating a link to the ImportantNumber activity
        imNumbers = findViewById(R.id.importantNumbers);
        imNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUser.this, ImportantNumbers.class);
                startActivity(intent);
            }
        });
        // Creating a link to the SavedDrugs activity
        savedDrugs = findViewById(R.id.savedDrugButton);
        savedDrugs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardUser.this, SavedDrugs.class);
                startActivity(intent);
            }
        });
    }
    //fetching pharmaceutical data from firestore and directing to DrugDetails
    private void fetchDrugDetails(String drugName) {
        db.collection("pharmaceuticals").whereEqualTo("name", drugName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> drugDetails = document.getData();
                    Intent intent = new Intent(DashboardUser.this, DrugDetails.class);
                    intent.putExtra("drugDetails", (HashMap<String, Object>) drugDetails);
                    startActivity(intent);
                    return;
                }
            } else {
                Toast.makeText(this, "Pharmaceutical is not in database", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Sign out confirmation
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.CustomAlertDialog)
                .setMessage("Do you want to sign-out?")
                .setTitle("Sign-Out")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Close the dialog and stay on the current screen
                    }
                })
                .setCancelable(true)
                .create()
                .show();
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
    }}
