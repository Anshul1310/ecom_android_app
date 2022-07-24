package com.anstudios.sellerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.sellerapp.AddProduct;
import com.anstudios.sellerapp.R;
import com.anstudios.sellerapp.models.Product;
import com.anstudios.sellerapp.models.modelProducts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adapterProducts extends RecyclerView.Adapter<adapterProducts.holder> {
    private Context context;
    private ArrayList<Product> arrayList;

    public adapterProducts(Context context, ArrayList<Product> arrayList) {
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
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddProduct.class);
                intent.putExtra("title", arrayList.get(position).getTitle());
                intent.putExtra("image", arrayList.get(position).getImage());
                intent.putExtra("moq", arrayList.get(position).getMoq()+"");
                intent.putExtra("_id", arrayList.get(position).get_id());
                intent.putExtra("description", arrayList.get(position).getDescription());
                intent.putExtra("measuringUnit", arrayList.get(position).getMeasuringUnit()+"");
                intent.putExtra("category", arrayList.get(position).getCategory());
                intent.putExtra("price", arrayList.get(position).getPrice()+"");
                intent.putExtra("slashedPrice", arrayList.get(position).getSlashedPrice()+"");
                context.startActivity(intent);
            }
        });
        holder.measuringUnit.setText("Price 1 " + arrayList.get(position).getMeasuringUnit() + "");
        holder.title.setText(arrayList.get(position).getTitle());
        holder.price.setText(arrayList.get(position).getPrice() + "");
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;
        private TextView price;
        private ImageView editBtn;
        private TextView measuringUnit;

        public holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.layout_orders_image);
            title = itemView.findViewById(R.id.layout_orders_title);
            price = itemView.findViewById(R.id.layout_orders_price);
            editBtn = itemView.findViewById(R.id.layout_orders_editBtn);
            measuringUnit = itemView.findViewById(R.id.layout_order_subtitle);

        }
    }
}
