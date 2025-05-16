package com.example.lost_and_found;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AdvertDetailActivity extends AppCompatActivity {

    private AdvertDao advertDao;
    private int advertId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_detail);

        advertId = getIntent().getIntExtra("advertId", -1);
        advertDao = Room.databaseBuilder(getApplicationContext(),
                        AdvertDatabase.class, "advert_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .advertDao();

        Advert advert = advertDao.getById(advertId);

        // Calculate days ago
        String daysAgoText = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date advertDate = sdf.parse(advert.getDate());
            Date today = new Date();

            long diffInMillis = today.getTime() - advertDate.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(Math.abs(diffInMillis));

            if (diffInMillis > 0) {
                if (diffInDays == 0) daysAgoText = "(today)";
                else if (diffInDays == 1) daysAgoText = "(1 day ago)";
                else daysAgoText = "(" + diffInDays + " days ago)";
            } else if (diffInMillis < 0) {
                if (diffInDays == 1) daysAgoText = "(in 1 day)";
                else daysAgoText = "(in " + diffInDays + " days)";
            } else {
                daysAgoText = "(today)";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((TextView)findViewById(R.id.detailText)).setText(
                advert.getType() + " - " + advert.getName() + "\n\n"
                        + "Description: " + advert.getDescription() + "\n"
                        + "Date: " + advert.getDate() + " " + daysAgoText + "\n"
                        + "Location: " + advert.getLocation()
        );

        findViewById(R.id.btnRemove).setOnClickListener(v -> {
            advertDao.deleteById(advert.getId());
            Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
            finish();
        });

        Button btnReturn = findViewById(R.id.btn_return);
        btnReturn.setOnClickListener(v -> finish());
    }

}
