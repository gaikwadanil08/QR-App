package com.techvishwa.qrmain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.techvishwa.qrmain.R;
import com.techvishwa.qrmain.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private String strUser, strPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        strUser = sharedPreferences.getString("loginUsername", "");
        strPass = sharedPreferences.getString("loginPassword", "");

        Log.d("User pass", " = "+strUser + "  " +strPass);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    if (strUser.isEmpty() && strPass.isEmpty()) {
                        Thread.sleep(3000);
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    } else {
                        Thread.sleep(3000);
                        startActivity(new Intent(SplashActivity.this, CalculatorActivity.class));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
