package com.anstudios.freshjoy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.freshjoy.adapters.adapterCategorySearch;
import com.anstudios.freshjoy.fragments.SearchFragment;
import com.anstudios.freshjoy.models.Categories;
import com.anstudios.freshjoy.models.HashmapSaver;
import com.anstudios.freshjoy.models.Product;
import com.anstudios.freshjoy.models.modelSearch;
import com.anstudios.freshjoy.models.modelhomeProducts;
import com.google.android.gms.tasks.OnFailureListener;
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

public class CategoryProducts extends AppCompatActivity {

    public static TextView cartItems;
    public static TextView cartItemsPrice;
    private RecyclerView recyclerView;
    private ArrayList<modelSearch> arrayList;
    private ArrayList<modelSearch> temp;
    private adapterCategorySearch adapterSearch;
    private CardView viewCartBtn;
    private LinearLayout linearLayout;
    public static ArrayList<String> chekListCat;
    private JSONArray jsonArray;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CategoryProducts.this, MainActivity.class));
    }


    private void updateDownCartValues() {
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
//                                chekListCat.add(documentSnapshot.get("productId").toString());
//                            }
//                        }
//                        cartItems.setText(totNum + " Items");
//                        String symbol = getResources().getString(R.string.currency_symbol);
//
//                        cartItemsPrice.setText(symbol + tp);
//                    } catch (Exception e) {
//                        Toast.makeText(CategoryProducts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            });


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
                        chekListCat.add(id);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        setContentView(R.layout.activity_category_products);
        temp = new ArrayList<>();
        chekListCat = new ArrayList<>();
        linearLayout = findViewById(R.id.category_search_linear);
        cartItems = findViewById(R.id.search_no_of_items);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        cartItemsPrice = findViewById(R.id.search_total_pric);
        viewCartBtn = findViewById(R.id.category_buy_now_btn);

        recyclerView = findViewById(R.id.category_products_recycler);
        arrayList = new ArrayList<>();
        adapterSearch = new adapterCategorySearch(temp, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterSearch);
        viewCartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryProducts.this, MainActivity.class);
            intent.putExtra("from", "cart");
            MainActivity.lastScreen = "category";
            startActivity(intent);
        });
        try {
            searchQuery(SplashScreen.sharedPreferences.getString("category", ""));
        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void searchQuery(String string) {
        String url = Constant.baseUrl + "product/all";
        RequestQueue queue = Volley.newRequestQueue(CategoryProducts.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                arrayList.clear();
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                try {
                    arrayList.clear();
                    recyclerView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    jsonArray = new JSONArray(response);
                    updateDownCartValues();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.get("category").toString().toLowerCase().equals(string.toLowerCase())) {
                            temp.add(new modelSearch(jsonObject.getString("seller") + "", jsonObject.get("_id") + "", jsonObject.get("title") + "",
                                    jsonObject.get("measuringUnit") + "",
                                    jsonObject.get("moq") + "",
                                    jsonObject.get("slashedPrice") + "",
                                    jsonObject.get("image") + "",
                                    jsonObject.get("price") + ""
                            ));
                        }
                    }
                    adapterSearch.notifyDataSetChanged();
                } catch (Exception exception) {
                    Toast.makeText(CategoryProducts.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("anshul", error.getMessage());
            }

        });

        queue.add(request);


//        Call<List<Product>> call=apiInterface.getAllProducts();
//        call.enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                arrayList.clear();
//                recyclerView.setVisibility(View.VISIBLE);
//                linearLayout.setVisibility(View.INVISIBLE);
//                for(Product product:response.body()){
//                    if ((product.getCategory().toLowerCase().contains(string.toLowerCase()))) {
//                        arrayList.add(new modelSearch(product.getSeller(),product.get_id(), product.getTitle(),
//                                product.getMeasuringUnit(),
//                                product.getMoq() + "",
//                                product.getSlashedPrice() + "",
//                                product.getImage(),
//                                product.getPrice() + ""
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
//                adapterSearch.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//
//            }
//        });

    }

    ApiInterface apiInterface;


}