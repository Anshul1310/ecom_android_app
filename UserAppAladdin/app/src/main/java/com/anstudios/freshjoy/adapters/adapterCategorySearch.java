package com.anstudios.freshjoy.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.freshjoy.CategoryProducts;
import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.models.HashmapSaver;
import com.anstudios.freshjoy.models.modelSearch;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class adapterCategorySearch extends RecyclerView.Adapter<adapterCategorySearch.holder> {

    private ArrayList<modelSearch> arrayList;
    private Context context;

    public adapterCategorySearch(ArrayList<modelSearch> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(context).inflate(R.layout.layout_products_horizontal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        try {
            String symbol=context.getResources().getString(R.string.currency_symbol);

            Picasso.get().load(arrayList.get(position).getImage())
                    .placeholder(R.drawable.upload_image_default).into(holder.imageView);
            holder.title.setText(arrayList.get(position).getTitle());
            holder.realPrice.setText(symbol + arrayList.get(position).getPrice() + "/" + returnAcronym(arrayList.get(position).getMeasuringUnit()));
            holder.slashedPrice.setText("MRP: " + arrayList.get(position).getSlashedPrice());
            holder.slashedPrice.setPaintFlags(holder.slashedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (isProductInCart(arrayList.get(position).getProductId())) {
                holder.constraintLayout.setVisibility(View.VISIBLE);

                holder.quantity.setText(new HashmapSaver().getHashMap("cart").get(arrayList.get(position).getProductId()));
            } else {
                holder.quantity.setText(arrayList.get(position).getMoq());
            }
            holder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    SplashScreen.sharedPreferences.edit()
//                            .putString(arrayList.get(position).getProductId(),
//                                    holder.moq.getText().toString().replace("MOQ:", "")
//                                            .replace(" ", "").replace(arrayList.get(position).getMeasuringUnit(), "")).apply();
//                    HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
//                    HashMap<String, String> price = new HashmapSaver().getHashMap("price_cart");
//                    if (price == null) {
//                        price = new HashMap<>();
//                    }
//                    if (hashMap == null) {
//                        hashMap = new HashMap<>();
//                    }
//                    int units = Integer.parseInt(holder.moq.getText().toString().replace("MOQ:", "")
//                            .replace(" ", "").replace(arrayList.get(position).getMeasuringUnit(), ""));
//                    price.put(arrayList.get(position).getProductId(), (Integer.parseInt(arrayList.get(position).getPrice()) * units) + "");
//                    hashMap.put(arrayList.get(position).getProductId(), holder.moq.getText().toString().replace("MOQ:", "")
//                            .replace(" ", "").replace(arrayList.get(position).getMeasuringUnit(), ""));
//                    new HashmapSaver().saveHashMap("cart", hashMap);
//                    new HashmapSaver().saveHashMap("price_cart", price);
//                    calData();
                    holder.constraintLayout.setVisibility(View.VISIBLE);
                    HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
                    HashMap<String, String> price = new HashmapSaver().getHashMap("price_cart");
                    if (price == null) {
                        price = new HashMap<>();
                    }
                    if (hashMap == null) {
                        hashMap = new HashMap<>();
                    }
                    holder.totalPrice.setText(symbol+ (Integer.parseInt(arrayList.get(position).getPrice())
                            * Integer.parseInt(holder.quantity.getText().toString())) + "");
                    price.put(arrayList.get(position).getProductId(), arrayList.get(position).getPrice());
                    hashMap.put(arrayList.get(position).getProductId(), holder.quantity.getText().toString());
                    new HashmapSaver().saveHashMap("cart", hashMap);
                    new HashmapSaver().saveHashMap("price_cart", price);
                    Log.d("anshul", String.valueOf(new HashmapSaver().getHashMap("cart")));

                    calData();
//                    holder
//                    holder.quantity.setText(units);

//                    Toast.makeText(context, "Added Successfully.", Toast.LENGTH_SHORT).show();
                }
            });

            holder.plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int moqInt = Integer.parseInt(arrayList.get(position).getMoq());
                    int maxQuantity = Integer.parseInt(arrayList.get(position).getMoq()) * 5;
                    int curQuantity = Integer.parseInt(holder.quantity.getText().toString());
                    if (curQuantity < maxQuantity) {
                        holder.quantity.setText((curQuantity + moqInt) + "");
                        HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
                        HashMap<String, String> price = new HashmapSaver().getHashMap("price_cart");
                        if (price == null) {
                            price = new HashMap<>();
                        }
                        if (hashMap == null) {
                            hashMap = new HashMap<>();
                        }
                        holder.totalPrice.setText(symbol + (Integer.parseInt(arrayList.get(position).getPrice())
                                * Integer.parseInt(holder.quantity.getText().toString())) + "");
                        price.put(arrayList.get(position).getProductId(), arrayList.get(position).getPrice());
                        hashMap.put(arrayList.get(position).getProductId(), holder.quantity.getText().toString());
                        new HashmapSaver().saveHashMap("cart", hashMap);
                        new HashmapSaver().saveHashMap("price_cart", price);
                        calData();
                    }
                }
            });

            holder.totalPrice.setText((symbol) + (Integer.parseInt(arrayList.get(position).getPrice())
                    * Integer.parseInt(arrayList.get(position).getMoq())) + "");

            holder.minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int moqInt = Integer.parseInt(arrayList.get(position).getMoq());
                    int curQuantity = Integer.parseInt(holder.quantity.getText().toString());
                    if (curQuantity != moqInt) {
                        holder.quantity.setText((curQuantity - moqInt) + "");
                        HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
                        HashMap<String, String> price = new HashmapSaver().getHashMap("price_cart");
                        holder.totalPrice.setText(symbol + (Integer.parseInt(arrayList.get(position).getPrice())
                                * Integer.parseInt(holder.quantity.getText().toString())) + "");
                        if (price == null) {
                            price = new HashMap<>();
                        }
                        if (hashMap == null) {
                            hashMap = new HashMap<>();
                        }
                        price.put(arrayList.get(position).getProductId(), arrayList.get(position).getPrice());
                        hashMap.put(arrayList.get(position).getProductId(), holder.quantity.getText().toString());
                        new HashmapSaver().saveHashMap("cart", hashMap);
                        new HashmapSaver().saveHashMap("price_cart", price);
                        calData();
                    } else if (curQuantity == moqInt) {
                        holder.constraintLayout.setVisibility(View.GONE);
                        HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
                        HashMap<String, String> price = new HashmapSaver().getHashMap("price_cart");
                        if (hashMap != null) {
                            if (hashMap.containsKey(arrayList.get(position).getProductId()) && price.containsKey(arrayList.get(position).getProductId())) {
                                hashMap.remove(arrayList.get(position).getProductId());
                                price.remove(arrayList.get(position).getProductId());
                            }
                        }

                        new HashmapSaver().saveHashMap("cart", hashMap);
                        new HashmapSaver().saveHashMap("price_cart", price);
                        calData();
                    }
                }
            });
            holder.moq.setText("MOQ: " + arrayList.get(position).getMoq() + returnAcronym(arrayList.get(position).getMeasuringUnit()));
        } catch (Exception exception) {
            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void calData() {
        try {
            HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
            HashMap<String, String> price = new HashmapSaver().getHashMap("price_cart");
            if (hashMap != null) {
                int num = 0;
                int unitPrice = 0;
                Set<String> hashset = hashMap.keySet();
                for (int i = 0; i < hashMap.size(); i++) {
                    num += Integer.parseInt(hashMap.get(hashset.toArray()[i]));
                    unitPrice += Integer.parseInt(price.get(hashset.toArray()[i])) * Integer.parseInt(hashMap.get(hashset.toArray()[i]));
                }
                CategoryProducts.cartItems.setText(num + " Items");
                String symbol=context.getResources().getString(R.string.currency_symbol);

                CategoryProducts.cartItemsPrice.setText(symbol + unitPrice);
            }
        } catch (Exception exception) {
            Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String returnAcronym(String unit) {
        String temp = "";
        if (unit.toLowerCase().equals("kilogram")) {
            temp = "Kg";
        } else if (unit.toLowerCase().equals("dozen")) {
            temp = "Doz";
        } else if (unit.toLowerCase().equals("pieces")) {
            temp = "Pcs";
        } else if (unit.toLowerCase().equals("litres") || unit.toLowerCase().equals("litre")) {
            temp = "LTR";
        } else if (unit.toLowerCase().equals("packets") || unit.toLowerCase().equals("packet")) {
            temp = "Pckt";
        } else {
            temp = unit;
        }
        return temp;
    }

    private boolean isProductInCart(String string) {
        boolean temp = false;
        HashMap<String, String> hashMap = new HashmapSaver().getHashMap("cart");
        if (hashMap != null) {
            if (hashMap.containsKey(string)) {
                temp = true;
            }
        }

        return temp;
    }

    public class holder extends RecyclerView.ViewHolder {
        private TextView title, moq, slashedPrice, realPrice;
        private MaterialCardView addBtn;
        private CardView plusBtn, minusBtn;
        private TextView quantity;
        private LinearLayout constraintLayout;
        private ImageView imageView;
        private TextView totalPrice;

        public holder(@NonNull View itemView) {
            super(itemView);
            totalPrice = itemView.findViewById(R.id.product_horizontal_totalPrice);
            minusBtn = itemView.findViewById(R.id.cart_minus);
            plusBtn = itemView.findViewById(R.id.cart_add);
            quantity = itemView.findViewById(R.id.cart_quantityl);
            constraintLayout = itemView.findViewById(R.id.mainLayoutVisisblity);
            addBtn = itemView.findViewById(R.id.search_add_btn);
            imageView = itemView.findViewById(R.id.search_product_image);
            slashedPrice = itemView.findViewById(R.id.search_slashed_price);
            title = itemView.findViewById(R.id.search_title);
            moq = itemView.findViewById(R.id.search_moq);
            realPrice = itemView.findViewById(R.id.search_price);
        }
    }


}
