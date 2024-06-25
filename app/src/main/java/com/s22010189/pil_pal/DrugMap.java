package com.s22010189.pil_pal;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrugMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    private TextView disapearText;
    private static final String TAG = "DrugMap";
    DatabaseReference dbRef;
    private String phoneNumber; // Add this variable to store the phone number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drugmap);
        setStatusBarColor();

        disapearText = findViewById(R.id.disText);

        ArrayList<String> lngLtdList = getIntent().getStringArrayListExtra("lngLtdList");
        ArrayList<String> storeNameList = getIntent().getStringArrayListExtra("storeNameList");
        if (lngLtdList != null && storeNameList != null) {
            for (int i = 0; i < lngLtdList.size(); i++) {
                Log.d(TAG, "LngLtd: " + lngLtdList.get(i) + ", StoreName: " + storeNameList.get(i));
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e(TAG, "SupportMapFragment is null!");
        }

        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f,0.0f);
        alphaAnim.setStartOffset(5000);
        alphaAnim.setDuration(400);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                disapearText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        disapearText.setAnimation(alphaAnim);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        ArrayList<String> lngLtdList = getIntent().getStringArrayListExtra("lngLtdList");
        ArrayList<String> storeNameList = getIntent().getStringArrayListExtra("storeNameList");

        if (lngLtdList != null && storeNameList != null) {
            for (int i = 0; i < lngLtdList.size(); i++) {
                String lngLtd = lngLtdList.get(i);
                String storeName = storeNameList.get(i);

                String[] parts = lngLtd.split(",");
                double lat = Double.parseDouble(parts[0]);
                double lng = Double.parseDouble(parts[1]);
                LatLng pharmacyLocation = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(pharmacyLocation).title(storeName));
            }
            if (!lngLtdList.isEmpty()) {
                String[] firstLocation = lngLtdList.get(0).split(",");
                double firstLat = Double.parseDouble(firstLocation[0]);
                double firstLng = Double.parseDouble(firstLocation[1]);
                LatLng firstPharmacyLocation = new LatLng(firstLat, firstLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPharmacyLocation, 10));
            }
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String storeName = marker.getTitle();
        Log.d(TAG, "Marker clicked: " + storeName);
        fetchPharmacyData(storeName);
        return true;
    }

    private void fetchPharmacyData(String storeName) {
        dbRef = FirebaseDatabase.getInstance().getReference("Pharmacists");
        Log.d(TAG, "Fetching data for store: " + storeName);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot pharmacistSnapshot : snapshot.getChildren()) {
                    String storedStoreName = pharmacistSnapshot.child("storename").getValue(String.class);
                    Log.d(TAG, "Checking store name: " + storedStoreName);
                    if (storedStoreName != null && storedStoreName.trim().equalsIgnoreCase(storeName.trim())) {
                        String timeFrom = pharmacistSnapshot.child("timeFrom").getValue(String.class);
                        String timeTo = pharmacistSnapshot.child("timeTo").getValue(String.class);
                        String mobileNumber = pharmacistSnapshot.child("pharmacistmobilenumber").getValue(String.class);

                        Log.d(TAG, "Data fetched for store: " + storeName);
                        phoneNumber = mobileNumber; // Save the mobile number
                        showBottomSheetDialog(storeName, timeFrom, timeTo, mobileNumber);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Log.e(TAG, "Store not found in database: " + storeName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    private void showBottomSheetDialog(String storeName, String timeFrom, String timeTo, String mNumber) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DrugMap.this);
        bottomSheetDialog.setContentView(R.layout.bottompanel);

        TextView storeNameTextView = bottomSheetDialog.findViewById(R.id.namePharma);
        TextView timeFromTextView = bottomSheetDialog.findViewById(R.id.fromTime);
        TextView timeToTextView = bottomSheetDialog.findViewById(R.id.toTime);
        TextView numberTextView = bottomSheetDialog.findViewById(R.id.numberMobile);
        ImageView callPharmacistButton = bottomSheetDialog.findViewById(R.id.callPharmacist);

        if (storeNameTextView != null) storeNameTextView.setText(storeName);
        if (timeFromTextView != null) timeFromTextView.setText(timeFrom);
        if (timeToTextView != null) timeToTextView.setText(timeTo);
        if (numberTextView != null) numberTextView.setText(mNumber);

        callPharmacistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Phone number is null or empty");
                }
            }
        });

        Log.d(TAG, "Showing bottom sheet dialog for store: " + storeName);
        bottomSheetDialog.show();
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
