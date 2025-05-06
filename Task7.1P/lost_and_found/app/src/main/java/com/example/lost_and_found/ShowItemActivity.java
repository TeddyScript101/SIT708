package com.example.lost_and_found;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

public class ShowItemActivity extends AppCompatActivity {

    private AdvertDao advertDao;
    private LinearLayout itemsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        advertDao = Room.databaseBuilder(getApplicationContext(),
                        AdvertDatabase.class, "advert_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .advertDao();

        itemsContainer = findViewById(R.id.items_container);
        Button btnReturnHome = findViewById(R.id.btn_return_home);
        btnReturnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ShowItemActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdverts();
    }

    private void loadAdverts() {
        itemsContainer.removeAllViews();  // Clear previous items

        List<Advert> adverts = advertDao.getAllAdverts();

        for (Advert advert : adverts) {
            TextView itemView = new TextView(this);
            itemView.setText(advert.getType() + " - " + advert.getName());
            itemView.setPadding(24, 24, 24, 24);
            itemView.setBackgroundResource(R.drawable.item_border);
            itemView.setTextSize(16);
            itemView.setClickable(true);
            itemView.setFocusable(true);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdvertDetailActivity.class);
                intent.putExtra("advertId", advert.getId());
                startActivity(intent);
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(16, 8, 16, 8);
            itemView.setLayoutParams(params);

            itemsContainer.addView(itemView);
        }
    }
}
