package com.anstudios.freshjoy;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.anstudios.freshjoy.fragments.CartFragment;
import com.anstudios.freshjoy.fragments.HomeFragment;
import com.anstudios.freshjoy.fragments.MyOrdersFragment;
import com.anstudios.freshjoy.fragments.NewsFragment;
import com.anstudios.freshjoy.fragments.PriceList;
import com.anstudios.freshjoy.fragments.SearchFragment;
import com.anstudios.freshjoy.models.Buyers;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS;

public class MainActivity extends AppCompatActivity {

    public static String queryStr;
    public static String fragmentStr, lastScreen;
    private DrawerLayout drawerLayout;
    private ImageView cartBtn;
    private ProgressDialog progressDialog;
    private ImageView profileImage;
    private FragmentManager fragmentManager;
    private long pressedTime;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        setContentView(R.layout.activity_main);
        fragmentStr = "search";
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        lastScreen = "mainActivity";
        fragmentManager = getSupportFragmentManager();
        if (getIntent().getStringExtra("from") != null) {
            fragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.frame_layout, new CartFragment())
                    .commit();
        } else {
            fragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.frame_layout, new HomeFragment())
                    .commit();
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Your data is being uploaded");
        progressDialog.setCanceledOnTouchOutside(false);
        profileImage = findViewById(R.id.frag_account_profileBtn);
        cartBtn = findViewById(R.id.imageView_cart_btn);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new CartFragment())
                        .commit();
            }
        });
//        getImage();


        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, null,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        findViewById(R.id.nav_contact_us_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactUsPage.class));
            }
        });


        findViewById(R.id.nav_cart_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new CartFragment())
                        .commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.nav_news_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new NewsFragment())
                        .commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        findViewById(R.id.nav_price_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new PriceList())
                        .commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.vector_profile_editBtn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dexter.withContext(MainActivity.this)
                                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        Intent i = new Intent();
                                        i.setType("image/*");
                                        i.setAction(Intent.ACTION_GET_CONTENT);

                                        startActivityForResult(Intent.createChooser(i, "Select Your Profile Image"), 121);
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

        TextView nameNav = findViewById(R.id.frag_account_name);
        TextView numberNav = findViewById(R.id.frag_account_number);
        nameNav.setText(SplashScreen.sharedPreferences.getString("name", "John Doe"));
        numberNav.setText(SplashScreen.sharedPreferences.getString("phoneNumber", "(+91) 12345 67890"));

        findViewById(R.id.nav_my_orders_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new MyOrdersFragment())
                        .commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        findViewById(R.id.account_logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = SplashScreen.sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
        findViewById(R.id.nav_change_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog= new BottomSheetDialog(MainActivity.this);
                View customLayout = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_change_language, null);
                bottomSheetDialog.setContentView(customLayout);
                bottomSheetDialog.show();
                CardView english, arhamic, affan;
                affan=customLayout.findViewById(R.id.langauge_affan);
                english=customLayout.findViewById(R.id.language_english);
                arhamic=customLayout.findViewById(R.id.language_arhamic);
                affan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLocale("om_ET");
                        SplashScreen.sharedPreferences.edit().putString("language","om_ET").apply();
                        recreate();
                    }
                });
                english.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLocale("en");
                        SplashScreen.sharedPreferences.edit().putString("language","en").apply();
                        recreate();
                    }
                });
                arhamic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLocale("am");
                        SplashScreen.sharedPreferences.edit().putString("language","am").apply();
                        recreate();
                    }
                });
            }
        });
        findViewById(R.id.nav_home_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new HomeFragment())
                        .commit();
                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        uploadNotificationData();
    }

    BottomSheetDialog bottomSheetDialog;

    private void  setLocale(String language){
        Locale locale=new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

    }
    private void uploadNotificationData() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(@NonNull String s) {
                        Buyers buyers = new Buyers();
                        buyers.setFcmToken(s);
                        Call<Buyers> call = apiInterface.postToken(buyers);
                        call.enqueue(new Callback<Buyers>() {
                            @Override
                            public void onResponse(Call<Buyers> call, Response<Buyers> response) {
                            }

                            @Override
                            public void onFailure(Call<Buyers> call, Throwable t) {
                                Log.d("anshul", call.toString()+ "   "  +t.getMessage());
                            }
                        });
                    }
                });

    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && data != null) {
//            if (requestCode == 121) {
//                try {
//                    Bitmap b = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                    profileImage.setImageBitmap(b);
//                    uploadImageToDb(data.getData());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

//    private void uploadImageToDb(Uri uri) {
//        progressDialog.show();
//        FirebaseStorage.getInstance().getReference("profileImages")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .putFile(uri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                        FirebaseStorage.getInstance().getReference("profileImages")
//                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(@NonNull Uri uri) {
//                                FirebaseFirestore.getInstance().collection("users")
//                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).
//                                        update("profileImage", uri.toString())
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(@NonNull Void aVoid) {
//                                                progressDialog.cancel();
//                                                Toast.makeText(MainActivity.this, "Saved Successfully.", Toast.LENGTH_SHORT).show();
//                                                SplashScreen.sharedPreferences.edit().putString("profileImage", uri.toString()).apply();
//                                            }
//                                        });
//                            }
//                        });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressDialog.cancel();
//                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void getImage() {
//        try {
//            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                FirebaseFirestore.getInstance().collection("users")
//                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
//                        Map<String, Object> hashMap = documentSnapshot.getData();
//                        if (hashMap != null) {
//                            if (hashMap.containsKey("profileImage")) {
//                                Picasso.get().load(hashMap.get("profileImage").toString())
//                                        .placeholder(R.drawable.upload_image_default)
//                                        .into(profileImage);
//                                SplashScreen.sharedPreferences.edit().putString("profileImage", hashMap.get("profileImage").toString())
//                                        .apply();
//                            }
//                        }
//                    }
//                });
//            }
//        } catch (Exception exception) {
//            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }

    @Override
    public void onBackPressed() {
        if (fragmentStr.equals("order")) {
            fragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.frame_layout, new SearchFragment())
                    .commit();
        } else if (fragmentStr.equals("search")) {
            fragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.frame_layout, new HomeFragment())
                    .commit();
        } else if (fragmentStr.equals("home")) {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                finishAffinity();
                System.exit(0);
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        } else if (fragmentStr.equals("cart")) {
            if (lastScreen.equals("category")) {
                startActivity(new Intent(MainActivity.this, CategoryProducts.class));
            } else {
                fragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .replace(R.id.frame_layout, new SearchFragment())
                        .commit();
            }

        }
    }

    public void openNavDrawer(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}