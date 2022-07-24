package com.anstudios.sellerapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.sellerapp.Constant;
import com.anstudios.sellerapp.R;
import com.anstudios.sellerapp.adapters.adapterNews;
import com.anstudios.sellerapp.models.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<News> arrayList;
    private adapterNews adapternews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerView=view.findViewById(R.id.news_recycler);
        arrayList=new ArrayList<>();
        adapternews=new adapterNews(getContext(), arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapternews);
        getData();
        return view;
    }

    private void getData() {
        String url = Constant.baseUrl + "news/all";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        arrayList.add(new News(jsonObject.get("title")+"", jsonObject.get("description")+"", jsonObject.get("image")+""));
                    }
                    Log.d("anshul",response);
                    adapternews.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("anshul","dffd"+ error.getMessage());
            }

        });

        queue.add(request);
    }
}