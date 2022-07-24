package com.anstudios.freshjoy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.freshjoy.models.modelSearch;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {

    ProgressBar progressBar;
    String verificationCode;
    private CardView continueBtn;
    private EditText otpbox1, otpbox2, otpbox3, otpbox4, otpbox5, otpbox6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        setContentView(R.layout.activity_otp_verification);
        progressBar = findViewById(R.id.progress_bar);
        otpbox1 = findViewById(R.id.otpBox1);
        otpbox2 = findViewById(R.id.otpBox2);
        otpbox3 = findViewById(R.id.otpBox3);
        otpbox4 = findViewById(R.id.otpBox4);
        otpbox5 = findViewById(R.id.otpBox5);
        otpbox6 = findViewById(R.id.otpBox6);
        continueBtn = findViewById(R.id.otp_continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String otp = otpbox1.getText().toString() +
                            otpbox2.getText().toString() +
                            otpbox3.getText().toString() +
                            otpbox4.getText().toString() +
                            otpbox5.getText().toString() +
                            otpbox6.getText().toString();
                    verifyCode(otp);
                } catch (Exception e) {
                    Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sendVerificationCode(getIntent().getStringExtra("phoneNumber"));
    }

    private void sendVerificationCode(String phoneno) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+251" + phoneno)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                String code = phoneAuthCredential.getSmsCode();
                                Toast.makeText(OtpVerification.this, code, Toast.LENGTH_SHORT).show();
                                if (code != null) {
                                    verifyCode(code);
                                    otpbox1.setText(code.charAt(0) + "");
                                    otpbox2.setText(code.charAt(1) + "");
                                    otpbox3.setText(code.charAt(2) + "");
                                    otpbox4.setText(code.charAt(3) + "");
                                    otpbox5.setText(code.charAt(4) + "");
                                    otpbox6.setText(code.charAt(5) + "");
                                }
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpVerification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(OtpVerification.this, "OTP Sent Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        signInUser(credential);
    }


    private void getBuyer() {
        SplashScreen.sharedPreferences.edit().putString("phoneNumber",getIntent().getStringExtra("phoneNumber")).apply();
        String url = Constant.baseUrl + "buyers/find/phone/" + getIntent().getStringExtra("phoneNumber");
//        String url="https://myspotifyisbest.herokuapp.com/get-playlist/"+
//              "BQBjgvqVo9OsiWdDghsrwLRkF1oN8wppxVVlPDs3ygArFAickSFZ3NbA7gVy7X_MVlC8WTMAloZAG9JKphJBpYuS4-I1ZO4I25KLktbFrYbsYTend42wdZ29FScinl-X5txPdWqkAytfpkBbiogWTl7l2u-t0VOd-ySCZruePGnb3vxBpwbI_WvhUkaEt0ZFVmVPUE8PpaXTfARhYe8kyQlxzqaT5sFtOlGZ48ReKgAu2c5CyzEwpYAe-Y6_mSckQ641iWge2_e0c18dOSPzulcPlQ4";
        RequestQueue queue = Volley.newRequestQueue(OtpVerification.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    SplashScreen.sharedPreferences.edit().putString("id",jsonObject.get("_id")+"").apply();
                    SplashScreen.sharedPreferences.edit().putString("address",jsonObject.get("address")+"").apply();
                    String status=jsonObject.getString("status");
                    Toast.makeText(OtpVerification.this, jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                    SplashScreen.sharedPreferences.edit().putString("status", status).apply();
                    if(status.equals("pending")){
                        Toast.makeText(OtpVerification.this, "Your Registration is under process", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OtpVerification.this, ContactUsPage.class));
                        finish();
                    }else if (status.equals("rejected")) {
                        Toast.makeText(OtpVerification.this, "Your Registration was rejected", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OtpVerification.this, UploadInfo.class));
                        finish();
                    } else {
                        startActivity(new Intent(OtpVerification.this, MainActivity.class));
                        finish();
                    }

//                    if (hashMap != null) {
//                        String status = (String) hashMap.get("status");
//                        if (status.equals("verifying")) {
//                            startActivity(new Intent(OtpVerification.this, ThankYou.class));
//                            finish();
//                        } else {
//                            startActivity(new Intent(OtpVerification.this, MainActivity.class));
//                            finish();
//                        }
//                    } else {
//                        startActivity(new Intent(OtpVerification.this, UploadInfo.class));
//                        finish();
//                    }
                } catch (Exception e) {
                    startActivity(new Intent(OtpVerification.this, UploadInfo.class));
                    finish();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                startActivity(new Intent(OtpVerification.this, UploadInfo.class));
                finish();
            }

        });

        queue.add(request);
    }

    HashMap<String, Object> hashMap;

    private void signInUser(PhoneAuthCredential phoneAuthCredential) {
        try {
//            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            SplashScreen.sharedPreferences.edit().putString("phoneNumber", getIntent().getStringExtra("phoneNumber")).apply();
////                            FirebaseFirestore.getInstance().collection("users")
////                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
////                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////                                @Override
////                                public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
////                                    Map<String, Object> hashMap = documentSnapshot.getData();
////                                    if (hashMap != null) {
////                                        String status = (String) hashMap.get("status");
////                                        if (status.equals("verifying")) {
////                                            startActivity(new Intent(OtpVerification.this, ThankYou.class));
////                                            finish();
////                                        } else {
////                                            startActivity(new Intent(OtpVerification.this, MainActivity.class));
////                                            finish();
////                                        }
////                                    } else {
////                                        startActivity(new Intent(OtpVerification.this, UploadInfo.class));
////                                        finish();
////                                    }
////                                }
////                            });
////
//
//                        } else {
//                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });

            getBuyer();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}