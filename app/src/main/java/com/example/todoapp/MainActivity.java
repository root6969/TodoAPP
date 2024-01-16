package com.example.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public TaskAdapter taskAdapter;
    private ArrayList<TaskItem> tasksArrayList = new ArrayList<>();
    private EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        inputText = findViewById(R.id.inputText);

        // Initialize RecyclerView and Adapter
        taskAdapter = new TaskAdapter(tasksArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(taskAdapter);

        // Set up swipe-to-delete functionality
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                tasksArrayList.remove(position);
                taskAdapter.notifyItemRemoved(position);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // Add long-press listener to RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null) {
                        int position = recyclerView.getChildAdapterPosition(child);
                        if (position != RecyclerView.NO_POSITION) {
                            showEditDeleteDialog(recyclerView, position); // Show the edit/delete dialog
                        }
                    }
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    // Method to show the edit/delete dialog
    private void showEditDeleteDialog(final RecyclerView rv, final int position) {
        EditDeleteDialogFragment dialogFragment = new EditDeleteDialogFragment();
        dialogFragment.setOnOptionSelectedListener(new EditDeleteDialogFragment.OnOptionSelectedListener() {
            @Override
            public void onEditSelected() {
                // Handle edit here
                showEditDialog(tasksArrayList.get(position), position);
            }

            @Override
            public void onDeleteSelected() {
                // Handle delete here
                tasksArrayList.remove(position);
                taskAdapter.notifyItemRemoved(position);
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "EditDeleteDialog");
    }

    // Method to show the edit dialog
    private void showEditDialog(final TaskItem taskItem, final int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_task_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTaskText = dialogView.findViewById(R.id.editTaskText);
        editTaskText.setText(taskItem.getText());

        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Handle edit and update the task here
                String newText = editTaskText.getText().toString();
                taskItem.setText(newText);
                taskAdapter.notifyItemChanged(position);
                dialog.dismiss();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancel the edit
                dialog.dismiss();
            }
        });

        AlertDialog editDialog = dialogBuilder.create();
        editDialog.show();
    }

    public void onClickAdd(View v) {
        String text = inputText.getText().toString();
        if (!text.isEmpty()) {
            tasksArrayList.add(new TaskItem(text));
            taskAdapter.notifyDataSetChanged();
            inputText.getText().clear();
        }
    }
}
