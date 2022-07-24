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
import com.anstudios.freshjoy.models.modelOrdersPage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapterOrdersPage extends RecyclerView.Adapter<adapterOrdersPage.holder> {

    private Context context;
    private ArrayList<modelOrdersPage> arrayList;

    public adapterOrdersPage(Context context, ArrayList<modelOrdersPage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_orders_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.title.setText(arrayList.get(position).getTitle());
        Picasso.get().load(arrayList.get(position).getImage())
                .placeholder(R.drawable.upload_image_default)
                .into(holder.imageView);
        holder.quantity.setText("Price X " + arrayList.get(position).getQuantity());
        String symbol=context.getResources().getString(R.string.currency_symbol);

        holder.price.setText(symbol+arrayList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private TextView title, quantity, price;
        private ImageView imageView;

        public holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.layout_editDetails_image);
            title = itemView.findViewById(R.id.layout_editDetails_title);
            quantity = itemView.findViewById(R.id.layout_editDetails_measuringUnit);
            price = itemView.findViewById(R.id.layout_editDetails_price);

        }
    }
}
