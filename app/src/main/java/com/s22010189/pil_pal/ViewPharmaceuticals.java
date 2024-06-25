package com.s22010189.pil_pal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ViewPharmaceuticals extends AppCompatActivity {

    private LinearLayout pharmaceuticalList;
    private FirebaseFirestore db;
    private String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpharmaceuticals);
        setStatusBarColor();
        //Initializing firebase : used FireStore and Realtime database for different purposes.
        pharmaceuticalList = findViewById(R.id.pharmaceuticalsList);
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //Data should be added to current user's data base. Getting current user from Authentication
        if (currentUser != null){
            String userId = currentUser.getUid();
            FirebaseDatabase.getInstance().getReference("Pharmacists").child(userId).get().
                    addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()){
                        storeName = task.getResult().child("storename").getValue(String.class);
                        loadPharmaceuticals();
                    }
                    else{
                        Toast.makeText(ViewPharmaceuticals.this,
                                "Failed to retrieve your data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //loadPharmaceutical method is using to get pharmaceutical data from Firestore.
    private void loadPharmaceuticals(){
        if (storeName != null){
            CollectionReference storeCollection = db.collection(storeName);
            storeCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot document :task.getResult()){
                            Log.d("ViewPharmaceuticals", "Document: " + document.getData());
                            displayPharmaceutical(document);
                        }
                    }
                    else {
                        Toast.makeText(ViewPharmaceuticals.this,
                                "Error getting your information", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //displayPharmaceutical is uses for display loaded details
    //bug fixed for not loading the screen 04.06
    private void displayPharmaceutical(DocumentSnapshot document){
        View pharmaceuticalView = LayoutInflater.from(this).inflate(R.layout
                .pharmaceutical_item,pharmaceuticalList,false);
        TextView nameTextView = pharmaceuticalView.findViewById(R.id.nameTextView);
        TextView doseTextView = pharmaceuticalView.findViewById(R.id.doseTextView);
        TextView quantityTextView = pharmaceuticalView.findViewById(R.id.quantityTextView);
        Button editButton = pharmaceuticalView.findViewById(R.id.editButton);
        Button deleteButton = pharmaceuticalView.findViewById(R.id.deleteButton);

        nameTextView.setText(document.getString("drugName"));
        doseTextView.setText(document.getString("dose"));
        Object quantityObject = document.get("quantity");
        String quantityString;
        //converting quantity because of number cannot display
        if (quantityObject instanceof Number) {
            quantityString = String.valueOf(((Number) quantityObject).longValue());
        } else if (quantityObject instanceof String) {
            quantityString = (String) quantityObject;
        } else {
            quantityString = "N/A";
        }
        quantityTextView.setText(quantityString);
        editButton.setOnClickListener(v -> showEditDialog(document));
        deleteButton.setOnClickListener(v -> showDeleteDialog(document));
        pharmaceuticalList.addView(pharmaceuticalView);
    }
    //showing update window
    private void showEditDialog(DocumentSnapshot document){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_pharmaceutical_dialog,null);
        builder.setView(dialogView);
        //getting user input
        EditText doseEditText = dialogView.findViewById(R.id.doseEditText);
        EditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);
        doseEditText.setText(document.getString("dose"));
        Object quantityObject = document.get("quantity");
        String quantityString;
        //bug resolved. Supported Mr. Frank Van Puffelen and chatGpt.
        if (quantityObject instanceof Number) {
            quantityString = String.valueOf(((Number) quantityObject).longValue());
        } else if (quantityObject instanceof String) {
            quantityString = (String) quantityObject;
        } else {
            quantityString = "0"; // Fallback if quantity is not a Number or String
        }
        quantityEditText.setText(quantityString);
        //getting new user inputs
        builder.setTitle(document.getString("drugName"));
        builder.setPositiveButton("Update",(dialog,which)->{
            String newDose = doseEditText.getText().toString();
            String newQuantity = quantityEditText.getText().toString();
            updatePharmaceutical(document.getId(),newDose, newQuantity, document.getString
                    ("drugName"), quantityString);
        });
        builder.setNegativeButton("Cancel",(dialog,which)-> dialog.dismiss());
        builder.create().show();
    }
    // updating new uer inputs in Firestore and realtime
    private void updatePharmaceutical(String documentId, String newDose, String newQuantity,
                                      String drugName, String oldQuantity) {
        if (storeName != null){
            CollectionReference storeCollection = db.collection(storeName);
            Map<String, Object> updates = new HashMap<>();
            updates.put("dose", newDose);
            updates.put("quantity", newQuantity);
            storeCollection.document(documentId).update(updates).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Successfully updated.", Toast.LENGTH_SHORT).show();
                updateRealtimeDatabase(drugName, oldQuantity, newQuantity);
            }).addOnFailureListener(e -> Toast.makeText(this,
                    "Error caught while updating document", Toast.LENGTH_SHORT).show());
        }
    }
    //showDelteDialog is for deleting override.
    private void showDeleteDialog(DocumentSnapshot document){
        new AlertDialog.Builder(this).setTitle("Delete Pharmaceutical")
                .setMessage("Do you want to delete the pharmaceutical?")
                .setPositiveButton("Yes",(dialog, which) -> deletePharmaceutical(document
                        .getId(), document.getString("drugName")))
                .setNegativeButton("No",((dialog, which) -> dialog.dismiss())).create().show();
    }
    //deleting user deleted pharmaceutical from databases.
    private void deletePharmaceutical (String documentId, String drugName){
        if (storeName != null){
            CollectionReference storeCollection = db.collection(storeName);
            storeCollection.document(documentId).delete().addOnSuccessListener(aVoid ->{
                Toast.makeText(this, "Pharmaceutical Deleted", Toast.LENGTH_SHORT).show();
                removeDrugFromRealtimeDatabase(drugName);
                reloadPharmaceuticals();
            }).addOnFailureListener(e -> Toast.makeText(this,
                    "Error caught deleting pharmaceutical. Please contact the administrator.",
                    Toast.LENGTH_SHORT).show());
        }
    }
    //after deleting an item reloadPharmaceutical reloads the screen
    private void reloadPharmaceuticals(){
        pharmaceuticalList.removeAllViews();
        loadPharmaceuticals();
    }
    //updateRealtimeDatabase is for update new quantity in realtime databse
    //bug reolved. instead of updating the same field because it's making errors now I deleting pre
    //vious one and adding new field.
    private void updateRealtimeDatabase(String drugName, String oldQuantity, String newQuantity) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference
                    ("Pharmacists").child(userId).child("drugData");

            // Remove the old quantity entry
            userRef.child(drugName).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Add the new quantity entry
                    userRef.child(drugName).setValue(newQuantity).addOnCompleteListener(newTask -> {
                        if (newTask.isSuccessful()) {
                            Toast.makeText(ViewPharmaceuticals.this,
                                    "Drug data updated in Realtime Database",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ViewPharmaceuticals.this,
                                    "Failed to update drug data in Realtime Database",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ViewPharmaceuticals.this,
                            "Failed to remove old drug data from Realtime Database",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void removeDrugFromRealtimeDatabase(String drugName) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference
                    ("Pharmacists").child(userId).child("drugData");

            userRef.child(drugName).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ViewPharmaceuticals.this,
                            "Drug data removed from Realtime Database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ViewPharmaceuticals.this,
                            "Failed to remove drug data from Realtime Database",
                            Toast.LENGTH_SHORT).show();
                }
            });
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
//Don't ever think to touch this code. all bugs now resolved.