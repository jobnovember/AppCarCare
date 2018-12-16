package com.example.job.appcars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Register extends Fragment {
    private EditText mEmail;
    private EditText mPassword;
    private EditText mRe_password;
    private Button mRegister;
    private ProgressDialog mProgressdialog;
    private FirebaseAuth mFirebaseAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mEmail = (EditText) view.findViewById(R.id.TextEmail);
        mPassword = (EditText) view.findViewById(R.id.TextPassword);
        mRe_password = (EditText) view.findViewById(R.id.TextRePassword);
        mProgressdialog = new ProgressDialog(this.getContext());
        mFirebaseAuth = FirebaseAuth.getInstance();

        mRegister = (Button) view.findViewById(R.id.buttonRegister);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString();
        String re_password = mRe_password.getText().toString();
        String error = "";
        mProgressdialog.setMessage("Registering User...");
        mProgressdialog.show();


        if(!TextUtils.isEmpty(email)) {
           if(!TextUtils.isEmpty(password)) {
               if(password.equals(re_password)) {
                   mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                           .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful()) {
                                       //user is successfully resgistered and logged in
                                       Toast.makeText(getActivity(), "Registered Successfull", Toast.LENGTH_SHORT).show();
                                       mFirebaseAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               mProgressdialog.dismiss();
                                               getActivity().finish();
                                               startActivity(new Intent(getActivity(), Setup.class));
                                           }
                                       });
                                   } else {
                                       Toast.makeText(getActivity(), "Registered Failed, Please try again", Toast.LENGTH_SHORT).show();
                                       mProgressdialog.dismiss();
                                   }
                               }
                           });
               }
           }else {
              error += "\n Password can't Empty ";
           }
        }else {
           error += "\nEmail can't Empty";
        }

        if(!TextUtils.isEmpty(error)) {
            Toast.makeText(this.getContext(), error,Toast.LENGTH_LONG).show();
            mProgressdialog.dismiss();
        }

    }

}
