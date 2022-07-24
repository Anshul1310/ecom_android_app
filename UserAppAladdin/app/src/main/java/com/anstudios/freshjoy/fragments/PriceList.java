package com.anstudios.freshjoy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.freshjoy.Constant;
import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.adapters.adapterPriceList;
import com.anstudios.freshjoy.models.modelPriceList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PriceList extends Fragment {
    private adapterPriceList adapterPriceList;
    private ArrayList<modelPriceList> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_price_list, container, false);
        arrayList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.price_list_recyler);
        adapterPriceList = new adapterPriceList(getContext(), arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterPriceList);
        getData();
        return view;
    }

    private void getData() {
        String url = Constant.baseUrl + "product-price/all";
//        String url="https://myspotifyisbest.herokuapp.com/get-playlist/"+
//              "BQBjgvqVo9OsiWdDghsrwLRkF1oN8wppxVVlPDs3ygArFAickSFZ3NbA7gVy7X_MVlC8WTMAloZAG9JKphJBpYuS4-I1ZO4I25KLktbFrYbsYTend42wdZ29FScinl-X5txPdWqkAytfpkBbiogWTl7l2u-t0VOd-ySCZruePGnb3vxBpwbI_WvhUkaEt0ZFVmVPUE8PpaXTfARhYe8kyQlxzqaT5sFtOlGZ48ReKgAu2c5CyzEwpYAe-Y6_mSckQ641iWge2_e0c18dOSPzulcPlQ4";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arrayList.add(new modelPriceList(jsonObject.getString("title"),
                                jsonObject.getString("location"),
                                jsonObject.get("measuringUnit") + "",
                                jsonObject.get("price") + "",
                                jsonObject.getString("image")));
                    }
                    adapterPriceList.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("anshul", error.getMessage());
            }

        });

        queue.add(request);
    }
}