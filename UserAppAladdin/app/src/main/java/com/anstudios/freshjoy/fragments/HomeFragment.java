package com.anstudios.freshjoy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.anstudios.freshjoy.OtpVerification;
import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.ThankYou;
import com.anstudios.freshjoy.UploadInfo;
import com.anstudios.freshjoy.adapters.adapterHomePageProducts;
import com.anstudios.freshjoy.models.Categories;
import com.anstudios.freshjoy.models.Product;
import com.anstudios.freshjoy.models.modelSearch;
import com.anstudios.freshjoy.models.modelhomeProducts;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private View view;
    private ArrayList<modelhomeProducts> arrayList;
    private adapterHomePageProducts adapter;
    private RecyclerView recyclerView;
    private ImageSlider imageSlider;
    private MaterialCardView searchBar;

    private List<SlideModel> slideModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        searchBar = view.findViewById(R.id.homeSearchBar);
        imageSlider = view.findViewById(R.id.imageSlider);
        MainActivity.fragmentStr = "home";
        slideModelList = new ArrayList<>();
        getBannerImages();
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        recyclerView = view.findViewById(R.id.homeRecycler);
        recyclerView.setNestedScrollingEnabled(false);

        arrayList = new ArrayList<>();
        adapter = new adapterHomePageProducts(getContext(), arrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(adapter);

        searchBar.setOnClickListener(v -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.frame_layout, new SearchFragment())
                .commit());
        showCatgeory();
        return view;
    }

    private void getBannerImages() {
        FirebaseFirestore.getInstance().collection("adminSettings")
                .document("bannersSetting")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) documentSnapshot.getData();
                for (int i = 1; i <= hashMap.size(); i++) {
                    if (!hashMap.get("banner" + i).toString().equals("no image")) {
                        slideModelList.add(new SlideModel(hashMap.get("banner" + i).toString(), ScaleTypes.CENTER_CROP));
                    }
                }
                imageSlider.setImageList(slideModelList);
            }
        });
    }

    private void showCatgeory() {
//        FirebaseFirestore.getInstance()
//                .collection("categories")
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                try {
//                    HashMap<String, Object> hashMap;
//                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
//                        hashMap = (HashMap<String, Object>) queryDocumentSnapshots.getDocuments().get(i).getData();
//                        arrayList.add(new modelhomeProducts(hashMap.get("image").toString(),
//                                hashMap.get("name").toString()));
//                    }
//                    adapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//        Call<List<Categories>> call = apiInterface.getAllCategories();
//        call.enqueue(new Callback<List<Categories>>() {
//            @Override
//            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
//                for(Categories categories:response.body()){
//                    arrayList.add(new modelhomeProducts(categories.getImage(),
//                            categories.getName()));
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<List<Categories>> call, Throwable t) {
//
//            }
//        });

        String url = Constant.baseUrl + "categories/all";
//        String url="https://myspotifyisbest.herokuapp.com/get-playlist/"+
//              "BQBjgvqVo9OsiWdDghsrwLRkF1oN8wppxVVlPDs3ygArFAickSFZ3NbA7gVy7X_MVlC8WTMAloZAG9JKphJBpYuS4-I1ZO4I25KLktbFrYbsYTend42wdZ29FScinl-X5txPdWqkAytfpkBbiogWTl7l2u-t0VOd-ySCZruePGnb3vxBpwbI_WvhUkaEt0ZFVmVPUE8PpaXTfARhYe8kyQlxzqaT5sFtOlGZ48ReKgAu2c5CyzEwpYAe-Y6_mSckQ641iWge2_e0c18dOSPzulcPlQ4";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        arrayList.add(new modelhomeProducts(jsonObject.getString("image"),
                                jsonObject.getString("name")));
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

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
