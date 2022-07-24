package com.anstudios.freshjoy;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.freshjoy.adapters.adapterOrdersPage;
import com.anstudios.freshjoy.models.Order;
import com.anstudios.freshjoy.models.OrdersProducts;
import com.anstudios.freshjoy.models.Product;
import com.anstudios.freshjoy.models.modelOrders;
import com.anstudios.freshjoy.models.modelOrdersPage;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class OrdersPage extends AppCompatActivity {

    private TextView orderId, dateTime, quantity, paymentType, totalPrice,note, address;
    private RecyclerView recyclerView;
    private ArrayList<modelOrdersPage> arrayList;
    private adapterOrdersPage adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.apptheme));
        }
        setContentView(R.layout.activity_orders_page);
        recyclerView = findViewById(R.id.order_details_recycler);
        arrayList = new ArrayList<>();
        adapter = new adapterOrdersPage(this, arrayList);
        note=findViewById(R.id.orders_page_note);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        orderId = findViewById(R.id.order_details_orderId);
        dateTime = findViewById(R.id.order_details_date);
        quantity = findViewById(R.id.order_details_qty);
        paymentType = findViewById(R.id.order_details_paymentMode);
        totalPrice = findViewById(R.id.order_details_price);
        address = findViewById(R.id.order_details_address);

        try {
            Gson gson = new Gson();
            modelOrders object = gson.fromJson(getIntent().getStringExtra("object"), modelOrders.class);
            String symbol=getResources().getString(R.string.currency_symbol);
            note.setText(object.getNote());

            totalPrice.setText(symbol + object.getTotalPrice());
            if (object.getPaymentType().equals("COD")) {
                paymentType.setText("Pay on Delivery");
            } else {
                paymentType.setText("Paid Online");
            }
            address.setText(object.getAddress());
            dateTime.setText(object.getDateTime());
            orderId.setText(object.getOrderId());
//            HashMap<String, HashMap<String, String>> hashMap = object.getHashMap();
//            HashMap<String, String> child;
            int quantityInt = 0;
//            for (int i = 0; i < hashMap.size(); i++) {
//                child = hashMap.get(hashMap.keySet().toArray()[i]);
//                arrayList.add(new modelOrdersPage(child.get("image"),
//                        child.get("title"), child.get("quantity"), child.get("price")));
//                quantityInt += Integer.parseInt(child.get("quantity"));
//            }
//

            JSONArray jsonArray=object.getItems();
            for(int i=0;i<jsonArray.length();i++){
                String name = jsonArray.get(i).toString().replace("{nameValuePairs={","")
                        .replace("}}","");
                String arr[]=name.split(", ");

                String seller=returnProper(arr[0]);
                String image=returnProper(arr[1]);
                String quantity=returnProper(arr[2]);
                String measuringUnit=returnProper(arr[3]);
                String price=returnProper(arr[4]);
                String title=returnProper(arr[5]);

                arrayList.add(new modelOrdersPage(image, title,
                        quantity, price,measuringUnit, seller));
                quantityInt += Integer.parseInt(quantity);
            }
            adapter.notifyDataSetChanged();
            quantity.setText(quantityInt + "");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

  private String returnProper(String str){
        String temp="";
        String[] arr =str.split("=");
        temp=arr[1];
        return temp;
  }

}