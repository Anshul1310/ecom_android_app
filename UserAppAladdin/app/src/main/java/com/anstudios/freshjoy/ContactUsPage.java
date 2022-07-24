package com.anstudios.freshjoy;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ContactUsPage extends AppCompatActivity {

    private TextView number, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_eee));
        }
        setContentView(R.layout.activity_contact_us_page);
        number=findViewById(R.id.contact_us_contactNumber);
        email=findViewById(R.id.contact_us_email);
        number.setText("Contact Number : "+getString(R.string.phone_number));
        email.setText("Contact Number : "+getString(R.string.email));
    }
}