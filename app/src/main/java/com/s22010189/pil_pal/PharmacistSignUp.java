package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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

public class PharmacistSignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText storeName, pharmacistMobileNumber, storeOwner, pharmacistEmail, pharmacistPassword, rePassword;
    private Button pharmacistSignup, userDirection, pharmSignin;
    private CheckBox checkBox;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private Spinner countyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacist_signup);
        setStatusBarColor();

        // Initializing Firebase and other elements
        auth = FirebaseAuth.getInstance();
        storeName = findViewById(R.id.storeName);
        pharmacistMobileNumber = findViewById(R.id.pMobileNumber);
        storeOwner = findViewById(R.id.ownerName);
        pharmacistEmail = findViewById(R.id.pEmail);
        pharmacistPassword = findViewById(R.id.pPassword);
        pharmacistSignup = findViewById(R.id.pSignup);
        userDirection = findViewById(R.id.directUser);
        pharmSignin = findViewById(R.id.signInPhar);
        rePassword = findViewById(R.id.repPassword);
        checkBox = findViewById(R.id.checkBox1);
        countyCode = findViewById(R.id.countryCodeP);

        ArrayAdapter<CharSequence> adapterp = ArrayAdapter.createFromResource(this,
                R.array.countrycode_array, android.R.layout.simple_spinner_item);
        adapterp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countyCode.setAdapter(adapterp);

        pharmacistSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sname = storeName.getText().toString().trim();
                String pnumber = pharmacistMobileNumber.getText().toString().trim();
                String ownname = storeOwner.getText().toString().trim();
                String pemail = pharmacistEmail.getText().toString().trim();
                String epassword = pharmacistPassword.getText().toString().trim();
                String erepassword = rePassword.getText().toString().trim();

                // Get the selected country code
                String countryCode = countyCode.getSelectedItem().toString().trim();
                // Combine country code and mobile number
                String fullMobileNumber = countryCode + pnumber;

                // Validating fields
                if (sname.isEmpty()) {
                    storeName.setError("Store Name cannot be Empty");
                } else if (pnumber.isEmpty()) {
                    pharmacistMobileNumber.setError("Mobile Number cannot be empty");
                } else if (ownname.isEmpty()) {
                    storeOwner.setError("Owner Name cannot be empty");
                } else if (pemail.isEmpty()) {
                    pharmacistEmail.setError("Email cannot be empty");
                } else if (epassword.isEmpty()) {
                    pharmacistPassword.setError("Password cannot be empty");
                } else if (!epassword.equals(erepassword)) {
                    rePassword.setError("Passwords do not match");
                } else if (!checkBox.isChecked()) {
                    Toast.makeText(PharmacistSignUp.this, "You have to agree with policies to signup", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(sname, fullMobileNumber, ownname, pemail, epassword);
                }
            }
        });

        // Creating a link to the UserSignUp activity
        userDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistSignUp.this, UserSignUp.class);
                startActivity(intent);
                finish();
            }
        });

        // Creating a link to the PharmacistSignIn activity
        pharmSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmacistSignUp.this, PharmacistSignIn.class);
                startActivity(intent);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPolicyDialog();
            }
        });
    }

    // Method to show the policy dialog
    private void showPolicyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.policydialog, null);
        builder.setView(dialogView);

        Button agreeButton = dialogView.findViewById(R.id.aagreeButton);
        final AlertDialog dialog = builder.create();

        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(true);
                dialog.dismiss();
            }
        });

        dialog.show();

        // Set the size of the dialog
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // 90% of screen width
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(width, height);
    }

    // Register user method to get user inputs and store in the database
    private void registerUser(String sname, String fullMobileNumber, String ownname, String pemail, String epassword) {
        auth.createUserWithEmailAndPassword(pemail, epassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = auth.getCurrentUser().getUid();
                    database = FirebaseDatabase.getInstance();
                    dbRef = database.getReference("Pharmacists").child(userId);

                    SupportPharmacist supportPharmacist = new SupportPharmacist(sname, fullMobileNumber, ownname, pemail, epassword);
                    dbRef.setValue(supportPharmacist).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(PharmacistSignUp.this, "User Account created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(PharmacistSignUp.this, PharmacistSignUpSupport.class));
                                finish();
                            } else {
                                Toast.makeText(PharmacistSignUp.this, "Failed to store user data: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(PharmacistSignUp.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Status bar color
    private void setStatusBarColor() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int statusBarColor = ContextCompat.getColor(this, R.color.statusbar);
            window.setStatusBarColor(statusBarColor);

            // Fixing all get white issue
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
