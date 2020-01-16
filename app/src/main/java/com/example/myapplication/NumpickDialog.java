package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NumpickDialog extends AppCompatDialogFragment {
    private EditText editQuantity;
    private NumpickDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_numpick_dialog, null);

        builder.setView(view)
                .setTitle("Quantity picker")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int quantity = Integer.valueOf(editQuantity.getText().toString());
                        listener.applyChangesFromDialog(quantity);
                    }
                });

        editQuantity = view.findViewById(R.id.edit_quantity);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (NumpickDialogListener) context ;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement NumpickDialogListenr");
        }

    }

    public interface NumpickDialogListener{
        void applyChangesFromDialog(int quantity);
    }
}
