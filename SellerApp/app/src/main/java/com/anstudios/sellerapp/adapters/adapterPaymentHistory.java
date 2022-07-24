package com.anstudios.sellerapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.sellerapp.R;
import com.anstudios.sellerapp.models.Transaction;

import java.util.ArrayList;

public class adapterPaymentHistory extends RecyclerView.Adapter<adapterPaymentHistory.holder> {

    private Context context;
    private ArrayList<Transaction> arrayList;

    public adapterPaymentHistory(Context context, ArrayList<Transaction> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public adapterPaymentHistory.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_payment_history,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull adapterPaymentHistory.holder holder, int position) {
        holder.type.setText(arrayList.get(position).getType().toUpperCase());
        holder.price.setText(arrayList.get(position).getPayout());
        if(arrayList.get(position).getType().equals("credit")){
            holder.title.setText("Payment for Order placed");
            holder.type.setTextColor(Color.parseColor("#00FF00"));
        }else{
            holder.title.setText("Withdrew from wallet");
            holder.type.setTextColor(Color.parseColor("#FF0000"));

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        private TextView title, type, price;
        public holder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.payment_history_title);
            type=itemView.findViewById(R.id.payment_history_type);
            price=itemView.findViewById(R.id.payment_history_price);
        }
    }
}
