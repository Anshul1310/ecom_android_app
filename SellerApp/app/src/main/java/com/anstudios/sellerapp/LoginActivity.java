package com.anstudios.sellerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.sellerapp.models.Seller;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView loginBtn;
    private ApiInterface apiInterface;
    private TextView register;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.txt_login);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        email = findViewById(R.id.ed_email);
        password = findViewById(R.id.ed_password);
        register=findViewById(R.id.txt_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, UploadInfo.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    loginUser();
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginUser() {
//        String url = Constant.baseUrl + "sellers/login";
////        String url="https://myspotifyisbest.herokuapp.com/get-playlist/"+
////              "BQBjgvqVo9OsiWdDghsrwLRkF1oN8wppxVVlPDs3ygArFAickSFZ3NbA7gVy7X_MVlC8WTMAloZAG9JKphJBpYuS4-I1ZO4I25KLktbFrYbsYTend42wdZ29FScinl-X5txPdWqkAytfpkBbiogWTl7l2u-t0VOd-ySCZruePGnb3vxBpwbI_WvhUkaEt0ZFVmVPUE8PpaXTfARhYe8kyQlxzqaT5sFtOlGZ48ReKgAu2c5CyzEwpYAe-Y6_mSckQ641iWge2_e0c18dOSPzulcPlQ4";
//        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//
//                } catch (Exception e) {
//                    finish();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("anshul", error.getMessage());
//            }
//
//        });
//
//        queue.add(request);
        Seller seller = new Seller();
        seller.setEmail(email.getText().toString());
        seller.setPassword(password.getText().toString());
        Call<Seller> call = apiInterface.getSeller(seller);
        call.enqueue(new Callback<Seller>() {
            @Override
            public void onResponse(Call<Seller> call, Response<Seller> response) {
                if (response.isSuccessful()) {
                    SplashScreen.sharedPreferences.edit().putString("id", response.body().get_id()).apply();
                    SplashScreen.sharedPreferences.edit().putString("name", response.body().getName()).apply();
                    SplashScreen.sharedPreferences.edit().putString("level", response.body().getLevel()).apply();
                    SplashScreen.sharedPreferences.edit().putString("phone", String.valueOf(response.body().getPhone())+"").apply();

                    SplashScreen.sharedPreferences.edit().putString("status", response.body().getStatus()+"").apply();
                    SplashScreen.sharedPreferences.edit().putString("region", response.body().getRegion()+"").apply();
                    SplashScreen.sharedPreferences.edit().putString("zone", response.body().getZone()+"").apply();

                    SplashScreen.sharedPreferences.edit().putString("type", response.body().getType()).apply();
                    SplashScreen.sharedPreferences.edit().putString("bookNumber", response.body().getBookNumber()).apply();
                    SplashScreen.sharedPreferences.edit().putString("tin", response.body().getTin()).apply();
                    if(response.body().getStatus().equals("pending")){
                        startActivity(new Intent(LoginActivity.this, ContactUs.class));
                    }else if(response.body().getStatus().equals("rejected")){
                        startActivity(new Intent(LoginActivity.this, UploadInfo.class));
                    }else{
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {
                Log.d("anshul", t.getMessage());
            }
        });
    }

}