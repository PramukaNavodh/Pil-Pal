package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

public class ProfileViewUser extends AppCompatActivity {
    private static final String TAG = "ProfileViewUser";
    TextView firstNameTexts, secondNameTexts, emailAddressTexts, mobileNumberTexts, userPasswordProfile;
    Button editProfile, deleteProfile;
    ImageView profilePicture;
    DatabaseReference dbRef;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileviewuser);
        setStatusBarColor();
        //Initialzing UI components
        firstNameTexts = findViewById(R.id.firstNameText);
        secondNameTexts = findViewById(R.id.secondNameText);
        emailAddressTexts = findViewById(R.id.emailAddressText);
        mobileNumberTexts = findViewById(R.id.mobileNumberText);
        userPasswordProfile = findViewById(R.id.userPasswordProfile);
        editProfile = findViewById(R.id.editProfileButton);
        deleteProfile = findViewById(R.id.deleteProfileButton);
        profilePicture = findViewById(R.id.profilePicture);
        //getting current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        //getting data from realtime database
        if(currentUser != null){
            String uid = currentUser.getUid();
            dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String fname = snapshot.child("firstname").getValue(String.class);
                        String sname = snapshot.child("lastname").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String mnumber = snapshot.child("mobilenumber").getValue(String.class);
                        String pword = snapshot.child("password").getValue(String.class);
                        //showing data
                        if (fname != null) firstNameTexts.setText(fname);
                        if (sname != null) secondNameTexts.setText(sname);
                        if (email != null) emailAddressTexts.setText(email);
                        if (mnumber != null) mobileNumberTexts.setText(mnumber);
                        if (pword != null) userPasswordProfile.setText(pword);
                    } else {
                        Log.d(TAG, "User data does not exist");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Failed to read user data", error.toException());
                    Toast.makeText(ProfileViewUser.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
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
