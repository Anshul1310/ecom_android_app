package com.anstudios.freshjoy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anstudios.freshjoy.adapters.adapterShopType;
import com.anstudios.freshjoy.models.Buyers;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadInfo extends AppCompatActivity {

    private RecyclerView recyclerView;
    private adapterShopType adapter;
    private ArrayList<String> name;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ArrayList<Integer> image;
    private CardView continueBtn;
    private EditText ownerName, phoneNumber,email, tin,contact_person, address, businessName, landmark, pincode;
    private HashMap<String, String> hashMap;
    private ProgressDialog progressDialog;
    private TextView locateBtn;
    private MaterialCheckBox materialCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_info);
        recyclerView = findViewById(R.id.shopTypeRecycler);
        progressDialog = new ProgressDialog(this);

        contact_person=findViewById(R.id.upload_info_contact_person);
        tin=findViewById(R.id.upload_info_tin);
        email=findViewById(R.id.upload_info_email);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        progressDialog.setTitle("Please wait..");
        locateBtn = findViewById(R.id.upload_info_locate_btn);
        progressDialog.setCanceledOnTouchOutside(false);
        businessName=findViewById(R.id.upload_info_business_name);
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getMyLocation("post");
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
                    buyers = new Buyers();
                    buyers.setName(ownerName.getText().toString());
                    buyers.setPhone(Double.parseDouble(phoneNumber.getText().toString()));
                    buyers.setAddress(address.getText().toString());
                    buyers.setStatus("pending");

                    buyers.setType(SplashScreen.sharedPreferences.getString("shopType","SuperMarket"));
                    buyers.setOrganization(businessName.getText().toString());
                    buyers.setTin(tin.getText().toString());
                    buyers.setContact_person(contact_person.getText().toString());
                    buyers.setEmail(email.getText().toString());
                    uploadDataToDb();
                }
            }
        });
    }

    private Buyers buyers;
    private void getMyLocation(String use) {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(this),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    if (location != null) {
                        if (use.equals("post")) {
                            hashMap.put("latitude", location.getLatitude() + "");
                            hashMap.put("longitude", location.getLongitude() + "");
                        } else {
                            address.setText(getGivenAddress(location.getLatitude(),
                                    location.getLongitude()));
                        }

                    }
                }
            });
        }
    }

    private void uploadDataToDb() {
        Call<Buyers> call ;
        if(SplashScreen.sharedPreferences.getString("id","").equals("")){
            call=apiInterface.createBuyer(buyers);
        }else{
            buyers.set_id(SplashScreen.sharedPreferences.getString("id",""));
            call=apiInterface.updateBuyer(buyers);
        }
        call.enqueue(new Callback<Buyers>() {
            @Override
            public void onResponse(Call<Buyers> call, Response<Buyers> response) {

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
            public void onFailure(Call<Buyers> call, Throwable t) {

            }
        });
    }

    private ApiInterface apiInterface;

    private String getGivenAddress(double latitude, double longitude) {
        String myaddress;
        Geocoder geocoder = new Geocoder(UploadInfo.this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            Address returnaddress = addresses.get(0);
            myaddress = returnaddress.getAddressLine(0);
        } else {
            myaddress = "";
        }
        return myaddress;
    }


    private boolean checkDetails() {
        boolean isAllCorrect = true;
        if (ownerName.getText().toString().equals("")) {
            isAllCorrect = false;
            Toast.makeText(this, "Please fill the owner name.", Toast.LENGTH_SHORT).show();
        } else if (address.getText().toString().equals("")) {
            isAllCorrect = false;
            Toast.makeText(this, "Please fill the Adress of the shop.", Toast.LENGTH_SHORT).show();
        }
        else if(businessName.getText().toString().equals("")){
            isAllCorrect=false;
            Toast.makeText(this, "Please fill Business Name.", Toast.LENGTH_SHORT).show();
        }
        else if (!materialCheckBox.isChecked()) {
            isAllCorrect = false;
            Toast.makeText(this, "Please agree to Terms & Conditions", Toast.LENGTH_SHORT).show();
        } else if (SplashScreen.sharedPreferences.getString("shopType", "null").equals("null")) {
            isAllCorrect = false;
            Toast.makeText(this, "Please select a shop type.", Toast.LENGTH_SHORT).show();
        }
        return isAllCorrect;
    }


}