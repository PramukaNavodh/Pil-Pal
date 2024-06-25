package com.s22010189.pil_pal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrugDetails extends AppCompatActivity {
    private static final String TAG = "DrugDetails";
    private Button availableSee, shareButton;
    TextView pharmName, pharmDescription, pharmDescriptionB, pharmDescriptionC, pharmPrice, pharmSideEffects, pharmNameB, pharmCounts;
    ImageView pharmImage, drugSave;
    ConstraintLayout constraintLayout;
    DatabaseReference dbRef, userRef;
    ArrayList<String> lngLtdList;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_details);
        setStatusBarColor();
        //Intializing UI components
        pharmName = findViewById(R.id.pharmName);
        pharmDescription = findViewById(R.id.pharmDescription);
        pharmPrice = findViewById(R.id.pharmPrice);
        pharmSideEffects = findViewById(R.id.pharmSideEffects);
        pharmImage = findViewById(R.id.pharmImage);
        pharmDescriptionB = findViewById(R.id.pharmDescriptionB);
        pharmDescriptionC = findViewById(R.id.pharmDescriptionC);
        constraintLayout = findViewById(R.id.constraintLayout);
        pharmNameB = findViewById(R.id.pharmNameB);
        pharmCounts = findViewById(R.id.pharmCount);
        availableSee = findViewById(R.id.seeavlblPharmacies);
        lngLtdList = new ArrayList<>();
        shareButton = findViewById(R.id.shareDrug);
        drugSave = findViewById(R.id.saveStar);
        //getting current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid());

        // Initialize Firebase Realtime Database reference
        dbRef = FirebaseDatabase.getInstance().getReference().child("Pharmacists");
        //retrieve data
        Map<String, Object> drugDetails = (Map<String, Object>) getIntent().getSerializableExtra("drugDetails");

        if (drugDetails != null) {
            pharmName.setText((String) drugDetails.get("name"));
            pharmDescription.setText((String) drugDetails.get("description"));
            pharmDescriptionB.setText((String) drugDetails.get("descriptionb"));
            pharmDescriptionC.setText((String) drugDetails.get("descriptionc"));
            pharmPrice.setText((String) drugDetails.get("price"));
            pharmSideEffects.setText((String) drugDetails.get("side_effect"));
            pharmNameB.setText((String) drugDetails.get("name"));

            String imageUrl = (String) drugDetails.get("image_url");
            Glide.with(this).load(imageUrl).into(pharmImage);

            // Query available pharmacies
            queryAvailablePharmacies((String) drugDetails.get("name"));
        }

        availableSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });
        //Sharing drug details as photos
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrugDetails.this, ShareImage.class);
                intent.putExtra("drugDetails", (HashMap<String, Object>) drugDetails);
                startActivity(intent);
            }
        });

        drugSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDrug((String) drugDetails.get("name"));
            }
        });
    }
    //getting available count from realtime database
    private void queryAvailablePharmacies(String drugName) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> lngLtdList = new ArrayList<>();
                ArrayList<String> storeNameList = new ArrayList<>();
                int availablePharmaciesCount = 0;
                //checking data from sub node
                for (DataSnapshot pharmacistSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot drugDataSnapshot = pharmacistSnapshot.child("drugData").child(drugName);
                    //getting available store locations from realtime database
                    if (drugDataSnapshot.exists()) {
                        int quantity = Integer.parseInt(drugDataSnapshot.getValue().toString());
                        if (quantity >= 1) {
                            availablePharmaciesCount++;
                            String lngLtd = pharmacistSnapshot.child("LngLtd").getValue(String.class);
                            String storeName = pharmacistSnapshot.child("storename").getValue(String.class);
                            if (lngLtd != null && storeName != null) {
                                lngLtdList.add(lngLtd);
                                storeNameList.add(storeName);
                            }
                        }
                    }
                }

                Log.d(TAG, "Available pharmacies count for drug " + drugName + ": " + availablePharmaciesCount);
                pharmCounts.setText(String.valueOf(availablePharmaciesCount));

                // Passing locations to DrugMap
                findViewById(R.id.seeavlblPharmacies).setOnClickListener(v -> {
                    Intent intent = new Intent(DrugDetails.this, DrugMap.class);
                    intent.putStringArrayListExtra("lngLtdList", lngLtdList);
                    intent.putStringArrayListExtra("storeNameList", storeNameList);
                    startActivity(intent);
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Error querying available pharmacies: " + databaseError.getMessage());
            }
        });
    }

    private void openMapActivity() {
        Intent intent = new Intent(DrugDetails.this, DrugMap.class);
        intent.putStringArrayListExtra("lngLtdList", lngLtdList);
        startActivity(intent);
    }

    private void saveDrug(String drugName) {
        userRef.child("savedDrugs").child(drugName).setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                drugSave.setImageResource(R.drawable.hearton);
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
