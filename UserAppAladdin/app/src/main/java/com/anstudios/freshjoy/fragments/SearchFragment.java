package com.anstudios.freshjoy.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.freshjoy.ApiClient;
import com.anstudios.freshjoy.ApiInterface;
import com.anstudios.freshjoy.Constant;
import com.anstudios.freshjoy.MainActivity;
import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.adapters.adapterSearch;
import com.anstudios.freshjoy.models.HashmapSaver;
import com.anstudios.freshjoy.models.Product;
import com.anstudios.freshjoy.models.modelSearch;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    public static TextView cartItems;
    public static TextView cartItemsPrice;
    private EditText searchBox;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<modelSearch> arrayList;
    private adapterSearch adapter;
    private ArrayList<modelSearch> temp;
    private LinearLayout linearLayout;
    private CardView viewCartBtn;
    private JSONArray jsonArray;
    public static ArrayList<String> chekList;

    private void updateDownCartValues(JSONArray jsonArray) {
        HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
        if (hashMap != null) {
//            FirebaseFirestore.getInstance().collection("products")
//                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
//                    try {
//                        List<DocumentSnapshot> str = queryDocumentSnapshots.getDocuments();
//                        int tp = 0;
//                        int totNum = 0;
//                        for (int i = 0; i < hashMap.size(); i++) {
//                            String productId = (String) hashMap.keySet().toArray()[i];
//                            for (DocumentSnapshot documentSnapshot : str) {
//                                if (productId.equals(documentSnapshot.get("productId").toString())) {
//                                    int realPriceStr = Integer.parseInt(documentSnapshot.get("price").toString());
//                                    tp += realPriceStr * (Integer.parseInt(hashMap.get(productId)));
//                                    totNum += Integer.parseInt(hashMap.get(productId));
//                                }
//                                chekList.add(documentSnapshot.get("productId").toString());
//                            }
//                        }
//                        SearchFragment.cartItems.setText(totNum + " Items");
//                        String symbol = getResources().getString(R.string.currency_symbol);
//
//                        SearchFragment.cartItemsPrice.setText(symbol + tp);
//                    } catch (Exception e) {
//                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            });
//
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int tp = 0;
                    int totNum = 0;
                    String id = jsonObject.getString("_id");
                    for (int k = 0; k < hashMap.size(); k++) {
                        String savedId = (String) hashMap.keySet().toArray()[k];
                        if (id.equals(savedId)) {
                            int realPriceStr = jsonObject.getInt("price");
                            tp += realPriceStr * (Integer.parseInt(hashMap.get(id)));
                            totNum += Integer.parseInt(hashMap.get(id));
                        }
                        chekList.add(id);
                    }
                    cartItems.setText(totNum + " Items");
                    String symbol = getResources().getString(R.string.currency_symbol);

                    cartItemsPrice.setText(symbol + tp);
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.search_recycler);
        MainActivity.fragmentStr = "search";
        temp = new ArrayList<>();
        chekList = new ArrayList<>();
        cartItems = view.findViewById(R.id.search_no_of_items);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        cartItemsPrice = view.findViewById(R.id.search_total_pric);
        viewCartBtn = view.findViewById(R.id.search_view_cart_btn);
        searchBox = view.findViewById(R.id.searchBox);
        searchQuery("");

        arrayList = new ArrayList<>();
        linearLayout = view.findViewById(R.id.frag_search_linear);
        adapter = new adapterSearch(temp, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        viewCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.lastScreen = "main";

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new CartFragment())
                        .commit();
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery(searchBox.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchQuery(searchBox.getText().toString());
                }
                return false;
            }
        });

        return view;
    }

    private void searchQuery(String string) {
//        FirebaseFirestore.getInstance().collection("products")
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
//                List<DocumentSnapshot> str = queryDocumentSnapshots.getDocuments();
//                arrayList.clear();
//                recyclerView.setVisibility(View.VISIBLE);
//                linearLayout.setVisibility(View.INVISIBLE);
//                for (DocumentSnapshot documentSnapshot : str) {
//                    Log.d("anshul", String.valueOf(documentSnapshot.getData().get("title")));
//                    if (documentSnapshot.get("title").toString().toLowerCase().contains(string.toLowerCase())) {
//                        arrayList.add(new modelSearch(documentSnapshot.get("dateTime").toString(), documentSnapshot.get("productId").toString(), documentSnapshot.get("title").toString(),
//                                documentSnapshot.get("measuringUnit").toString(),
//                                documentSnapshot.get("moq").toString(),
//                                documentSnapshot.get("slashedPrice").toString(),
//                                documentSnapshot.get("image").toString(),
//                                documentSnapshot.get("price").toString()
//                        ));
//                    } else if (documentSnapshot.get("measuringUnit").toString().toLowerCase().contains(string.toLowerCase())) {
//                        arrayList.add(new modelSearch(documentSnapshot.get("dateTime").toString(), documentSnapshot.get("productId").toString(), documentSnapshot.get("title").toString(),
//                                documentSnapshot.get("measuringUnit").toString(),
//                                documentSnapshot.get("moq").toString(),
//                                documentSnapshot.get("slashedPrice").toString(),
//                                documentSnapshot.get("image").toString(),
//                                documentSnapshot.get("price").toString()
//                        ));
//                    } else if (documentSnapshot.get("price").toString().toLowerCase().contains(string.toLowerCase())) {
//                        arrayList.add(new modelSearch(documentSnapshot.get("dateTime").toString(), documentSnapshot.get("productId").toString(), documentSnapshot.get("title").toString(),
//                                documentSnapshot.get("measuringUnit").toString(),
//                                documentSnapshot.get("moq").toString(),
//                                documentSnapshot.get("slashedPrice").toString(),
//                                documentSnapshot.get("image").toString(),
//                                documentSnapshot.get("price").toString()
//                        ));
//                    } else if (string.equals("")) {
//                        arrayList.add(new modelSearch(documentSnapshot.get("dateTime").toString(), documentSnapshot.get("productId").toString(), documentSnapshot.get("title").toString(),
//                                documentSnapshot.get("measuringUnit").toString(),
//                                documentSnapshot.get("moq").toString(),
//                                documentSnapshot.get("slashedPrice").toString(),
//                                documentSnapshot.get("image").toString(),
//                                documentSnapshot.get("price").toString()
//                        ));
//                    }
//                }
//                if (arrayList.size() == 0) {
//                    recyclerView.setVisibility(View.INVISIBLE);
//                    linearLayout.setVisibility(View.VISIBLE);
//                } else {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    linearLayout.setVisibility(View.INVISIBLE);
//                }
//                sortData();
//            }
//        });
//        Call<List<String>> call=apiInterface.getAllProducts();
//        call.enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                arrayList.clear();
//                Toast.makeText(getContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
//                recyclerView.setVisibility(View.VISIBLE);
//                linearLayout.setVisibility(View.INVISIBLE);
////                for(Product product:response.body()){
////                    arrayList.add(new modelSearch(product.get_id(), product.getTitle(),
////                            product.getMeasuringUnit(),
////                            product.getMoq()+"",
////                            product.getSlashedPrice()+"",
////                            product.getImage(),
////                            product.getPrice()+""
////                    ));
////                }
//                if (arrayList.size() == 0) {
//                    recyclerView.setVisibility(View.INVISIBLE);
//                    linearLayout.setVisibility(View.VISIBLE);
//                } else {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    linearLayout.setVisibility(View.INVISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//                Log.d("anshul",t.getLocalizedMessage()+"  "+call.request().body());
//            }
//        });
        String url = Constant.baseUrl + "product/all";
//        String url="https://myspotifyisbest.herokuapp.com/get-playlist/"+
//              "BQBjgvqVo9OsiWdDghsrwLRkF1oN8wppxVVlPDs3ygArFAickSFZ3NbA7gVy7X_MVlC8WTMAloZAG9JKphJBpYuS4-I1ZO4I25KLktbFrYbsYTend42wdZ29FScinl-X5txPdWqkAytfpkBbiogWTl7l2u-t0VOd-ySCZruePGnb3vxBpwbI_WvhUkaEt0ZFVmVPUE8PpaXTfARhYe8kyQlxzqaT5sFtOlGZ48ReKgAu2c5CyzEwpYAe-Y6_mSckQ641iWge2_e0c18dOSPzulcPlQ4";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (string.equals("")) {
                        updateDownCartValues(new JSONArray(response));
                    }
                    jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if ((jsonObject.get("title") + "").toLowerCase().contains(string)) {
                            temp.add(new modelSearch(jsonObject.get("seller")+"",jsonObject.get("_id") + "", jsonObject.get("title") + "",
                                    jsonObject.get("measuringUnit") + "",
                                    jsonObject.get("moq") + "",

                                    jsonObject.get("slashedPrice") + "",
                                    jsonObject.get("image") + "",
                                    jsonObject.get("price") + ""
                            ));
                        }

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
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

    ApiInterface apiInterface;

}