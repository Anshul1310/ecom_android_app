package com.anstudios.sellerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.sellerapp.models.Seller;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences("shared",MODE_PRIVATE);
        setContentView(R.layout.activity_splash_screen);
        new CountDownTimer(2500,2500){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (sharedPreferences.getString("status","").equals("")) {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    finish();
                }else {
                    if(SplashScreen.sharedPreferences.getString("status","").equals("verified")){
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    }else{
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    }

                }

            }
        }.start();
    }

//    private void configureData() {
//        if (SplashScreen.sharedPreferences.getString("id", "").equals("")) {
//            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
//            finish();
//        } else {
//
//            String url = Constant.baseUrl + "buyers/find/phone/" + SplashScreen.sharedPreferences.getString("phoneNumber","");
//            RequestQueue queue = Volley.newRequestQueue(SplashScreen.this);
//            StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        SplashScreen.sharedPreferences.edit().putString("id", jsonObject.get("_id")+"").apply();
//
//                        SplashScreen.sharedPreferences.edit().putString("address",jsonObject.getString("address")+"").apply();
//                        SplashScreen.sharedPreferences.edit().putString("status", jsonObject.get("status") + "").apply();
//
//                        if (SplashScreen.sharedPreferences.getString("status", "pending").equals("pending")) {
//                            Toast.makeText(SplashScreen.this, "Your Registration is under process", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(SplashScreen.this, ContactUs.class));
//                            finish();
//                        } else if (SplashScreen.sharedPreferences.getString("status", "pending").equals("rejected")) {
//                            Toast.makeText(SplashScreen.this, "Your Registration was rejected", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(SplashScreen.this, UploadInfo.class));
//                            finish();
//                        } else {
//                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
//                            finish();
//                        }
//
//
//                    } catch (Exception e) {
//                        startActivity(new Intent(SplashScreen.this, UploadInfo.class));
//                        finish();
//                    }
//
//
//                }
//            }, new com.android.volley.Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("anshul", error.networkResponse.statusCode + "");
//                    startActivity(new Intent(SplashScreen.this, UploadInfo.class));
//                    finish();
//                }
//
//            });
//
//            queue.add(request);
//
//        }
//    }
}