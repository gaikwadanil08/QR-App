package com.techvishwa.qrmain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.techvishwa.qrmain.R;
import com.techvishwa.qrmain.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText username, password;
    private String strUsername, strPassword;
    private String strUser, strPass;

    private Button login;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        strUser = sharedPreferences.getString("username", "");
        strPass = sharedPreferences.getString("password", "");
        Log.d("User Pass", " =  " + strUser + "  " + strPass);

        editor = sharedPreferences.edit();

        username = findViewById(R.id.et_userName);
        password = findViewById(R.id.et_password);

        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strUsername = username.getText().toString().trim();
                strPassword = password.getText().toString().trim();

                if (strUsername.isEmpty() || strPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields Can't be Empty", Toast.LENGTH_LONG).show();
                } else if (strUsername.equals(strUser) && strPassword.equals(strPass)) {
                    editor.putString("loginUsername", strUsername);
                    editor.putString("loginPassword", strPassword);
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}