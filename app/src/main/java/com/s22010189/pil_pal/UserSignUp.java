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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText firstName, lastName, mobileNumber, emailAddress, passWord, reUPassword;
    private Button signupButton, pharmacistDirection, userSignin;
    private Spinner countryCode;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);
        setStatusBarColor();

        auth = FirebaseAuth.getInstance();
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        mobileNumber = findViewById(R.id.mobileNumber);
        emailAddress = findViewById(R.id.emailAddress);
        passWord = findViewById(R.id.passWord);
        signupButton = findViewById(R.id.signUp);
        pharmacistDirection = findViewById(R.id.directPharmacist);
        userSignin = findViewById(R.id.signiDirec);
        reUPassword = findViewById(R.id.userRePassword);
        countryCode = findViewById(R.id.countryCodeU);

        // Set up the country code spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countrycode_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryCode.setAdapter(adapter);

        // Handle sign-up button click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = firstName.getText().toString().trim();
                String lname = lastName.getText().toString().trim();
                String mnumber = mobileNumber.getText().toString().trim();
                String email = emailAddress.getText().toString().trim();
                String password = passWord.getText().toString().trim();
                String confirm = reUPassword.getText().toString().trim();

                // Validate fields
                if (fname.isEmpty()) {
                    firstName.setError("First name cannot be empty");
                } else if (lname.isEmpty()) {
                    lastName.setError("Last name cannot be empty");
                } else if (mnumber.isEmpty()) {
                    mobileNumber.setError("Mobile number cannot be empty");
                } else if (email.isEmpty()) {
                    emailAddress.setError("Email cannot be empty");
                } else if (password.isEmpty()) {
                    passWord.setError("Password cannot be empty");
                } else if (!password.equals(confirm)) {
                    reUPassword.setError("Passwords do not match");
                } else {
                    registerUser(fname, lname, mnumber, email, password);
                }
            }
        });

        // Navigate to PharmacistSignUp
        pharmacistDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSignUp.this, PharmacistSignUp.class);
                startActivity(intent);
                finish();
            }
        });

        // Navigate to UserSignIn
        userSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSignUp.this, UserSignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Register user with Firebase
    private void registerUser(String fname, String lname, String mnumber, String email, String password) {
        // Get the selected country code from the spinner
        String selectedCountryCode = countryCode.getSelectedItem().toString();
        // Combine country code and mobile number
        String fullMobileNumber = selectedCountryCode + mnumber;

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = auth.getCurrentUser().getUid();
                    database = FirebaseDatabase.getInstance();
                    dbRef = database.getReference("Users").child(userId);

                    Support support = new Support(fname, lname, email, fullMobileNumber, password);
                    dbRef.setValue(support).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserSignUp.this, "User Account created", Toast.LENGTH_SHORT)
                                        .show();
                                startActivity(new Intent(UserSignUp.this, DashboardUser.class));
                                finish();
                            } else {
                                Toast.makeText(UserSignUp.this, "Failed to store user data: " + task
                                        .getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(UserSignUp.this, "Signup Failed: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Set status bar color
    private void setStatusBarColor() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int statusBarColor = ContextCompat.getColor(this, R.color.statusbar);
            window.setStatusBarColor(statusBarColor);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (isColorDark(statusBarColor)) {
                    window.getDecorView().setSystemUiVisibility(0); // Light text
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Dark text
                }
            }
        }
    }

    // Check if color is dark
    private boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }

    // Handle back press
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
