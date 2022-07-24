package com.anstudios.sellerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.sellerapp.ApiClient;
import com.anstudios.sellerapp.ApiInterface;
import com.anstudios.sellerapp.Constant;
import com.anstudios.sellerapp.PaymentHistory;
import com.anstudios.sellerapp.R;
import com.anstudios.sellerapp.SplashScreen;
import com.anstudios.sellerapp.models.Transaction;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView balance;
    private ImageView historyBtn;
    private CardView withDrawBtn;

    private ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        balance = view.findViewById(R.id.home_wallet_balance);
        historyBtn = view.findViewById(R.id.history_btn);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        withDrawBtn=view.findViewById(R.id.home_withdraw_button);
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PaymentHistory.class));
            }
        });
        withDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!balance.getText().toString().equals("0")){
                    Transaction transaction=new Transaction();
                    transaction.setSeller(SplashScreen.sharedPreferences.getString("id",""));
                    transaction.setPayout(balance.getText().toString());
                    Toast.makeText(getContext(), "YOur payout is under process", Toast.LENGTH_SHORT).show();
                    Call<String> call=apiInterface.initiateWithdrwalRequest(transaction);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            balance.setText("0");
                            Toast.makeText(getContext(), "Your payout is in under process", Toast.LENGTH_SHORT).show();
                            Log.d("anshul",String.valueOf(response.body()));
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }else{
                    Toast.makeText(getContext(), "No Sufficient Balance", Toast.LENGTH_SHORT).show();
                }


            }
        });
        getWalletBalance();
        return view;
    }

    private void getWalletBalance() {
        String url = Constant.baseUrl + "transaction/find/seller/walletBalance/" + SplashScreen.sharedPreferences.getString("id", "");
        Log.d("anshul",url);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                balance.setText(response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        queue.add(request);
    }

}