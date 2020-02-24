package com.techvishwa.qrmain.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techvishwa.qrmain.R;
import com.techvishwa.qrmain.pojo.PojoSave;
import com.techvishwa.qrmain.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private TextView UPI_ID, staticQr, viewQr;
    private RelativeLayout relCalcAmt;
    private CircleImageView profile_image;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String upiId;
    private ImageView qr, logout, closeQr;
    private Switch staticSwitch;
    private String strQR;
    private int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    Bitmap bitmap;
    LinearLayout layout;
    private String staticQrUrl;
    private WebView webView;
    int cnt;
    int count = 0;

    DatabaseReference databaseReference;
    List<PojoSave> pojoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        databaseReference = FirebaseDatabase.getInstance().getReference("data");
        pojoList = new ArrayList<>();

        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        upiId = sharedPreferences.getString("upiId", "");
        UPI_ID.setText(upiId);

        staticQrUrl = sharedPreferences.getString("staticQrUrl", "");
        Log.d("Static Url ", " = "+staticQrUrl);

        cnt = sharedPreferences.getInt("count", 0);
        Log.d("Count pref = ", " == " + cnt);

        relCalcAmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalculatorActivity.class));
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        staticQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StaticQRActivity.class));
            }
        });

        viewQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewQr();
            }
        });

        closeQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeQr();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        staticSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCheckedTrue();
                } else {
                    isCheckedFalse();
                }
            }
        });

        getAmount();

        if (count > cnt) {
            cnt = count;
            Toast.makeText(getApplicationContext(), "Response Generated", Toast.LENGTH_LONG).show();

            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

            String data = "";
            data = "http://192.168.43.214:80/" + "Done_333";
            //webView.loadUrl(data);

            Log.d("Url", "Url  " + data);
        }
    }

    private void init() {
        relCalcAmt = findViewById(R.id.relCalcAmt);
        UPI_ID = findViewById(R.id.tvUPI_ID);
        profile_image = findViewById(R.id.profile_image);
        staticQr = findViewById(R.id.staticQr);
        viewQr = findViewById(R.id.viewQr);
        closeQr = findViewById(R.id.ivQRClose);
        staticSwitch = findViewById(R.id.staticSwitch);
        qr = findViewById(R.id.ivQr);
        layout = findViewById(R.id.layout);
        logout = findViewById(R.id.logout);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAmount();
        if (count > cnt) {
            cnt = count;
            Toast.makeText(getApplicationContext(), "Response Generated", Toast.LENGTH_LONG).show();

            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

            String data = "";
            data = "http://192.168.43.214:80/" + "Done_333";
            //webView.loadUrl(data);

            Log.d("Url", "Url  " + data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAmount();
        if (count > cnt) {
            cnt = count;
            Toast.makeText(getApplicationContext(), "Response Generated", Toast.LENGTH_LONG).show();
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

            String data = "";
            data = "http://192.168.43.214:80/" + "Done_333";
            //webView.loadUrl(data);

            Log.d("Url", "Url  " + data);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getAmount();
        if (count > cnt) {
            cnt = count;
            Toast.makeText(getApplicationContext(), "Response Generated", Toast.LENGTH_LONG).show();
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

            String data = "";
            data = "http://192.168.43.214:80/" + "Done_333";
            //webView.loadUrl(data);

            Log.d("Url", "Url  " + data);
        }
    }

    public void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //Home is name of the activity
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities

                SharedPreferences settings = getApplicationContext().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
                settings.edit().clear().apply();

                startActivity(i);
                finish();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void viewQr() {
        strQR = sharedPreferences.getString("staticQr", "");
        Log.d("String", "String = " + strQR);

        bitmap = StringToBitMap(strQR);
        qr.setImageBitmap(bitmap);
        qr.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        closeQr.setVisibility(View.VISIBLE);
    }

    public void closeQr() {
        qr.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        closeQr.setVisibility(View.GONE);
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profile_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void isCheckedTrue() {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        String data = "";
        data = "http://192.168.43.214:80/" + staticQrUrl;
        webView.loadUrl(data);

        Log.d("Url", "Url  " + data);
    }

    public void isCheckedFalse() {
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void getAmount() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pojoList.clear();

                for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                    PojoSave pojoSave = listSnapshot.getValue(PojoSave.class);

                    count++;
                }
                Log.d("Count", " =1234= " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}



