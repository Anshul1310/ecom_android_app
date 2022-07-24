package com.anstudios.sellerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.sellerapp.adapters.adapterPaymentHistory;
import com.anstudios.sellerapp.models.Transaction;
import com.anstudios.sellerapp.models.modelOrders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Transaction> arrayList;
    private adapterPaymentHistory adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        recyclerView = findViewById(R.id.payment_history_recycler);
        arrayList = new ArrayList<>();
        adapter = new adapterPaymentHistory(this, arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        String url = Constant.baseUrl + "transaction/find/seller/" + SplashScreen.sharedPreferences.getString("id", "");
        RequestQueue queue = Volley.newRequestQueue(PaymentHistory.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arrayList.add(new Transaction(jsonObject.get("payout") + "",
                                jsonObject.get("type") + "", jsonObject.get("createdAt") + ""));
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(PaymentHistory.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        queue.add(request);
    }
}