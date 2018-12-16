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

public class InfoDialog extends DialogFragment {
    private String mTitle;
    private String mMsg;
    private TextView tvTitle;
    private TextView tvMsg;

    public InfoDialog() {}

    public static InfoDialog newInstance(String title, String message) {
        InfoDialog dialog = new InfoDialog();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putString("msg",message);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_info_layout, null);
        mTitle = getArguments().getString("title");
        mMsg = getArguments().getString("msg");
        tvTitle = (TextView) v.findViewById(R.id.title);
        tvMsg = (TextView) v.findViewById(R.id.message);

        tvTitle.setText(mTitle);
        tvMsg.setText(mMsg);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return alertDialogBuilder.create();
    }
}
