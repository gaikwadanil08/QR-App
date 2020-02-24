package com.techvishwa.qrmain.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.techvishwa.qrmain.R;
import com.techvishwa.qrmain.pojo.PojoSave;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorActivity extends AppCompatActivity {

    Button buttonViewQR;
    private ImageView back;

    private String amount;
    private String strName, strUpiId;
    public static String PREF_NAME = "RegisterDetails";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ImageView home;

    private String URL;
    ImageView QRcode, ivQRClose;
    private String appId = "com.infrasofttech.mahaupi";
    private Button generate;
    private WebView webView;
    Bitmap image;
    LinearLayout layout;
    int cnt;
    int count = 0;

    List<PojoSave> pojoList;

    DatabaseReference databaseReference;

    //*********************************************************************************
    // IDs of all the numeric buttons
    private int[] numericButtons = {R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour, R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine};
    // IDs of all the operator buttons
    private int[] operatorButtons = {R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide};
    // TextView used to display the output
    private TextView txtScreen;
    // Represent whether the lastly pressed key is numeric or not
    private boolean lastNumeric;
    // Represent that current state is in error or not
    private boolean stateError;
    // If true, do not allow to add another DOT
    private boolean lastDot;
    //*********************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        databaseReference = FirebaseDatabase.getInstance().getReference("data");

        init();
        viewQR();

        if (android.os.Build.VERSION.SDK_INT > 15) {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        pojoList = new ArrayList<>();

        QRcode = findViewById(R.id.imageView);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        strName = sharedPreferences.getString("name", "");
        //strName = sharedPreferences.getString("bankName", "");
        strUpiId = sharedPreferences.getString("upiId", "");
        Log.d("Data = ", " == " + strName + " " + strUpiId);

        cnt = sharedPreferences.getInt("count", 0);
        Log.d("Count pref = ", " == " + cnt);

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
        // Find the TextView
        this.txtScreen = (TextView) findViewById(R.id.txtScreen);
        // Find and set OnClickListener to numeric buttons
        setNumericOnClickListener();
        // Find and set OnClickListener to operator buttons, equal button and decimal point button
        setOperatorOnClickListener();

        generate = findViewById(R.id.btnGenerateQR);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = txtScreen.getText().toString();
                try {
                    generateQR();
                    buttonViewQR.setVisibility(View.VISIBLE);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalculatorActivity.this, MainActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CalculatorActivity.this, MainActivity.class));
            }
        });
    }

    // Find and set OnClickListener to numeric buttons.
    private void setNumericOnClickListener() {
        // Create a common OnClickListener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just append/set the text of clicked button
                Button button = (Button) v;
                if (stateError) {
                    // If current state is Error, replace the error message
                    txtScreen.setText(button.getText());
                    stateError = false;
                } else {
                    // If not, already there is a valid expression so append to it
                    txtScreen.append(button.getText());
                }
                // Set the flag
                lastNumeric = true;
            }
        };
        // Assign the listener to all the numeric buttons
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * Find and set OnClickListener to operator buttons, equal button and decimal point button.
     */
    private void setOperatorOnClickListener() {
        // Create a common OnClickListener for operators
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the current state is Error do not append the operator
                // If the last input is number only, append the operator
                if (lastNumeric && !stateError) {
                    Button button = (Button) v;
                    txtScreen.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;    // Reset the DOT flag
                }
            }
        };
        // Assign the listener to all the operator buttons
        for (int id : operatorButtons) {
            findViewById(id).setOnClickListener(listener);
        }
        // Decimal point
        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastNumeric && !stateError && !lastDot) {
                    txtScreen.append(".");
                    lastNumeric = false;
                    lastDot = true;
                }
            }
        });
        // Clear button
        findViewById(R.id.btnClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtScreen.setText("");  // Clear the screen
                // Reset all the states and flags
                lastNumeric = false;
                stateError = false;
                lastDot = false;
            }
        });
        // Equal button
        findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEqual();
            }
        });
    }

    /**
     * Logic to calculate the solution.
     */
    private void onEqual() {
        // If the current state is error, nothing to do.
        // If the last input is a number only, solution can be found.
        if (lastNumeric && !stateError) {
            // Read the expression
            String txt = txtScreen.getText().toString();
            // Create an Expression (A class from exp4j library)
            Expression expression = new ExpressionBuilder(txt).build();
            try {
                // Calculate the result and display
                double result = expression.evaluate();
                txtScreen.setText(Double.toString(result));
                lastDot = true; // Result contains a dot
            } catch (ArithmeticException ex) {
                // Display an error message
                txtScreen.setText("Error");
                stateError = true;
                lastNumeric = false;
            }
        }
    }

    private void init() {
        buttonViewQR = findViewById(R.id.buttonViewQR);
        ivQRClose = findViewById(R.id.ivQRClose);
        back = findViewById(R.id.ivBack);
        home = findViewById(R.id.ivHome);
        layout = findViewById(R.id.layout);
    }

    public void viewQR() {
        buttonViewQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRcode.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                txtScreen.setVisibility(View.GONE);
                ivQRClose.setVisibility(View.VISIBLE);
            }
        });

        ivQRClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRcode.setVisibility(View.GONE);
                ivQRClose.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                txtScreen.setVisibility(View.VISIBLE);
            }
        });
    }

    public void generateQR() throws UnsupportedEncodingException, URISyntaxException {

        Uri uri = Uri.parse("upi://pay?").buildUpon()
                .appendQueryParameter("appid", appId)
                .appendQueryParameter("tr", "")
                .appendQueryParameter("mc", "")
                .appendQueryParameter("pa", strUpiId)
                .appendQueryParameter("pn", strName)
                .appendQueryParameter("tn", "")
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        //Uri uri = Uri.parse("upi://pay?appid=com.infrasofttech.mahaupi&tr=&mc=&pa=keshavmetkar@mahb&pn=Mr%KESHAV%MANOHAR%METKAR&tn=&am=100&cu=INR");
        //pay?appid=com.infrasofttech.mahaupi&tr=&mc=&pa=9503677978bvv0ybl&pn=anil%20Gaikwad&tn=466Tth&am=12&cu=INR
        //Uri uri1 = Uri.parse(" http://192.168.2.214:80/upi://pay?appid=com.infrasofttech.mahaupi&tr=&mc=&pa=9763032818@ybl&pn=KANCHAN%20ZINJADE&tn=test%20data&am=10&cu=INR");

        //pay?appid=com.infrasofttech.mahaupi&tr=&mc=&pa=9503677978@ybl&pn=Sagar%darade&tn=&am=3214&cu=INR
        //upi://pay?appid=com.infrasofttech.mahaupi&tr=&mc=&pa=keshavmetkar@mahb&pn=Mr%KESHAV%MANOHAR%METKAR&tn=&am=100&cu=INR

        Log.d("Upi Id", "URL = " + uri);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        String data = "", data1 = "";
//        data = "http://192.168.43.214:80/" + URLEncoder.encode(uri.toString(), "UTF-8");
        data = "http://192.168.43.214:80/" + uri;
        //data = URLDecoder.decode(uri.toString(), "UTF-8");

        //URI uri1 = new URI(data.replace(" ", "%"));

        //data1 = "http://192.168.43.214:80/" + uri;

        webView.loadUrl(data);
        Log.d("Url", "Url  " + data);


        final QRCodeWriter writer = new QRCodeWriter();

        URL = getUPIString(strUpiId, strName, amount);

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
        Log.d("Bitmap ", "Bitmap =  " + bmp);
        image = bmp;
        //BitMapToString(bmp);
        //Log.d("Bitmap image", "Bitmap image =  " + BitMapToString(bmp));

        // edt1.setText("");
        Toast.makeText(getApplicationContext(), "QR Code Generated Successfully", Toast.LENGTH_LONG).show();
        //startActivity(intent);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private String getUPIString(String payeeAddress,
                                String payeeName,
                                String payeeAmount) {

        String UPI = "upi://pay?" +
                "pa=" + payeeAddress +
                "&pn=" + payeeName +
                "&am=" + payeeAmount;

        return UPI.replace(" ", "+");
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public void getAmount() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pojoList.clear();

                for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                    PojoSave pojoSave = dataSnapshot.getValue(PojoSave.class);
                    //Log.d("data 0000", " = " + pojoSave);
                    count++;
                }
                Log.d("Count", " = " + count);
                editor.putInt("count", count);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
