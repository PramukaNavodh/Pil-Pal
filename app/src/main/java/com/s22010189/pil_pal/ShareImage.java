package com.s22010189.pil_pal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ShareImage extends AppCompatActivity {
    TextView drugName, descriptionX, descriptionY, descriptionZ;
    ImageView imageShare;
    private Button cancelShare, buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareimage);
        setStatusBarColor();
        //Initializing UI Components
        drugName = findViewById(R.id.shareDrugName);
        descriptionX = findViewById(R.id.sharePharmDescription);
        descriptionY = findViewById(R.id.sharePharmDescriptionB);
        descriptionZ = findViewById(R.id.sharePharmDescriptionC);
        imageShare = findViewById(R.id.sharePharmImage);
        cancelShare = findViewById(R.id.shareCancel);
        buttonShare = findViewById(R.id.shareButton);
        //retreiving
        Map<String, Object> drugDetails = (Map<String, Object>) getIntent().getSerializableExtra("drugDetails");

        if (drugDetails != null) {
            drugName.setText((String) drugDetails.get("name"));
            descriptionX.setText((String) drugDetails.get("description"));
            descriptionY.setText((String) drugDetails.get("descriptionb"));
            descriptionZ.setText((String) drugDetails.get("descriptionc"));

            String imageUrl = (String) drugDetails.get("image_url");
            Glide.with(this).load(imageUrl).into(imageShare);
        }

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = captureLayoutAsBitmap();
                    File file = saveBitmapToFile(bitmap);
                    shareImage(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ShareImage.this, "Error sharing image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Correcting the link to the MapPage activity
        cancelShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareImage.this, MapPage.class);
                startActivity(intent);
            }
        });
    }

    private Bitmap captureLayoutAsBitmap() {
        View layout = findViewById(R.id.shareLayout);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        return bitmap;
    }

    private File saveBitmapToFile(Bitmap bitmap) throws IOException {
        File cacheDir = getCacheDir();
        File file = new File(cacheDir, "share_image.png");
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
        return file;
    }

    private void shareImage(File file) {
        Uri uri = FileProvider.getUriForFile(this, "com.s22010189.pil_pal.fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share Image"));
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
