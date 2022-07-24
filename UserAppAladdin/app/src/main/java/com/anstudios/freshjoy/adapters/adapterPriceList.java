package com.anstudios.freshjoy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.models.modelPriceList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapterPriceList extends RecyclerView.Adapter<adapterPriceList.holder> {

    private Context context;
    private ArrayList<modelPriceList> arrayList;

    public adapterPriceList(Context context, ArrayList<modelPriceList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_price_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.image);
        holder.title.setText(arrayList.get(position).getTitle());
        holder.location.setText(arrayList.get(position).getLocation());
        holder.priceIndicator.setText(arrayList.get(position).getPrice()+"/"+arrayList.get(position).getMeasuringUnit());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title, location, priceIndicator;

        public holder(@NonNull View itemView) {
            super(itemView);
            image= itemView.findViewById(R.id.price_list_image);
            title=itemView.findViewById(R.id.price_list_title);
            location=itemView.findViewById(R.id.price_list_location);
            priceIndicator=itemView.findViewById(R.id.price_list_price);
        }
    }
}
