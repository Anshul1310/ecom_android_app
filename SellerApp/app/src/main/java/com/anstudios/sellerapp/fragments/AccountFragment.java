package com.anstudios.sellerapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anstudios.sellerapp.ApiClient;
import com.anstudios.sellerapp.ApiInterface;
import com.anstudios.sellerapp.R;
import com.anstudios.sellerapp.SplashScreen;
import com.anstudios.sellerapp.models.Password;
import com.anstudios.sellerapp.models.Product;
import com.anstudios.sellerapp.models.Seller;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private View view;
    private TextView id, changePasswordBtn;
    private ApiInterface apiInterface;

    private EditText phone, name, type,region, zone, level, tin, bookNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        phone = view.findViewById(R.id.account_phone);
        level = view.findViewById(R.id.account_level);
        region=view.findViewById(R.id.account_region);
        zone=view.findViewById(R.id.account_zone);

        id = view.findViewById(R.id.account_id);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        changePasswordBtn = view.findViewById(R.id.account_password);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                final View customLayout = getLayoutInflater().inflate(R.layout.layout_change_password, null);
                alertDialog.setView(customLayout);

                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(false);
                alert.show();
                EditText newPassword, oldPassword;
                newPassword = customLayout.findViewById(R.id.change_password_new_password);
                oldPassword = customLayout.findViewById(R.id.change_password_old_password);

                CardView cardView = customLayout.findViewById(R.id.change_password_btn);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Password password = new Password(oldPassword.getText().toString(), newPassword.getText().toString(), SplashScreen.sharedPreferences.getString("id", ""));

                        Call<Product> call = apiInterface.updatePassword(password);

                        call.enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                if (response.code() == 200) {
                                    Toast.makeText(getContext(), "Successfully Changed", Toast.LENGTH_SHORT).show();
                                    alert.cancel();

                                } else {
                                    Toast.makeText(getContext(), "Please enter correct Old Password", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
                                Toast.makeText(getContext(), "Wrong Old Password entered", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

            }
        });
        type = view.findViewById(R.id.account_type);
        tin = view.findViewById(R.id.account_tin);
        bookNumber = view.findViewById(R.id.account_book_number);
        name = view.findViewById(R.id.account_name);

        phone.setText(SplashScreen.sharedPreferences.getString("phone", ""));
        level.setText(SplashScreen.sharedPreferences.getString("level", ""));
        type.setText(SplashScreen.sharedPreferences.getString("type", ""));
        tin.setText(SplashScreen.sharedPreferences.getString("tin", ""));
        zone.setText(SplashScreen.sharedPreferences.getString("zone","North Ethipia"));
        region.setText(SplashScreen.sharedPreferences.getString("region","Chamba North"));
        id.setText(SplashScreen.sharedPreferences.getString("id", ""));
        name.setText(SplashScreen.sharedPreferences.getString("name", ""));
        bookNumber.setText(SplashScreen.sharedPreferences.getString("bookNumber", ""));
        return view;
    }
}