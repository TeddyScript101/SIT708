package com.example.bottomnav;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Task> tasks;
    private final DBHelper dbHelper;
    private final Context context;

    public RecyclerViewAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.tasks = tasks;
    }

    public void setTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, detail, dueDate;
        View editButton, deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskTitleTextView);
            detail = itemView.findViewById(R.id.detailTextView);
            dueDate = itemView.findViewById(R.id.dueDateTextView);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }

        void bind(Task task) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            title.setText(task.getTaskTitle());
            detail.setText(task.getTaskDetail());

            if (task.getDueDate() != null) {
                dueDate.setText(format.format(task.getDueDate()));
            } else {
                dueDate.setText("No Due Date");
            }

            editButton.setOnClickListener(v -> {
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);

                EditText editTitle = dialogView.findViewById(R.id.editTaskTitle);
                EditText editDueDate = dialogView.findViewById(R.id.editDueDate);
                EditText editDetail = dialogView.findViewById(R.id.editDetail);

                // Set existing data
                editTitle.setText(task.getTaskTitle());
                editDetail.setText(task.getTaskDetail());

                if (task.getDueDate() != null) {
                    editDueDate.setText(format.format(task.getDueDate()));
                } else {
                    editDueDate.setText("");
                }

                editDueDate.setOnClickListener(v1 -> {
                    final Calendar calendar = Calendar.getInstance();

                    try {
                        Date existingDate = format.parse(editDueDate.getText().toString());
                        calendar.setTime(existingDate);
                    } catch (Exception ignored) {}

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                            (view, year1, month1, dayOfMonth) -> {
                                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                                editDueDate.setText(selectedDate);
                            }, year, month, day);

                    datePickerDialog.show();
                });

                new AlertDialog.Builder(context)
                        .setTitle("Edit Task")
                        .setView(dialogView)
                        .setPositiveButton("Save", (dialog, which) -> {
                            String newTitle = editTitle.getText().toString().trim();
                            String newDueDateStr = editDueDate.getText().toString().trim();
                            String newDetail = editDetail.getText().toString().trim();

                            if (newTitle.isEmpty()) {
                                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            task.setTaskTitle(newTitle);
                            task.setTaskDetail(newDetail);

                            try {
                                if (!newDueDateStr.isEmpty()) {
                                    Date parsedDate = format.parse(newDueDateStr);
                                    task.setDueDate(parsedDate);
                                } else {
                                    task.setDueDate(null);
                                }

                                dbHelper.updateTask(task);
                                int oldPosition = getAdapterPosition();

                                tasks.sort((t1, t2) -> {
                                    Date d1 = t1.getDueDate();
                                    Date d2 = t2.getDueDate();
                                    if (d1 == null && d2 == null) return 0;
                                    if (d1 == null) return 1;
                                    if (d2 == null) return -1;
                                    return d1.compareTo(d2);
                                });


                                int newPosition = tasks.indexOf(task);

                                if (oldPosition != newPosition) {
                                    notifyItemMoved(oldPosition, newPosition);
                                }

                                notifyItemChanged(getAdapterPosition());
                                Toast.makeText(context, "Task updated", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Invalid date format (use yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            deleteButton.setOnClickListener(v -> {
                dbHelper.deleteTask(task.getId());
                tasks.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
