package com.example.job.appcars;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class CarDialog extends DialogFragment {
    private String mNumber;
    private String mCarsName;
    private ArrayList<String> mCars;
    public CarDialog() {}

    public static CarDialog newInstance(ArrayList<String> cars) {
        CarDialog dialog = new CarDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("Cars",cars);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_layout, null);
        mCars = new ArrayList<String>();
        mCars = getArguments().getStringArrayList("Cars");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, mCars);

        final EditText editNumber = (EditText)v.findViewById(R.id.edit_text);
        final Spinner spinerCar = (Spinner)v.findViewById(R.id.list_cars);
        spinerCar.setAdapter(adapter);

        spinerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCarsName = mCars.get(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNumber = editNumber.getText().toString();
                        mListener.OnFinishDialog(mCarsName, mNumber);
                    }
                });
        return alertDialogBuilder.create();
    }

    interface OnFinishDialogListener {
        void OnFinishDialog(String carname, String carnumber);
    }

    private OnFinishDialogListener mListener;

    public void setOnFinishDialogListener(OnFinishDialogListener lstn) {
        mListener = lstn;
    }
}
