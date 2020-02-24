package com.techvishwa.qrmain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.techvishwa.qrmain.R;
import com.techvishwa.qrmain.utils.Constants;

import java.io.ByteArrayOutputStream;

public class StaticQRActivity extends AppCompatActivity {

    private EditText name, upiId;
    private String strName, strUpiId, URL;
    ImageView QRcode, back;
    private String appId = "com.infrasofttech.mahaupi";
    private Button generate;
    Bitmap image;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_qr);

        init();

        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQR();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        name = findViewById(R.id.name);
        upiId = findViewById(R.id.upiId);
        QRcode = findViewById(R.id.imageView);
        back = findViewById(R.id.ivBack);
        generate = findViewById(R.id.generate);
    }

    public void generateQR() {
        strName = name.getText().toString().trim();
        strUpiId = upiId.getText().toString().trim();

        if (!strUpiId.isEmpty()) {

            Uri uri = Uri.parse("upi://pay?").buildUpon()
                    .appendQueryParameter("appid", appId)
                    .appendQueryParameter("tr", "")
                    .appendQueryParameter("mc", "")
                    .appendQueryParameter("pa", strUpiId)
                    .appendQueryParameter("pn", strName)
                    .appendQueryParameter("tn", "")
                    .appendQueryParameter("am", "")
                    .appendQueryParameter("cu", "INR")
                    .build();

            Log.d("Upi Id", "URL = " + uri);

            final QRCodeWriter writer = new QRCodeWriter();

            URL = getUPIString(strUpiId, strName);

            BitMatrix bitMatrix = null;
            try {
                bitMatrix = writer.encode(URL, BarcodeFormat.QR_CODE, 540, 480);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            QRcode.setImageBitmap(bmp);
            image = bmp;
            String text = BitMapToString(bmp);
            Log.d("Bitmap image", "Bitmap image =  " + text);

            editor.putString("staticQr", text);
            editor.putString("staticQrUrl", uri.toString());
            editor.apply();

            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Please Enter UPI ID", Toast.LENGTH_LONG).show();
        }
    }

    private String getUPIString(String payeeAddress,
                                String payeeName) {

        String UPI = "upi://pay?" +
                "pa=" + payeeAddress +
                "&pn=" + payeeName;

        return UPI.replace(" ", "+");
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}

