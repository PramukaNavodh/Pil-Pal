package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileViewPharmacist extends AppCompatActivity {
    private static final String TAG = "ProfileViewPharmacist";
    TextView storeName, ownerName, emailAddress, mobileNumber, location, password;
    Button editButton, deleteButton;
    DatabaseReference dbRef;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileviewpharmacist);
        setStatusBarColor();
        //initializing UI components
        storeName = findViewById(R.id.profileStoreNameText);
        ownerName = findViewById(R.id.profileOwnerName);
        emailAddress = findViewById(R.id.emailAddressTextP);
        mobileNumber = findViewById(R.id.PharmacistMobileNumberHere);
        location = findViewById(R.id.loctionText);
        password = findViewById(R.id.pharmacistPasswordProfile);
        editButton = findViewById(R.id.editProfileButtonP);
        deleteButton = findViewById(R.id.deleteProfileButtonP);
        //getting current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //getting data from realtime database
        if(currentUser != null){
            String uid = currentUser.getUid();
            dbRef = FirebaseDatabase.getInstance().getReference().child("Pharmacists").child(uid);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String stname = snapshot.child("storename").getValue(String.class);
                        String oname = snapshot.child("storeowner").getValue(String.class);
                        String emails = snapshot.child("pharmacistemail").getValue(String.class);
                        String mnumbers = snapshot.child("pharmacistmobilenumber").getValue(String.class);
                        String locations = snapshot.child("LngLtd").getValue(String.class);
                        String pwords = snapshot.child("pharmacistpassword").getValue(String.class);
                        //showing data
                        if (stname != null) storeName.setText(stname);
                        if (oname != null) ownerName.setText(oname);
                        if (emails != null) emailAddress.setText(emails);
                        if (mnumbers != null) mobileNumber.setText(mnumbers);
                        if (locations != null) location.setText(locations);
                        if (pwords != null) password.setText(pwords);
                    } else {
                        Log.d(TAG, "User data does not exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Failed to read user data", error.toException());
                    Toast.makeText(ProfileViewPharmacist.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not authenticated.", Toast.LENGTH_SHORT).show();
        }

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