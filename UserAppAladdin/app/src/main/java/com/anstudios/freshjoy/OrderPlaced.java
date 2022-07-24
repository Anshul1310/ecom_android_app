package com.anstudios.freshjoy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class OrderPlaced extends AppCompatActivity {

    private CardView orderPlaced;

    private TextView query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        Window window = this.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audio_order_paced);
        mediaPlayer.start();
        orderPlaced = findViewById(R.id.go_to_home_btn);
        query=findViewById(R.id.order_placed_contact_us);
        query.setText(String.format("Please feel free to contact us on %s if you have any questions or concerns.", getResources().getString(R.string.phone_number)));
        orderPlaced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderPlaced.this, MainActivity.class));
                finish();
            }
        });
    }
}