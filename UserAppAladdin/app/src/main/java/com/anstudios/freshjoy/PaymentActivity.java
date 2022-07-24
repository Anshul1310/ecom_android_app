package com.anstudios.freshjoy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.freshjoy.models.Order;
import com.anstudios.freshjoy.models.modelOrders;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private RadioButton codRadio;
    private TextView address;
    private MaterialCardView codCard;
    private EditText note;
    private MaterialCardView backBtn, nextStepBtn;
    private modelOrders object;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        setContentView(R.layout.activity_payment);
        Gson gson = new Gson();
        note = findViewById(R.id.payments_note);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        object = gson.fromJson(getIntent().getStringExtra("object"), modelOrders.class);

        backBtn = findViewById(R.id.payment_back_btn);
        nextStepBtn = findViewById(R.id.payment_next_btn);
        codCard = findViewById(R.id.payment_cod_card);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codRadio.isChecked()) {
                    object.setPaymentType("cod");
//                    FirebaseFirestore.getInstance().collection("adminSettings")
//                            .document("lastOrderIndex")
//                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
//                            try {
//                                String lastOrderIndex = (String) documentSnapshot.getData().get("lastOrderIndex");
//                                HashMap<String, String> lOI = new HashMap<>();
//                                lOI.put("lastOrderIndex", ((Integer.parseInt(lastOrderIndex) + 1) + ""));
//                                object.setOrderId("Order Id : #" + lastOrderIndex);
//                                FirebaseFirestore.getInstance().collection("adminSettings")
//                                        .document("lastOrderIndex")
//                                        .set(lOI).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(@NonNull Void aVoid) {
//                                        String uuid = UUID.randomUUID().toString();
//
////                                        FirebaseFirestore.getInstance().collection("orders")
////                                                .document(uuid).set(object)
////                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                                                    @Override
////                                                    public void onSuccess(@NonNull Void aVoid) {
////                                                        Toast.makeText(PaymentActivity.this, "Thanks for placing an order with us.", Toast.LENGTH_SHORT).show();
////                                                        startActivity(new Intent(PaymentActivity.this, OrderPlaced.class));
////                                                    }
////                                                });
//                                    }
//                                });
//                            } catch (Exception exception) {
//                                Toast.makeText(PaymentActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
                    Order order = new Order();
                    order.setBuyer(SplashScreen.sharedPreferences.getString("id", ""));
                    order.setNote(note.getText().toString());
                    Object[] values = object.getHashMap().values().toArray(new Object[0]);
                    order.setItems(values);
                    order.setPhone(object.getPhoneNumber());
                    order.setAddress(object.getAddress());
                    order.setTotalPrice(object.getTotalPrice());
                    order.setStatus("dispatched");
                    Call<Order> call = apiInterface.addOrder(order);
                    call.enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            startActivity(new Intent(PaymentActivity.this, OrderPlaced.class));
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        codRadio = findViewById(R.id.cod_radio);
        address = findViewById(R.id.billing_adress_payment);
        address.setText(SplashScreen.sharedPreferences.getString("address", ""));


    }

    @Override
    public void onPaymentSuccess(String s) {
        String uuid = UUID.randomUUID().toString();
        FirebaseFirestore.getInstance().collection("orders")
                .document(uuid).set(object)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void aVoid) {
                        Toast.makeText(PaymentActivity.this, "Order Placed Successfully.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentActivity.this, OrderPlaced.class));

                    }
                });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Order Failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
        intent.putExtra("from", "paymentActivity");
        startActivity(intent);
    }
}