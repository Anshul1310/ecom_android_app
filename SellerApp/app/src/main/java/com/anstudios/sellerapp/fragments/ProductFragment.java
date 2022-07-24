package com.anstudios.sellerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
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
import com.anstudios.sellerapp.AddProduct;
import com.anstudios.sellerapp.Constant;
import com.anstudios.sellerapp.LoginActivity;
import com.anstudios.sellerapp.R;
import com.anstudios.sellerapp.adapters.adapterProducts;
import com.anstudios.sellerapp.models.Product;
import com.anstudios.sellerapp.models.modelProducts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    private CardView addProductBtn;
    private RecyclerView recyclerView;
    private ArrayList<Product> arrayList;
    private adapterProducts adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        recyclerView = view.findViewById(R.id.productsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList = new ArrayList<>();
        addProductBtn = view.findViewById(R.id.fragment_add_product_btn);
        adapter = new adapterProducts(getContext(), arrayList);
        getData();
        recyclerView.setAdapter(adapter);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddProduct.class));
            }
        });
        return view;
    }

    private void getData() {
        String url = Constant.baseUrl + "product/all";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Product product = new Product();
                        product.setTitle(jsonObject.get("title") + "");
                        product.setImage(jsonObject.get("image") + "");
                        product.set_id(jsonObject.get("_id") + "");
                        product.setCategory(jsonObject.get("category") + "");
                        product.setMeasuringUnit(jsonObject.get("measuringUnit") + "");
                        product.setDescription(jsonObject.get("description") + "");
                        product.setMoq(Integer.parseInt(jsonObject.get("moq") + ""));
                        product.setPrice(Integer.parseInt(jsonObject.get("price") + ""));
                        product.setSlashedPrice(Integer.parseInt(jsonObject.get("slashedPrice") + ""));
                        arrayList.add(product);

                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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