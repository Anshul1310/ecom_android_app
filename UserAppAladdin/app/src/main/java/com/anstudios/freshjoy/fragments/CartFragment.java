package com.anstudios.freshjoy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
import com.anstudios.freshjoy.PaymentActivity;
import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.SplashScreen;
import com.anstudios.freshjoy.UploadInfo;
import com.anstudios.freshjoy.adapters.adapterCart;
import com.anstudios.freshjoy.models.HashmapSaver;
import com.anstudios.freshjoy.models.modelCart;
import com.anstudios.freshjoy.models.modelOrders;
import com.anstudios.freshjoy.models.modelSearch;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    public static ArrayList<modelCart> arrayListCart;
    public static TextView subtotal, deliveryCharge, totalPrice;
    public HashMap<String, Object> order;
    public modelOrders object;
    private View view;
    private RecyclerView recyclerView;
    private adapterCart adapter;
    private CardView continueBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        try {
            MainActivity.fragmentStr = "cart";
            object = new modelOrders();
            recyclerView = view.findViewById(R.id.recycler_cart);
            arrayListCart = new ArrayList<>();
            adapter = new adapterCart(getContext(), arrayListCart);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            order = new HashMap<>();
            continueBtn = view.findViewById(R.id.cart_proceed_btn);
            subtotal = view.findViewById(R.id.cart_subtotal_price);
            deliveryCharge = view.findViewById(R.id.delivery_charge_price);
            totalPrice = view.findViewById(R.id.cart_total_price);
            getDataDelivery();
//            applyBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!coupanCode.getText().toString().equals("")) {
//                        try {
//                            FirebaseFirestore.getInstance().collection("coupanCodes")
//                                    .document(coupanCode.getText().toString())
//                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
//                                    if (documentSnapshot.exists()) {
//                                        try {
//                                            String off = documentSnapshot.getData().get("off").toString();
//                                            discountOff = Integer.parseInt(off);
//                                            int stInt = Integer.parseInt(subtotal.getText().toString()
//                                                    .replace(" ", "").replace("Rs", "")
//                                                    .replace(".00", "")
//                                                    .replace(".", "")
//                                            );
//                                            int deleiveryChargeInt = Integer.parseInt(deliveryCharge.getText().toString()
//                                                    .replace(".00", "")
//                                                    .replace(".", "")
//                                                    .replace(" ", "").replace("Rs", "")
//                                            );
//                                            if (stInt != 0 && deleiveryChargeInt != 0) {
//                                                double realOff = (Double.parseDouble(discountOff + "") / 100) * (deleiveryChargeInt + stInt);
//                                                discountPrice.setText("- Rs." + realOff);
//
//                                                totalPrice.setText(("Rs.") + Math.ceil((deleiveryChargeInt + stInt) - realOff) + "");
//                                                coupanCode.setEnabled(false);
//                                            }
//                                        } catch (Exception exception) {
//                                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//
//
//                                }
//                            });
//                        } catch (Exception exception) {
//                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        Toast.makeText(getContext(), "Invalid Input.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        HashMap<String, String> child;
                        HashMap<String, HashMap<String, String>> hashMap = new HashMap<>();
                        if (arrayListCart != null) {
                            for (int i = 0; i < arrayListCart.size(); i++) {
                                child = new HashMap<>();
                                child.put("title", arrayListCart.get(i).getTitle());
                                child.put("seller", arrayListCart.get(i).getSeller());

                                child.put("measuringUnit", arrayListCart.get(i).getMeasuringUnit());

                                child.put("price", arrayListCart.get(i).getPrice());
                                child.put("quantity", arrayListCart.get(i).getQuantity());
                                child.put("image", arrayListCart.get(i).getImage());
                                hashMap.put(arrayListCart.get(i).getProductId(), child);
                            }
                            object.setAddress(SplashScreen.sharedPreferences.getString("address", ""));
                            object.setPhoneNumber(SplashScreen.sharedPreferences.getString("phoneNumber", ""));
                            object.setHashMap(hashMap);
                            object.setStatus("preparing");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy | HH:mm", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());
                            object.setDateTime(currentDateandTime);
                            object.setUserId(SplashScreen.sharedPreferences.getString("id", ""));
                            object.setTotalPrice(totalPrice.getText().toString()
                                    .replace("Rs. ", "")
                                    .replace(".00","")
                                    .replace(".0","")
                                    .replace("Rs.","")
                            );
                            if (Integer.parseInt(object.getTotalPrice()) >= 499) {
                                Gson gson = new Gson();
                                String myJson = gson.toJson(object);
                                Intent intent = new Intent(getContext(), PaymentActivity.class);
                                intent.putExtra("object", myJson);
                                startActivity(intent);
                            } else {
                                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Minimum Order Value is Rs.499", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                    } catch (Exception e) {
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Minimum Order Value is Rs.499", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }

                }
            });
        } catch (Exception exception) {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public static double rate;
    private void getDataDelivery() {

        String url = Constant.baseUrl + "settings/rate";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rate=Double.parseDouble(response);
                getData();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });

        queue.add(request);

    }

    private void getData() {
        String url = Constant.baseUrl + "product/all";
//        String url="https://myspotifyisbest.herokuapp.com/get-playlist/"+
//              "BQBjgvqVo9OsiWdDghsrwLRkF1oN8wppxVVlPDs3ygArFAickSFZ3NbA7gVy7X_MVlC8WTMAloZAG9JKphJBpYuS4-I1ZO4I25KLktbFrYbsYTend42wdZ29FScinl-X5txPdWqkAytfpkBbiogWTl7l2u-t0VOd-ySCZruePGnb3vxBpwbI_WvhUkaEt0ZFVmVPUE8PpaXTfARhYe8kyQlxzqaT5sFtOlGZ48ReKgAu2c5CyzEwpYAe-Y6_mSckQ641iWge2_e0c18dOSPzulcPlQ4";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    fetchData(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void fetchData(JSONArray jsonArray) {
        try {
            arrayListCart.clear();
            HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
            if (hashMap != null) {
//                FirebaseFirestore.getInstance().collection("products")
//                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
//                        List<DocumentSnapshot> str = queryDocumentSnapshots.getDocuments();
//                        int tp = 0;
//                        for (int i = 0; i < hashMap.size(); i++) {
//                            String productId = (String) hashMap.keySet().toArray()[i];
//                            for (DocumentSnapshot documentSnapshot : str) {
//                                if (productId.equals(documentSnapshot.get("productId").toString())) {
//                                    arrayListCart.add(new modelCart(documentSnapshot.get("productId").toString(),
//                                            documentSnapshot.get("moq").toString(),
//                                            documentSnapshot.get("image").toString(),
//                                            hashMap.get(productId),
//                                            documentSnapshot.get("price").toString(),
//                                            documentSnapshot.get("title").toString()));
//                                }
//                            }
//                            adapter.notifyDataSetChanged();
//
//                        }
//
//                    }
//                });

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("_id");
                        for (int k = 0; k < hashMap.size(); k++) {
                            String savedId = (String) hashMap.keySet().toArray()[k];
                            if (id.equals(savedId)) {
                                arrayListCart.add(new modelCart(jsonObject.get("measuringUnit")+"",jsonObject.get("seller") + "", jsonObject.getString("_id"),
                                        jsonObject.getString("moq"),
                                        jsonObject.getString("image"),
                                        hashMap.get(jsonObject.getString("_id")),
                                        jsonObject.get("price") + "",
                                        jsonObject.getString("title")));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}
