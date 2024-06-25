package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class DashboardPharmacist extends AppCompatActivity {
    private AutoCompleteTextView searchBar;
    private Button searchButton;
    private TextView nameText;
    ImageView profile;
    private CardView addDrugButton,viewDrugButton;
    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    FirebaseFirestore db;

    private static final String[] DRUGS = new String[] {
            "Aspirin", "Amitone", "Aero Spacer Plus", "Asthafen Tab", "Beclohale","Beclomate",
            "Beclovent","Belarmin Expectorant Syrup","Brilinta","Cetrine","Disprin","Flusal 250hfa Inhaler",
            "Flutihale", "Flutimate","Flutimate 50mcg Nasal Spray", "Flutivent","Foracort 100mcg Inhaler",
            "Halothane", "Montiget","Plavix","Foracort Dp Caps","Rupa"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacist_dashboard);
        setStatusBarColor();

        // Initialize UI components
        searchBar = findViewById(R.id.drugSearchP);
        searchButton = findViewById(R.id.drugButton);
        addDrugButton = findViewById(R.id.addDataButton);
        viewDrugButton = findViewById(R.id.viewDrugsButton);
        nameText = findViewById(R.id.textName);
        profile = findViewById(R.id.pharmacistProfile);

        // Set up the adapter for the AutoCompleteTextView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, DRUGS);
        searchBar.setAdapter(adapter);

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDrug = searchBar.getText().toString().trim();
                if (selectedDrug.isEmpty()){
                    Toast.makeText(DashboardPharmacist.this, "Please enter a pharmaceutical name", Toast.LENGTH_SHORT).show();
                    return;
                }
                fetchDrugDetails(selectedDrug);
            }
        });
        //Creating a link to the addpharmaceuticals activity
        addDrugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPharmacist.this, AddPharmaceuticals.class);
                startActivity(intent);
            }
        });
        //Creating a link to the ViewPharmaceuticals activity
        viewDrugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPharmacist.this, ViewPharmaceuticals.class);
                startActivity(intent);
            }
        });
        // Creating a link to the ProfileViewPharmacist activity
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardPharmacist.this, ProfileViewPharmacist.class);
                startActivity(intent);
            }
        });

        // Get the current logged-in user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            String userId = currentUser.getUid();

            // Fetch data for the current user
            dbRef.child("Pharmacists").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String storeOwnerName = snapshot.child("storeowner").getValue(String.class);
                        nameText.setText(storeOwnerName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
    //fetching drug details after onclick from Firestore and publishing on DrugDetails activity
    private void fetchDrugDetails(String drugName) {
        db.collection("pharmaceuticals").whereEqualTo("name", drugName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> drugDetails = document.getData();
                    Intent intent = new Intent(DashboardPharmacist.this, DrugDetails.class);
                    intent.putExtra("drugDetails", (HashMap<String, Object>) drugDetails);
                    startActivity(intent);
                    return;
                }
            } else {
                Toast.makeText(this, "Pharmaceutical is not in database", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Sign Out confirmation.
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
                        dialog.dismiss();
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
    }
}
