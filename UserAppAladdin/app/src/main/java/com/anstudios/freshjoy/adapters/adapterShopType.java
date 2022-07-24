package com.anstudios.freshjoy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.SplashScreen;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;

public class adapterShopType extends RecyclerView.Adapter<adapterShopType.holder> {

    private Context context;
    private ArrayList<String> name;
    private ArrayList<Integer> image;
    private ArrayList<Boolean> isCheckedList;


    public adapterShopType(Context context, ArrayList<String> name, ArrayList<Integer> image) {
        this.context = context;
        this.name = name;
        isCheckedList = new ArrayList<>(Arrays.asList(false, false, false, false, false, false));
        this.image = image;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_shop_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        if (!isCheckedList.get(position)) {
            holder.bg.setStrokeColor(Color.GRAY);
        } else {
            holder.bg.setStrokeColor(Color.GREEN);
        }
        holder.name.setText(name.get(position));
        holder.imageView.setImageResource(image.get(position));
        holder.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (int i = 0; i < 6; i++) {
                        isCheckedList.set(i, false);
                    }
                    SplashScreen.sharedPreferences.edit().putString("shopType", name.get(position)).apply();
                    isCheckedList.set(position, true);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class holder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name;
        private MaterialCardView bg;

        public holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.shop_type_image);
            name = itemView.findViewById(R.id.shop_type_name);
            bg = itemView.findViewById(R.id.shop_type_bg);
        }
    }
}
