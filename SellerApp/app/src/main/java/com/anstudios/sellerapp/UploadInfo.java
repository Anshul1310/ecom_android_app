package com.anstudios.sellerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anstudios.sellerapp.adapters.adapterShopType;
import com.anstudios.sellerapp.models.Seller;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadInfo extends AppCompatActivity {

    private RecyclerView recyclerView;
    private adapterShopType adapter;
    private ArrayList<String> name;
//    private FusedLocationProviderClient fusedLocationProviderClient;

    private ArrayList<Integer> image;
    private CardView continueBtn;
    private EditText ownerName, phoneNumber, email,region, tin,woreda, zone, address, bookNumber;
    private HashMap<String, String> hashMap;
    private ProgressDialog progressDialog;
    private TextView locateBtn;
    private MaterialCheckBox materialCheckBox;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_info);
        woreda=findViewById(R.id.upload_info_worede);
        recyclerView = findViewById(R.id.shopTypeRecycler);
        zone=findViewById(R.id.upload_info_zone);
        region=findViewById(R.id.upload_info_region);
        progressDialog = new ProgressDialog(this);
        bookNumber = findViewById(R.id.upload_info_book_number);
        tin = findViewById(R.id.upload_info_tin);
        email = findViewById(R.id.upload_info_email);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        progressDialog.setTitle("Please wait..");
        locateBtn = findViewById(R.id.upload_info_locate_btn);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("We are uploading your data to our store.");
        continueBtn = findViewById(R.id.upload_info_continue_btn);
        name = new ArrayList<>(Arrays.asList("Supermarkets", "Vegetable Store", "Kirana Store", "Push Cart", "Restaurant", "Other"));
        image = new ArrayList<>(Arrays.asList(R.drawable.vector_material_supermarket, R.drawable.vector_material_vegetable_store,
                R.drawable.vector_material_kirana_store, R.drawable.vector_material_pushcart
                , R.drawable.vector_matrial_restaurant, R.drawable.vector_materia_other));
        materialCheckBox = findViewById(R.id.upload_info_agree_tandc);
        ownerName = findViewById(R.id.upload_info_name);
        phoneNumber = findViewById(R.id.upload_info_contact_num);
        address = findViewById(R.id.upload_info_address);
        hashMap = new HashMap<>();

        locateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation("locate");
            }
        });

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        getMyLocation("post");
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        recyclerView.setLayoutManager(new GridLayoutManager(UploadInfo.this, 2));
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new adapterShopType(UploadInfo.this, name, image);
        recyclerView.setAdapter(adapter);
        phoneNumber.setText(SplashScreen.sharedPreferences.getString("phoneNumber", ""));


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDetails()) {
                    seller = new Seller();
                    seller.setName(ownerName.getText().toString());
                    seller.setPhone(Double.parseDouble(phoneNumber.getText().toString()));
                    seller.setStatus("pending");
                    seller.setZone(zone.getText().toString());
                    seller.setRegion(region.getText().toString());
                    seller.setWoreda(woreda.getText().toString());
                    seller.setType(SplashScreen.sharedPreferences.getString("shopType", "SuperMarket"));
                    seller.setBookNumber(bookNumber.getText().toString());
                    seller.setTin(tin.getText().toString());
                    seller.setEmail(email.getText().toString());
                    uploadDetails();
                }
            }
        });
    }

    private void uploadDetails(){
        Call<Seller> call ;
        if(SplashScreen.sharedPreferences.getString("id","").equals("")){
            call=apiInterface.createSeller(seller);
        }else{
            seller.set_id(SplashScreen.sharedPreferences.getString("id",""));
            call=apiInterface.updateSeller(seller);
        }
        call.enqueue(new Callback<Seller>() {
            @Override
            public void onResponse(Call<Seller> call, Response<Seller> response) {
                progressDialog.cancel();
                try{
                    SplashScreen.sharedPreferences.edit().putString("address", address.getText().toString()).apply();
                    SplashScreen.sharedPreferences.edit().putString("id",response.body().get_id()).apply();
                    SplashScreen.sharedPreferences.edit().putString("status","pending").apply();

                    SplashScreen.sharedPreferences.edit().putString("address", address.getText().toString()).apply();
                    SplashScreen.sharedPreferences.edit().putString("name", ownerName.getText().toString()).apply();


                    Toast.makeText(UploadInfo.this, "Uploaded Successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UploadInfo.this, ThankYou.class));
                    finish();
                }catch(Exception e){
                    Toast.makeText(UploadInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {

            }
        });
    }


    Seller seller;

    private boolean checkDetails() {
        boolean isAllCorrect = true;
        if (ownerName.getText().toString().equals("")) {
            isAllCorrect = false;
            Toast.makeText(this, "Please fill the owner name.", Toast.LENGTH_SHORT).show();
        } else if (address.getText().toString().equals("")) {
            isAllCorrect = false;
            Toast.makeText(this, "Please fill the Adress of the shop.", Toast.LENGTH_SHORT).show();
        } else if (bookNumber.getText().toString().equals("")) {
            isAllCorrect = false;
            Toast.makeText(this, "Please fill Book Name.", Toast.LENGTH_SHORT).show();
        } else if (!materialCheckBox.isChecked()) {
            isAllCorrect = false;
            Toast.makeText(this, "Please agree to Terms & Conditions", Toast.LENGTH_SHORT).show();
        } else if (SplashScreen.sharedPreferences.getString("shopType", "null").equals("null")) {
            isAllCorrect = false;
            Toast.makeText(this, "Please select a shop type.", Toast.LENGTH_SHORT).show();
        }
        return isAllCorrect;
    }

    private void getMyLocation(String locate) {
    }
}