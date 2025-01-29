package com.s22010189.pil_pal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ResetPassword extends AppCompatActivity {

    private EditText etMobileNumber, etOtp, etNewPassword;
    private Button btnSendOtp, btnVerifyOtp, btnResetPassword;
    private FirebaseAuth auth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);

        auth = FirebaseAuth.getInstance();

        etMobileNumber = findViewById(R.id.etMobileNumber);
        etOtp = findViewById(R.id.etOtp);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnVerifyOtp = findViewById(R.id.btnVerifyOtp);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = etMobileNumber.getText().toString().trim();
                if (TextUtils.isEmpty(mobileNumber)) {
                    etMobileNumber.setError("Mobile number is required");
                    return;
                }
                // Assuming your country code is +1
                String phoneNumber = "+1" + mobileNumber;
                sendOtp(phoneNumber);
            }
        });

        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = etOtp.getText().toString().trim();
                if (TextUtils.isEmpty(otp)) {
                    etOtp.setError("OTP is required");
                    return;
                }
                verifyOtp(otp);
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString().trim();
                if (TextUtils.isEmpty(newPassword)) {
                    etNewPassword.setError("New password is required");
                    return;
                }
                resetPassword(newPassword);
            }
        });
    }

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)        // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    etOtp.setText(phoneAuthCredential.getSmsCode());
                    verifyOtp(phoneAuthCredential.getSmsCode());
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(ResetPassword.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("PasswordResetActivity", "Verification failed", e);
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    super.onCodeSent(s, token);
                    verificationId = s;
                    etOtp.setVisibility(View.VISIBLE);
                    btnVerifyOtp.setVisibility(View.VISIBLE);
                }
            };

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "OTP verified", Toast.LENGTH_SHORT).show();
                            etNewPassword.setVisibility(View.VISIBLE);
                            btnResetPassword.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(ResetPassword.this, "OTP verification failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void resetPassword(String newPassword) {
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPassword.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPassword.this, UserSignIn.class));
                        finish();
                    } else {
                        Toast.makeText(ResetPassword.this, "Password reset failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
