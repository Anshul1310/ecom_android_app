package com.anstudios.sellerapp.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.sellerapp.Constant;
import com.anstudios.sellerapp.R;
import com.anstudios.sellerapp.SplashScreen;
import com.anstudios.sellerapp.adapters.adapterOrders;
import com.anstudios.sellerapp.adapters.adapterProducts;
import com.anstudios.sellerapp.models.modelOrders;
import com.anstudios.sellerapp.models.modelProducts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrdersFragment extends Fragment {

    private View view;
    adapterOrders adapterProducts;
    private ConstraintLayout onTheWayBtn;

    private RecyclerView recyclerView;
    private ArrayList<modelOrders> arrayList;
    private ConstraintLayout dispatchedBtn, allBtn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerView = view.findViewById(R.id.ordersRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        arrayList = new ArrayList<>();
        adapterProducts = new adapterOrders(getContext(), arrayList);
        recyclerView.setAdapter(adapterProducts);


        ConstraintLayout preparingBtn = view.findViewById(R.id.ordersPreparingBtn);
        dispatchedBtn = view.findViewById(R.id.orderDispatchBtn);
        onTheWayBtn = view.findViewById(R.id.orders_onTheWayBtn);
        ConstraintLayout deliveredBtn = view.findViewById(R.id.ordersDeliveredBtn);
        allBtn = view.findViewById(R.id.ordersAllBtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getMyOrders("all");
        allBtn.setOnClickListener(v -> {
            getMyOrders("all");
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        onTheWayBtn.setOnClickListener(v -> {
            getMyOrders("onTheWay");
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        deliveredBtn.setOnClickListener(v -> {
            getMyOrders("delivered");
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        dispatchedBtn.setOnClickListener(v -> {
            getMyOrders("dispatched");
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        preparingBtn.setOnClickListener(v -> {
            getMyOrders("preparing");
            preparingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.apptheme));
            allBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            dispatchedBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            deliveredBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
            onTheWayBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gray_eee));
        });
        return view;
    }

    private void getMyOrders(String status) {
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

//        String url = Constant.baseUrl + "order/find/user/" + SplashScreen.sharedPreferences.getString("id", "");
        String url = Constant.baseUrl+"order/all";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arrayList.clear();
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
                            arrayList.add(orders);
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
                            arrayList.add(orders);
                        }
                    }
                    adapterProducts.notifyDataSetChanged();
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