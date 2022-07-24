package com.anstudios.sellerapp.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.sellerapp.OrdersPage;
import com.anstudios.sellerapp.R;
import com.anstudios.sellerapp.models.modelOrders;
import com.anstudios.sellerapp.models.modelProducts;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.ArrayList;

public class adapterOrders extends RecyclerView.Adapter<adapterOrders.holder> {
    private Context context;
    private ArrayList<modelOrders> arrayList;

    public adapterOrders(Context context, ArrayList<modelOrders> arrayList) {
        this.context=context;
        this.arrayList=arrayList;

    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_orders,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        try {
            holder.orderId.setText(arrayList.get(position).getOrderId());
            holder.dateTime.setText(arrayList.get(position).getDateTime());
            holder.status.setText(arrayList.get(position).getStatus());
            if (arrayList.get(position).getPaymentType().equals("cod")) {
                holder.paymentType.setText("Pay on Pickup");
            } else {
                holder.paymentType.setText("Paid Online");
            }
            String symbol=context.getResources().getString(R.string.currency_symbol);

            holder.totalPrice.setText(symbol + arrayList.get(position).getTotalPrice());
            holder.nextBnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Gson gson = new Gson();
                    String myJson = gson.toJson(arrayList.get(position));
                    Intent intent = new Intent(context, OrdersPage.class);
                    intent.putExtra("object", myJson);
                    context.startActivity(intent);
                }
            });
        } catch (Exception exceptione) {
            Toast.makeText(context, exceptione.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private TextView status;
        private TextView orderId;
        private TextView paymentType;
        private TextView dateTime, totalPrice;
        private MaterialCardView nextBnt;

        public holder(@NonNull View itemView) {
            super(itemView);
            totalPrice = itemView.findViewById(R.id.layout_orders_price);
            status = itemView.findViewById(R.id.layout_order_status);
            orderId = itemView.findViewById(R.id.layout_orders_orderId);
            paymentType = itemView.findViewById(R.id.layout_orders_paymentType);
            dateTime = itemView.findViewById(R.id.layout_orders_date);
            nextBnt = itemView.findViewById(R.id.order_next_btn);
        }
    }
}
