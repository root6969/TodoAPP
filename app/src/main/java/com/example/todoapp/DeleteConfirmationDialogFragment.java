package com.example.todoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmationDialogFragment extends DialogFragment {

    private OnDeleteConfirmedListener onDeleteConfirmedListener;

    public interface OnDeleteConfirmedListener {
        void onDeleteConfirmed();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onDeleteConfirmedListener != null) {
                            onDeleteConfirmedListener.onDeleteConfirmed();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                    }
                });

        return builder.create();
    }

    public void setOnDeleteConfirmedListener(OnDeleteConfirmedListener listener) {
        onDeleteConfirmedListener = listener;
    }
}
