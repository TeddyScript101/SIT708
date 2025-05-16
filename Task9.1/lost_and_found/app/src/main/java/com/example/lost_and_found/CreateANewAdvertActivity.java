package com.example.lost_and_found;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateANewAdvertActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 101;
    private static final int PICK_LOCATION_REQUEST_CODE = 102;

    private RadioGroup radioGroupType;
    private EditText inputName, inputPhone, inputDesc, inputDate, inputLocation;
    private Button btnSave;
    private TextView textLatitude, textLongitude;

    private double selectedLat = 0.0;
    private double selectedLng = 0.0;

    private AdvertDao advertDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_anew_advert);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCLs3onkhRd5on4EB7EUkJpBYdFcGtXNVc", Locale.getDefault());
        }

        // UI elements
        radioGroupType = findViewById(R.id.radio_group_type);
        inputName = findViewById(R.id.input_name);
        inputPhone = findViewById(R.id.input_phone);
        inputDesc = findViewById(R.id.input_description);
        inputDate = findViewById(R.id.input_date);
        inputLocation = findViewById(R.id.input_location);
        btnSave = findViewById(R.id.btn_save);
        textLatitude = findViewById(R.id.text_latitude);
        textLongitude = findViewById(R.id.text_longitude);

        // Disable manual typing in location field
        inputLocation.setFocusable(false);
        inputLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
            );
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(CreateANewAdvertActivity.this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        inputDate.setFocusable(false);
        inputDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreateANewAdvertActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        inputDate.setText(formattedDate);
                    },
                    year, month, day
            );

            // Allow only past and current dates
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
        // Pick from map button
        Button btnPickLocation = findViewById(R.id.btn_pick_location);
        btnPickLocation.setOnClickListener(v -> {
            Intent intent = new Intent(CreateANewAdvertActivity.this, PickLocationActivity.class);
            startActivityForResult(intent, PICK_LOCATION_REQUEST_CODE);
        });

        // Set up Room database
        advertDao = Room.databaseBuilder(getApplicationContext(),
                        AdvertDatabase.class, "advert_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
                .advertDao();

        btnSave.setOnClickListener(v -> saveAdvert());
    }

    private void saveAdvert() {
        int selectedRadioButtonId = radioGroupType.getCheckedRadioButtonId();

        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Please select a post type", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String type = selectedRadioButton.getText().toString();

        String name = inputName.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String desc = inputDesc.getText().toString().trim();
        String date = inputDate.getText().toString().trim();
        String location = inputLocation.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || desc.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Advert advert = new Advert(type, name, phone, desc, date, location, selectedLat, selectedLng);
        advertDao.insert(advert);

        Toast.makeText(this, "Advert Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedLat = data.getDoubleExtra("selected_lat", 0);
            selectedLng = data.getDoubleExtra("selected_lng", 0);
            String address = data.getStringExtra("selected_address");

            inputLocation.setText(address != null ? address : ("Lat: " + selectedLat + ", Lng: " + selectedLng));
            textLatitude.setText("Latitude: " + selectedLat);
            textLongitude.setText("Longitude: " + selectedLng);
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            inputLocation.setText(place.getAddress());

            if (place.getLatLng() != null) {
                selectedLat = place.getLatLng().latitude;
                selectedLng = place.getLatLng().longitude;

                textLatitude.setText("Latitude: " + selectedLat);
                textLongitude.setText("Longitude: " + selectedLng);
            }
        }
    }
}
