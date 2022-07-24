package com.anstudios.freshjoy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    private static final String ONESIGNAL_APP_ID = "b17c92f3-4a73-49b6-92ce-aed52203926f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = getSharedPreferences("com.anstudios.freshjoy", MODE_PRIVATE);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
//                try {
//                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                        FirebaseFirestore.getInstance().collection("users")
//                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
//                                Map<String, Object> hashMap = documentSnapshot.getData();
//                                if (hashMap != null) {
//                                    String status = (String) hashMap.get("status");
//                                    if (status.equals("verifying")) {
//                                        sharedPreferences.edit().putString("address", hashMap.get("address").toString()).apply();
//                                        sharedPreferences.edit().putString("phoneNumber", hashMap.get("phoneNumber").toString()).apply();
//                                        sharedPreferences.edit().putString("name", hashMap.get("name").toString()).apply();
//                                        startActivity(new Intent(SplashScreen.this, ThankYou.class));
//                                        finish();
//                                    } else if (hashMap.get("status").toString().equals("rejected")) {
//                                        Toast.makeText(SplashScreen.this, "Your uploaded information was incorrect, Please fill it again", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(SplashScreen.this, UploadInfo.class));
//                                        finish();
//                                    } else {
//                                        sharedPreferences.edit().putString("phoneNumber", hashMap.get("phoneNumber").toString()).apply();
//                                        sharedPreferences.edit().putString("name", hashMap.get("name").toString()).apply();
//                                        sharedPreferences.edit().putString("address", hashMap.get("address").toString()).apply();
//
//                                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
//                                        finish();
//                                    }
//                                } else {
//                                    startActivity(new Intent(SplashScreen.this, UploadInfo.class));
//                                    finish();
//                                }
//                            }
//                        });
//                    } else {
//                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
//                        finish();
//                    }
//                } catch (Exception exception) {
//                    Toast.makeText(SplashScreen.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
//                }


                configureData();
            }
        }.start();
    }


    private void setLocale(){
        Locale locale=new Locale(SplashScreen.sharedPreferences.getString("language","en"));
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
    }

    private void configureData() {
        if (SplashScreen.sharedPreferences.getString("id", "").equals("")) {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
        } else {

            String url = Constant.baseUrl + "buyers/find/phone/" + SplashScreen.sharedPreferences.getString("phoneNumber","");
            RequestQueue queue = Volley.newRequestQueue(SplashScreen.this);
            StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        SplashScreen.sharedPreferences.edit().putString("id", jsonObject.get("_id")+"").apply();

                        SplashScreen.sharedPreferences.edit().putString("address",jsonObject.getString("address")+"").apply();
                        SplashScreen.sharedPreferences.edit().putString("status", jsonObject.get("status") + "").apply();

                        if (SplashScreen.sharedPreferences.getString("status", "pending").equals("pending")) {
                            Toast.makeText(SplashScreen.this, "Your Registration is under process", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashScreen.this, ContactUsPage.class));
                            finish();
                        } else if (SplashScreen.sharedPreferences.getString("status", "pending").equals("rejected")) {
                            Toast.makeText(SplashScreen.this, "Your Registration was rejected", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashScreen.this, UploadInfo.class));
                            finish();
                        } else {
                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                            finish();
                        }


                    } catch (Exception e) {
                        startActivity(new Intent(SplashScreen.this, UploadInfo.class));
                        finish();
                    }


                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    startActivity(new Intent(SplashScreen.this, UploadInfo.class));
                    finish();
                }

            });

            queue.add(request);

        }
    }


}