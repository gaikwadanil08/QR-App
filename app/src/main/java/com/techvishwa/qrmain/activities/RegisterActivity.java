package com.techvishwa.qrmain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.techvishwa.qrmain.R;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, bankName, upiId, username, password;
    private String strName, strBankName, strUpiId, strUsername, strPassword;
    private Button register;
    private ImageView back;

    public static String PREF_NAME = "RegisterDetails";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
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
        name = findViewById(R.id.etName);
        bankName = findViewById(R.id.etBankName);
        upiId = findViewById(R.id.etUpiId);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);

        back = findViewById(R.id.ivBack);
        register = findViewById(R.id.register);
    }

    public void registerUser() {
        strName = name.getText().toString().trim();
        strBankName = bankName.getText().toString().trim();
        strUpiId = upiId.getText().toString().trim();
        strUsername = username.getText().toString().trim();
        strPassword = password.getText().toString().trim();

        if (strName.isEmpty()) {
            name.requestFocus();
            Toast.makeText(getApplicationContext(), "Please Enter Your Name", Toast.LENGTH_LONG).show();
        } else if (strBankName.isEmpty()) {
            bankName.requestFocus();
            Toast.makeText(getApplicationContext(), "Please Enter Your Bank Name", Toast.LENGTH_LONG).show();
        } else if (strUpiId.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Upi Id", Toast.LENGTH_LONG).show();
            upiId.requestFocus();

            if (isValidUpiId(strUpiId)) {
                Toast.makeText(getApplicationContext(), "Valid UPI", Toast.LENGTH_LONG).show();
            } else {
                upiId.requestFocus();
                Toast.makeText(getApplicationContext(), "InValid UPI", Toast.LENGTH_LONG).show();
            }
        } else if (strUsername.isEmpty()) {
            username.requestFocus();
            Toast.makeText(getApplicationContext(), "Please Enter Your UserName", Toast.LENGTH_LONG).show();
        } else if (strPassword.isEmpty()) {
            password.requestFocus();
            Toast.makeText(getApplicationContext(), "Please Enter Your Password", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

            editor.putString("name", strName);
            editor.putString("bankName", strBankName);
            editor.putString("upiId", strUpiId);
            editor.putString("username", strUsername);
            editor.putString("password", strPassword);

            editor.apply();

            startActivity(intent);
        }
    }

    private boolean isValidUpiId(String upi) {
        String upiID = "/[a-zA-Z0-9_]{3,}@[a-zA-Z]{3,}/";

        return Pattern.compile(upiID).matcher(upi).matches();
    }
}

