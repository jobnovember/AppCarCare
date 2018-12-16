package com.example.job.appcars;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class ConfirmDialog extends DialogFragment {
    private String mTitle;
    private String mYes;
    private String mNo;
    private String mCarname;
    private String mCarnumber;
    private ArrayList<String> mCars;
    private ArrayList<String> mNumber;

    public ConfirmDialog() {}

    public static ConfirmDialog newInstance(
            String title, String yes, String no,
            ArrayList<String> cars, ArrayList<String> number,
            String carname, String carnumber) {
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("yes",yes);
        args.putString("no",no);
        args.putString("carname",carname);
        args.putString("carnumber",carnumber);
        args.putStringArrayList("ArrayListCars",cars);
        args.putStringArrayList("ArrayListNumber", number);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_confrim_layout, null);
        mTitle = getArguments().getString("title");
        mYes = getArguments().getString("yes");
        mNo = getArguments().getString("no");
        mCars = getArguments().getStringArrayList("ArrayListCars");
        mNumber = getArguments().getStringArrayList("ArrayListNumber");
        mCarname = getArguments().getString("carname");
        mCarnumber = getArguments().getString("carnumber");
        final TextView  titleTextView = (TextView)v.findViewById(R.id.TextViewTitle);
        titleTextView.setText(mTitle);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNegativeButton(mNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setPositiveButton(mYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCars.remove(mCarname);
                        mNumber.remove(mCarnumber);
                        mListener.OnFinishDialog(mCars, mNumber);
                    }
                });
        return alertDialogBuilder.create();
    }

    interface OnFinishDialogListener {
        void OnFinishDialog(ArrayList<String> carsList, ArrayList<String> numberList);
    }

    private OnFinishDialogListener mListener;

    public void setOnFinishDialogListener(OnFinishDialogListener lstn) {
        mListener = lstn;
    }
}
