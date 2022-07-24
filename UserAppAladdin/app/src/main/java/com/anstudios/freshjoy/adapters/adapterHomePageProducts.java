package com.anstudios.freshjoy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.freshjoy.CategoryProducts;
import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.SplashScreen;
import com.anstudios.freshjoy.models.modelhomeProducts;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapterHomePageProducts extends RecyclerView.Adapter<adapterHomePageProducts.holder> {

    private Context context;
    private ArrayList<modelhomeProducts> arrayList;

    public adapterHomePageProducts(Context context, ArrayList<modelhomeProducts> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        Picasso.get().load(arrayList.get(position).getImage())
                .placeholder(R.drawable.upload_image_default)
                .into(holder.image);
        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                        .beginTransaction()
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .replace(R.id.frame_layout, new SearchFragment())
//                        .commit();
                Intent intent = new Intent(context, CategoryProducts.class);
                SplashScreen.sharedPreferences.edit().putString("category", arrayList.get(position).getTitle()).apply();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView image;
        private MaterialCardView materialCardView;

        public holder(@NonNull View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.layout_products_container);
            image = itemView.findViewById(R.id.category_homepage_image);
            title = itemView.findViewById(R.id.product_category_name);
        }
    }
}
