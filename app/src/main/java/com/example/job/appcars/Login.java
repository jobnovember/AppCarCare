package com.example.job.appcars;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.job.appcars.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends Fragment {
    private Button mLogin;
    private EditText mEditEmail;
    private EditText mEditPassword;

    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mEditEmail = (EditText) view.findViewById(R.id.editEmail);
        mEditPassword = (EditText) view.findViewById(R.id.editPassword);
        mProgressDialog = new ProgressDialog(getContext());
        mLogin = (Button) view.findViewById(R.id.loginBtn);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    private void userLogin() {
        String email = mEditEmail.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        String error = "";

        mProgressDialog.setMessage("Waiting for login Please wait");
        mProgressDialog.show();

        if(email.isEmpty()) {
            error = "\n Email is empty";
            return;
        }

        if(password.isEmpty()) {
            error = "\n Password is empty";
            return;
        }

        if(!error.isEmpty()) {
            show("Errors + "+error);
            mProgressDialog.dismiss();
        }else {
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                //setup_profile();
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), SecondActivity.class));
                            }else {
                                show("Login failed ... Please try agin");
                            }
                            mProgressDialog.dismiss();
                        }
                    });
        }

    }

    private void show(String text) {
        Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
    }
}
