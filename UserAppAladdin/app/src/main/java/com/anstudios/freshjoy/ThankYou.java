package com.anstudios.freshjoy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.anstudios.freshjoy.models.Buyers;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThankYou extends AppCompatActivity {

    private CardView contactUsBtn;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_thank_you);
        contactUsBtn = findViewById(R.id.contact_us_btn_thank_you);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        contactUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThankYou.this, ContactUsPage.class));
            }
        });
        uploadNotificationData();
    }


    private void uploadNotificationData() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(@NonNull String s) {
                        Buyers buyers = new Buyers();
                        buyers.setFcmToken(s);
                        Call<Buyers> call = apiInterface.postToken(buyers);
                        call.enqueue(new Callback<Buyers>() {
                            @Override
                            public void onResponse(Call<Buyers> call, Response<Buyers> response) {
                                Toast.makeText(ThankYou.this, "Data Uploaded Successfull.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Buyers> call, Throwable t) {
                                Log.d("anshul", call.toString()+ "   "  +t.getMessage());
                            }
                        });
                    }
                });

    }
}