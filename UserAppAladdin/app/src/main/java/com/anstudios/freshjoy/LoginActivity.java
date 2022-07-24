package com.anstudios.freshjoy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {

    private CardView loginBtn;
    private EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
        setContentView(R.layout.activity_login);
        Fade slide = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slide = new Fade();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            slide.setDuration(1000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(slide);
        }
        phoneNumber = findViewById(R.id.phoneNumberLoginBx);
        phoneNumber.requestFocus();
        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(v -> {
            if (phoneNumber.getText().toString().length() == 10) {
                Intent intent = new Intent(LoginActivity.this, OtpVerification.class);
                intent.putExtra("phoneNumber", phoneNumber.getText().toString());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Please enter a valid mobile number.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}