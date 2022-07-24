package com.anstudios.freshjoy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.freshjoy.Constant;
import com.anstudios.freshjoy.ContactUsPage;
import com.anstudios.freshjoy.MainActivity;
import com.anstudios.freshjoy.OtpVerification;
import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.SplashScreen;
import com.anstudios.freshjoy.UploadInfo;
import com.anstudios.freshjoy.adapters.adapterOrders;
import com.anstudios.freshjoy.models.modelOrders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyOrdersFragment extends Fragment {

    private View view;
    private ConstraintLayout dispatchedBtn, allBtn;
    private adapterOrders adapter;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private ConstraintLayout onTheWayBtn;
    private ArrayList<modelOrders> arrayList;
    private ArrayList<modelOrders> temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        recyclerView = view.findViewById(R.id.ordersRecycler);
        arrayList = new ArrayList<>();
        temp = new ArrayList<>();
        MainActivity.fragmentStr = "order";
        progressBar = view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        linearLayout = view.findViewById(R.id.fragment_orders_status_layout);
        ConstraintLayout preparingBtn = view.findViewById(R.id.ordersPreparingBtn);
        dispatchedBtn = view.findViewById(R.id.orderDispatchBtn);
        onTheWayBtn = view.findViewById(R.id.orders_onTheWayBtn);
        ConstraintLayout deliveredBtn = view.findViewById(R.id.ordersDeliveredBtn);
        allBtn = view.findViewById(R.id.ordersAllBtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new adapterOrders(getContext(), temp);
        recyclerView.setAdapter(adapter);
        getMyOrders("all");
        allBtn.setOnClickListener(v -> {
            getMyOrders("all");
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_theme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        onTheWayBtn.setOnClickListener(v -> {
            getMyOrders("onTheWay");
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_theme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        deliveredBtn.setOnClickListener(v -> {
            getMyOrders("delivered");
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_theme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        dispatchedBtn.setOnClickListener(v -> {
            getMyOrders("dispatched");
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_theme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        preparingBtn.setOnClickListener(v -> {
            getMyOrders("preparing");
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green_theme));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        return view;
    }

    private void getMyOrders(String status) {
        progressBar.setVisibility(View.VISIBLE);
//        FirebaseFirestore.getInstance().collection("orders")
//                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    arrayList.clear();
//                    Map<String, Object> hashMap;
//                    if (task.getResult().getDocuments().size() != 0) {
//                        for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
//                            hashMap = task.getResult().getDocuments().get(i).getData();
//                            if (hashMap.get("status").equals(status)) {
//                                modelOrders orders = new modelOrders();
//                                orders.setOrderId(hashMap.get("orderId").toString());
//                                orders.setDateTime(hashMap.get("dateTime").toString());
//                                orders.setPaymentType(hashMap.get("paymentType").toString());
//                                orders.setTotalPrice(hashMap.get("totalPrice").toString());
//                                orders.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                orders.setStatus(hashMap.get("status").toString());
//                                orders.setAddress(hashMap.get("address").toString());
//                                orders.setPhoneNumber(hashMap.get("phoneNumber").toString());
//                                orders.setHashMap((HashMap<String, HashMap<String, String>>) hashMap.get("hashMap"));
//                                arrayList.add(orders);
//                            } else if (status.equals("all")) {
//                                modelOrders orders = new modelOrders();
//                                orders.setDateTime(hashMap.get("dateTime").toString());
//                                orders.setOrderId(hashMap.get("orderId").toString());
//                                orders.setPaymentType(hashMap.get("paymentType").toString());
//                                orders.setTotalPrice(hashMap.get("totalPrice").toString());
//                                orders.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                orders.setStatus(hashMap.get("status").toString());
//                                orders.setAddress(hashMap.get("address").toString());
//                                orders.setPhoneNumber(hashMap.get("phoneNumber").toString());
//                                orders.setHashMap((HashMap<String, HashMap<String, String>>) hashMap.get("hashMap"));
//                                arrayList.add(orders);
//                            }
//                        }
//                        progressBar.setVisibility(View.INVISIBLE);
//                    } else {
//                        linearLayout.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//
//                }
//            }
//        });

        String url = Constant.baseUrl + "order/find/user/" + SplashScreen.sharedPreferences.getString("id", "");
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        temp.clear();
                        if (status.equals(jsonObject.get("status") + "")) {
                            modelOrders orders = new modelOrders();
                            orders.setOrderId(jsonObject.get("orderId").toString());
                            orders.setDateTime(jsonObject.get("createdAt").toString().substring(0,10));
                            orders.setPaymentType("COD");
                            orders.setTotalPrice(jsonObject.get("totalPrice").toString());
                            orders.setUserId(jsonObject.get("buyer").toString());
                            orders.setStatus(jsonObject.get("status").toString());
                            orders.setNote(jsonObject.get("note")+"");
                            orders.setAddress(jsonObject.get("address").toString());
                            orders.setPhoneNumber(jsonObject.get("phone").toString());
                            orders.setItems((JSONArray) jsonObject.get("items"));
                            temp.add(orders);
                        } else if (status.equals("all")) {

                            modelOrders orders = new modelOrders();
                            orders.setOrderId(jsonObject.get("orderId").toString());
                            orders.setDateTime(jsonObject.get("createdAt").toString().substring(0,10));
                            orders.setPaymentType("COD");
                            orders.setNote(jsonObject.get("note")+"");

                            orders.setTotalPrice(jsonObject.get("totalPrice").toString());
                            orders.setUserId(jsonObject.get("buyer").toString());
                            orders.setStatus(jsonObject.get("status").toString());
                            orders.setAddress(jsonObject.get("address").toString());
                            orders.setPhoneNumber(jsonObject.get("phone").toString());
                            orders.setItems((JSONArray) jsonObject.get("items"));
                            temp.add(orders);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
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

//                .
    }

}