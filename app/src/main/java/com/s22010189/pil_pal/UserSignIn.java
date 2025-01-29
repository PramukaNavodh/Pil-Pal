package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
//version 1.0011 added user type validator. Pharmacists cannot use to sign in. - current

public class UserSignIn extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private Button signinButton, pharmDir,signInDirI;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signin);

        createNotificationChannel();
        setStatusBarColor();

        pharmDir = findViewById(R.id.lginPharm);
        signInDirI = findViewById(R.id.signInDirI);

        //creating a link to the PharmacistSignIn activity
        pharmDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSignIn.this, PharmacistSignIn.class);
                startActivity(intent);
            }
        });
        //creating a link to the UserSignUp activity
        signInDirI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSignIn.this, UserSignUp.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.emailAddress);
        loginPassword = findViewById(R.id.lognPassword);
        signinButton = findViewById(R.id.signInUser);
        //action triggers when signin button clicked
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                //field validator
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener
                                (new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                checkIfUserExists(authResult.getUser().getUid());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserSignIn.this, "Sign-in Failed: " + e.
                                        getMessage(), Toast.LENGTH_SHORT).show();
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
        dbRef = database.getReference("Users").child(userId);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(UserSignIn.this, "Sign-in Successful", Toast
                            .LENGTH_SHORT).show();
                    startActivity(new Intent(UserSignIn.this, DashboardUser.class));
//                    finish();
                } else {
                    auth.signOut();
                    Toast.makeText(UserSignIn.this, "Access Denied: This account is " +
                            "not a user account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                auth.signOut();
                Toast.makeText(UserSignIn.this, "Database Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MedicationReminderChannel";
            String description = "Channel for medication reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
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
    // at this screen user press back: the app closes
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finishAffinity();
    }
}
