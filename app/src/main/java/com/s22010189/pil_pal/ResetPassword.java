package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ResetPassword extends AppCompatActivity {

    private ImageView Back;
    private Button verifyButton;
    private EditText emailVerifier;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        Back = findViewById(R.id.backInReset);
        verifyButton = findViewById(R.id.emailVerifierButton);
        emailVerifier = findViewById(R.id.emailVerifier);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResetPassword.this, UserSignIn.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(intent);
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmail();
            }
        });
        setStatusBarColor();
    }
    private void verifyEmail(){
        String enteredEmail = emailVerifier.getText().toString().trim();

        if(enteredEmail.isEmpty()){
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean emailExists = false;
                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    String storedEmail = userSnapshot.child("email").getValue(String.class);
                    if(storedEmail !=null && storedEmail.equals(enteredEmail)){
                        emailExists = true;
                        break;
                    }
                }
                if(emailExists){
                    Intent intent = new Intent(ResetPassword.this, ResetPasswordOTP.class);
                    intent.putExtra("email",enteredEmail);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(ResetPassword.this, "Email not registered. Please check again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResetPassword.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //status bar color
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