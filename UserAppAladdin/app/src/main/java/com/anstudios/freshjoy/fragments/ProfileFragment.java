package com.anstudios.freshjoy.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.anstudios.freshjoy.LoginActivity;
import com.anstudios.freshjoy.MainActivity;
import com.anstudios.freshjoy.R;
import com.anstudios.freshjoy.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private View view;
    private TextView name, phoneNumber, address, mainName;
    private CardView logoutBtn;
    private CircleImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.profile_name);
        mainName = view.findViewById(R.id.frag_profile_name);
        MainActivity.fragmentStr = "profile";
        profileImage = view.findViewById(R.id.frag_profile_image);
        phoneNumber = view.findViewById(R.id.profile_phone_number);
        address = view.findViewById(R.id.profile_address);
        logoutBtn = view.findViewById(R.id.profile_logout_btn);
        mainName.setText(SplashScreen.sharedPreferences.getString("name", "John Doe"));
        if (SplashScreen.sharedPreferences.getString("profileImage", "").equals("")) {
            profileImage.setImageResource(R.drawable.app_logo_main);
        } else {
            Picasso.get().load(SplashScreen.sharedPreferences.getString("profileImage", ""))
                    .error(R.drawable.app_logo_main)
                    .placeholder(R.drawable.upload_image_default)
                    .into(profileImage);
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences.Editor editor = SplashScreen.sharedPreferences.edit();
                editor.clear();
                editor.apply();
                getActivity().finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        phoneNumber.setText(SplashScreen.sharedPreferences.getString("phoneNumber", "(+91) 6395390703"));
        address.setText(SplashScreen.sharedPreferences.getString("address", ""));
        name.setText(SplashScreen.sharedPreferences.getString("name", ""));
        return view;
    }
}