package com.example.bottomnav.ui.addTask;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bottomnav.DBHelper;
import com.example.bottomnav.R;
import com.example.bottomnav.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTaskFragment extends Fragment {

    private EditText editTitle, editDetail, editDueDate;
    private Button buttonSave;
    private DBHelper dbHelper;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_task, container, false);

        editTitle = root.findViewById(R.id.editTextTitle);
        editDetail = root.findViewById(R.id.editTextDetail);
        editDueDate = root.findViewById(R.id.editTextDueDate);
        buttonSave = root.findViewById(R.id.buttonSave);

        editDueDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                        editDueDate.setText(selectedDate);
                    },
                    year, month, day);

            datePickerDialog.show();
        });

        dbHelper = new DBHelper(requireContext());

        buttonSave.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String detail = editDetail.getText().toString().trim();
            String dueDateStr = editDueDate.getText().toString().trim();

            if (title.isEmpty() || dueDateStr.isEmpty()) {
                Toast.makeText(getContext(), "Title and Due Date are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Date dueDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dueDateStr);
                Task task = new Task(0, title, detail, dueDate);
                dbHelper.addTask(task);

                Toast.makeText(getContext(), "Task Added", Toast.LENGTH_SHORT).show();
                editTitle.setText("");
                editDetail.setText("");
                editDueDate.setText("");
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Invalid date format!", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}
