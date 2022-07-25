package com.anstudios.freshjoy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.SplashScreen;
import com.anstudios.freshjoy.fragments.CartFragment;
import com.anstudios.freshjoy.models.HashmapSaver;
import com.anstudios.freshjoy.models.modelCart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class adapterCart extends RecyclerView.Adapter<adapterCart.holder> {

    int pos;
    private Context context;
    private ArrayList<modelCart> arrayList;

    public adapterCart(Context context, ArrayList<modelCart> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        alterValues();
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        try {
            int moq = Integer.parseInt(arrayList.get(position).getMoq());
            String[] quantity = {(moq + ""), ((moq * 2) + ""), ((moq * 3) + ""), ((moq * 4) + ""), ((moq * 5) + "")};
            final int[] cur = {0};
            for (int i = 0; i < 5; i++) {
                if (quantity[i].equals(arrayList.get(position).getQuantity())) {
                    cur[0] = i;
                }
            }
            Picasso.get().load(arrayList.get(position).getImage())
                    .placeholder(R.drawable.upload_image_default).into(holder.cart_image);
            holder.quantity.setText(arrayList.get(position).getQuantity());
            pos = Integer.parseInt(arrayList.get(position).getQuantity()) /
                    Integer.parseInt(arrayList.get(position).getMoq());

            holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
                    hashMap.remove(arrayList.get(position).getProductId());
                    new HashmapSaver().saveHashMap("cart", hashMap);
                    CartFragment.arrayListCart.remove(position);
                    notifyDataSetChanged();
                    alterValues();
                }
            });
            holder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cur[0] < 4) {
                        try {
                            cur[0]++;
                            holder.quantity.setText(quantity[cur[0]]);
                            String symbol = context.getResources().getString(R.string.currency_symbol);

                            holder.price.setText(symbol + (Integer.parseInt(quantity[cur[0]]) *
                                    Integer.parseInt(arrayList.get(position).getPrice())) + "");
                            SplashScreen.sharedPreferences.edit().putString(arrayList.get(position).getProductId(),
                                    quantity[cur[0]]).apply();
                            arrayList.get(position).setQuantity(quantity[cur[0]]);
                            CartFragment.arrayListCart.get(position).setQuantity(quantity[cur[0]]);
                            HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
                            hashMap.put(arrayList.get(position).getProductId(), holder.quantity.getText().toString());
                            new HashmapSaver().saveHashMap("cart", hashMap);

                            alterValues();
                        } catch (Exception exception) {
                            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
            holder.minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cur[0] > 0) {
                        cur[0]--;
                        holder.quantity.setText(quantity[cur[0]]);
                        String symbol = context.getResources().getString(R.string.currency_symbol);

                        holder.price.setText(symbol + (Integer.parseInt(quantity[cur[0]]) *
                                Integer.parseInt(arrayList.get(position).getPrice())) + "");
                        SplashScreen.sharedPreferences.edit().putString(arrayList.get(position).getProductId(),
                                quantity[cur[0]]).apply();
                        arrayList.get(position).setQuantity(quantity[cur[0]]);
                        CartFragment.arrayListCart.get(position).setQuantity(quantity[cur[0]]);
                        HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
                        hashMap.put(arrayList.get(position).getProductId(), holder.quantity.getText().toString());
                        new HashmapSaver().saveHashMap("cart", hashMap);
                        alterValues();
                    }
                }
            });
            holder.title.setText(arrayList.get(position).getTitle());
            String symbol = context.getResources().getString(R.string.currency_symbol);

            holder.price.setText(symbol + (Integer.parseInt(quantity[cur[0]]) *
                    Integer.parseInt(arrayList.get(position).getPrice())) + "");
        } catch (Exception exception) {
            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void alterValues() {
        int tp = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            tp += Integer.parseInt(arrayList.get(i).getPrice())
                    * Integer.parseInt(arrayList.get(i).getQuantity());
        }
        CartFragment.subtotal.setText("Rs." + tp + "");
        int stInt = Integer.parseInt(CartFragment.subtotal.getText().toString()
                .replace(" ", "").replace("Rs", "")
                .replace(".00", "")
                .replace(".", "")
        );
//            CartFragment.totalPrice.setText(("Rs.") + Math.ceil((deleiveryChargeInt + stInt) - realOff) + "");

        double serviceCharge=(CartFragment.rate/100)*stInt;
        CartFragment.deliveryCharge.setText(Math.ceil(serviceCharge)+"");
        double total=serviceCharge+stInt;
        CartFragment.totalPrice.setText(Math.ceil(total)+"");
        if (arrayList.size() == 0) {
            CartFragment.totalPrice.setText("0.00");

        }


    }

    public class holder extends RecyclerView.ViewHolder {
        private ImageView cart_image;
        private TextView quantity;
        private CardView addBtn, minusBtn;
        private TextView price, title;
        private ImageView removeBtn;

        public holder(@NonNull View itemView) {
            super(itemView);
            cart_image = itemView.findViewById(R.id.cart_image_main);
            quantity = itemView.findViewById(R.id.cart_quantityl);
            addBtn = itemView.findViewById(R.id.cart_add);
            removeBtn = itemView.findViewById(R.id.cart_remove_btn);
            minusBtn = itemView.findViewById(R.id.cart_minus);
            title = itemView.findViewById(R.id.cart_title);
            price = itemView.findViewById(R.id.cart_item_price);
        }
    }


}
