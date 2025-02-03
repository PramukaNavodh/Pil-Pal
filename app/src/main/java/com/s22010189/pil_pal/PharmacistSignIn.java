package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//version 1.0002 intent created for switching between screens.
//version 1.0003 created sign up access from firebase real time data base.
//version 1.0004 changed sign up to use fire base authentication.
//version 1.0005 used realtime database and authentication system together.
//version 1.0010 added field validator.
//version 1.0011 added user type validator. Users cannot use to sign in. - current

public class PharmacistSignIn extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private FirebaseAuth auth;
    private Button loginPharmacist, UserDir,signipDir;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacist_signin);
        setStatusBarColor();

        UserDir = findViewById(R.id.lginUser1);
        signipDir = findViewById(R.id.signinDir);

        // Create a link to UserSignIn activity
        UserDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistSignIn.this, UserSignIn.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        // Create a link to the PharmacistSignUp activity
        signipDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistSignIn.this, PharmacistSignUp.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.emailAddress);
        loginPassword = findViewById(R.id.loginPassword);
        loginPharmacist = findViewById(R.id.signInP);
        //action triggers when signin button clicked
        loginPharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                //field validator
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                checkIfUserExists(authResult.getUser().getUid());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PharmacistSignIn.this, "Sign-in Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        loginPassword.setError("Password cannot be empty");
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty");
                } else {
                    loginEmail.setError("Please provide a valid Email");
                }
            }
        });
    }
    //user validator
    private void checkIfUserExists(String userId) {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Pharmacists").child(userId);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(PharmacistSignIn.this, "Sign-in Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PharmacistSignIn.this, DashboardPharmacist.class));
                    finish();
                } else {
                    auth.signOut();
                    Toast.makeText(PharmacistSignIn.this,
                            "Access Denied: This account is not a pharmacist account",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                auth.signOut();
                Toast.makeText(PharmacistSignIn.this, "Database Error: " +
                        error.getMessage(), Toast.LENGTH_SHORT).show();
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

    // Close the app when the back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
