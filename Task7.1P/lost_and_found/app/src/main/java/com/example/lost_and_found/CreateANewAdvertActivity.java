package com.example.lost_and_found;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.Calendar;

public class CreateANewAdvertActivity extends AppCompatActivity {

    private RadioGroup radioGroupType;
    private EditText inputName, inputPhone, inputDesc, inputDate, inputLocation;
    private Button btnSave;

    private AdvertDao advertDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_anew_advert);

        radioGroupType = findViewById(R.id.radio_group_type);
        inputName = findViewById(R.id.input_name);
        inputPhone = findViewById(R.id.input_phone);
        inputDesc = findViewById(R.id.input_description);
        inputDate = findViewById(R.id.input_date);

        inputDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreateANewAdvertActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String dateStr = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        inputDate.setText(dateStr);
                    },
                    year, month, day);
            datePickerDialog.show();
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        });


        inputLocation = findViewById(R.id.input_location);
        btnSave = findViewById(R.id.btn_save);

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

        // Check if any RadioButton is selected
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, "Please select a post type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the selected RadioButton and its text
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String type = selectedRadioButton.getText().toString();

        String name = inputName.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String desc = inputDesc.getText().toString().trim();
        String date = inputDate.getText().toString().trim();
        String location = inputLocation.getText().toString().trim();

        // Validate other fields
        if (name.isEmpty() || phone.isEmpty() || desc.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and save the advert
        Advert advert = new Advert(type, name, phone, desc, date, location);
        advertDao.insert(advert);

        Toast.makeText(this, "Advert Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

}
