package com.s22010189.pil_pal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImportantNumbers extends AppCompatActivity {
    ImageView callIDH, callJapura, callNawaloka, callStJohn, callNational, callApeksha, callSuwaseriya,
            callHealthLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importantnumbers);
        setStatusBarColor();

        callIDH = findViewById(R.id.idhcallButton);
        callJapura =  findViewById(R.id.jayawadenepurahospitalcallButton);
        callNawaloka = findViewById(R.id.nawalokacallButton);
        callStJohn = findViewById(R.id.stJohncallButton);
        callNational = findViewById(R.id.nationalhospitalcallButton);
        callApeksha = findViewById(R.id.apekshacallButton);
        callSuwaseriya = findViewById(R.id.suwasariyacallButton);
        callHealthLine = findViewById(R.id.healthLineCallButton);

        callSuwaseriya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("1990");
            }
        });

        callHealthLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("1999");
            }
        });

        callApeksha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("+94112850252");
            }
        });

        callNational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("+94112691111");
            }
        });

        callStJohn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("+94777805944");
            }
        });

        callNawaloka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("+94115577111");
            }
        });

        callJapura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("+94112778610");
            }
        });

        callIDH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall("+94112411224");
            }
        });
    }

    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
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
