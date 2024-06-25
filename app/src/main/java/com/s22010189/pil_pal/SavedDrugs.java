package com.s22010189.pil_pal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavedDrugs extends AppCompatActivity {
    private LinearLayout linearLayoutSavedDrugs;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_drugs);
        setStatusBarColor();
        //setting layout
        linearLayoutSavedDrugs = findViewById(R.id.linearLayoutSavedDrugs);
        //getting user and loading data
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("savedDrugs");
        loadSavedDrugs();
    }
    private void loadSavedDrugs() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot drugSnapshot : dataSnapshot.getChildren()) {
                    String drugName = drugSnapshot.getKey();
                    addDrugToLayout(drugName);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
    //showing data in layout
    private void addDrugToLayout(String drugName) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View drugCardView = inflater.inflate(R.layout.card_drug, linearLayoutSavedDrugs, false);
        TextView drugTextView = drugCardView.findViewById(R.id.drugNameHere);
        drugTextView.setText(drugName);
        Button deleteButton = drugCardView.findViewById(R.id.deleteButtonHere);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDrug(drugName);
            }
        });
        linearLayoutSavedDrugs.addView(drugCardView);
    }
    private void deleteDrug(String drugName) {
        userRef.child(drugName).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recreate();
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
