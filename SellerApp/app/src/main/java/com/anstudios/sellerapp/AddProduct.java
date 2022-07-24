package com.anstudios.sellerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anstudios.sellerapp.models.Product;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProduct extends AppCompatActivity {

    private TextView title, moq, price, slashedPrice, description, categoryTxt, measuringUnitTxt;
    private Spinner categorySpinner, measuringUnitSpinner;
    private Uri imagePath;
    private ImageView image;
    private CardView saveBtn;
    private CardView addImageBtn;
    private ApiInterface apiInterface;
    String[] units = {"Kilogram (kg)", "Gram (g)", "Litres (L)", "Packets (pcks)", "Box (box)"
            , "Dozens (dzs)"};
    String encodedimage;


    private void setData() {
        if (getIntent().getStringExtra("title") != null) {
            title.setText(getIntent().getStringExtra("title"));
            description.setText(getIntent().getStringExtra("description"));
            measuringUnitTxt.setText(getIntent().getStringExtra("measuringUnit"));
            price.setText(getIntent().getStringExtra("price"));
            slashedPrice.setText(getIntent().getStringExtra("slashedPrice"));
            categoryTxt.setText(getIntent().getStringExtra("category"));
            moq.setText(getIntent().getStringExtra("moq"));
            Picasso.get().load(getIntent().getStringExtra("image")).into(image);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        title = findViewById(R.id.add_product_title);
        addImageBtn = findViewById(R.id.add_product_addImageBtn);
        description = findViewById(R.id.add_prdouct_description);
        saveBtn = findViewById(R.id.add_product_saveBtn);
        price = findViewById(R.id.add_product_price);
        slashedPrice = findViewById(R.id.add_product_slashedPrice);
        categoryTxt = findViewById(R.id.add_product_category_txt);
        image = findViewById(R.id.add_product_image);
        measuringUnitTxt = findViewById(R.id.add_product_measuringUnit_txt);
        moq = findViewById(R.id.add_product_moq);
        categorySpinner = findViewById(R.id.add_product_category_spinner);
        measuringUnitSpinner = findViewById(R.id.add_product_measuringUnit_spinner);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                units);
        measuringUnitSpinner.setAdapter(ad);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryTxt.setText(categoriesArr[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        measuringUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                measuringUnitTxt.setText(units[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(AddProduct.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent i = new Intent();
                                i.setType("image/*");
                                i.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(i, "Select Your Profile Image"), 111);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        setData();
        setCategorySpinner();
    }

    String[] categoriesArr = null;

    private ArrayList<String> categoryList;

    private void setCategorySpinner() {
        categoryList = new ArrayList<>();
        String url = Constant.baseUrl + "categories/all";
        RequestQueue queue = Volley.newRequestQueue(AddProduct.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("asnhul", response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        categoryList.add(jsonObject.getString("name"));
                    }
                    categoriesArr = new String[categoryList.size()];
                    for (int i = 0; i < categoryList.size(); i++) {
                        categoriesArr[i] = categoryList.get(i);
                    }
                    ArrayAdapter ad
                            = new ArrayAdapter(
                            AddProduct.this,
                            android.R.layout.simple_spinner_item,
                            categoriesArr);
                    categorySpinner.setAdapter(ad);
                } catch (Exception e) {
                    Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("anshul", error.getMessage());
            }

        });

        queue.add(request);
    }

    private boolean isImageChnaged;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Bitmap b = null;
            try {
                b = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                isImageChnaged=true;
                image.setImageBitmap(b);
            } catch (IOException e) {
                e.printStackTrace();
            }
            encodebitmap(b);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadData() {
        Product product = new Product();
        ProgressDialog progressDialog=new ProgressDialog(AddProduct.this);
        progressDialog.setMessage("Your product is being uploaded.");
        progressDialog.show();
        product.setCategory(categoryTxt.getText().toString());
        product.setMeasuringUnit(measuringUnitTxt.getText().toString());
        product.setMoq(Integer.parseInt(moq.getText().toString()));
        product.setPrice(Integer.parseInt(price.getText().toString()));
        product.set_id(getIntent().getStringExtra("_id"));
        product.setSlashedPrice(Integer.parseInt(slashedPrice.getText().toString()));
        product.setDescription(description.getText().toString());
        product.setSeller(SplashScreen.sharedPreferences.getString("id", ""));
        product.setTitle(title.getText().toString());
        product.setImage(encodedimage);
        Call<Product> productCall;
        if(getIntent().getStringExtra("title")==null){
           productCall = apiInterface.addProduct(product);
        }else{
            product.setChanged(isImageChnaged);
            productCall=apiInterface.updateProduct(product);
        }
        productCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                progressDialog.cancel();
                Toast.makeText(AddProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddProduct.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(AddProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void encodebitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] byteofimages = byteArrayOutputStream.toByteArray();
        encodedimage = android.util.Base64.encodeToString(byteofimages, Base64.DEFAULT);

        Product product = new Product();
        product.setImage(encodedimage);

    }

}