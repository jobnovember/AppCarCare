package com.example.job.appcars;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.job.appcars.models.Car;
import com.example.job.appcars.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class Setup extends AppCompatActivity {

    private ImageView mButton;
    private ImageView mImageView;
    private Button mButtonRegister;
    private EditText mEditName;
    private EditText mEditPhone;
    private Button mAddCars;
    private ListView mListView;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;
    private StorageReference mStorageref;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;
    private Uri mResultUri;
    private String mDownloadUri;

    private ArrayList<String> mArrayListCars;
    private ArrayList<String> mArrayListMyCars;
    private ArrayList<String> mArrayListCarsNumber;

    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mStorageref = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();       
        mProgressDialog = new ProgressDialog(Setup.this);
        mArrayListCars = new ArrayList<String>();
        mArrayListMyCars = new ArrayList<String>();
        mArrayListCarsNumber = new ArrayList<String>();

        mArrayAdapter = new ArrayAdapter<String>(Setup.this, android.R.layout.simple_list_item_1, mArrayListMyCars);
        fetchCars();
        if(mFirebaseAuth.getCurrentUser() != null) {
            mUser = mFirebaseAuth.getCurrentUser();
        }

        mButton = (ImageView)findViewById(R.id.pickImage);
        mButtonRegister = (Button)findViewById(R.id.regisBtn);
        mImageView = (ImageView)findViewById(R.id.imageView);
        mEditName = (EditText)findViewById(R.id.EditName);
        mEditPhone = (EditText)findViewById(R.id.EditPhone);
        mListView = (ListView)findViewById(R.id.listView);
        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //mListMyCars.get(position) mListMyCarsNumber(position);
                String carname = mArrayListMyCars.get(position);
                String carnumber = mArrayListCarsNumber.get(position);
                createItemDialog(carname, carnumber);
                return true;
            }
        });
        mAddCars = (Button)findViewById(R.id.addCars);

        mAddCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Setup.this);
            }
        });

         mButtonRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savePicture();
                        saveInfomation();
                    }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                mResultUri = result.getUri();
                mImageView.setImageURI(mResultUri);
                mImageView.setBackgroundColor(0);

            }
        }
    }

    private void savePicture() {
        mProgressDialog.setMessage("Uploadding");
        mProgressDialog.show();
        String uid = mUser.getUid();
        StorageReference storageReference = mStorageref.child("images/users/"+uid+"/"+uid+".jpg");
        storageReference.putFile(mResultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mDownloadUri = mUser.getUid()+".jpg";
                saveInfomation();
                mProgressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
            }
        });
    }

    private void saveInfomation() {
        String uid = mUser.getUid().toString();
        String name = mEditName.getText().toString().trim();
        String phone = mEditPhone.getText().toString().trim();
        String img = mDownloadUri+"";

        User users = new User(uid,img,name,phone,mArrayListMyCars, mArrayListCarsNumber);

        mDatabaseRef.child("users").child(uid)
                .setValue(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
                startActivity(new Intent(Setup.this, SecondActivity.class));
            }
        });

    }

    private void show(String text) {
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }

    private void fetchCars() {
        mDatabaseRef.child("cars").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Car value = dataSnapshot.getValue(Car.class);
                mArrayListCars.add(value.getName());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showInputDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CarDialog dialog = CarDialog.newInstance(mArrayListCars);
        dialog.show(fm, null);
        dialog.setOnFinishDialogListener(new CarDialog.OnFinishDialogListener() {
            @Override
            public void OnFinishDialog(String carname, String carnumber) {
                mArrayListMyCars.add(carname);
                mArrayListCarsNumber.add(carnumber);

                mArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void createItemDialog(final String carname, final String carnumber) {
        final String[] menu = {"Delete"};
        new AlertDialog.Builder(Setup.this)
                .setIcon(R.drawable.ic_settings_black_24dp)
                .setTitle("Menu")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ShowConfrimDialog(carname, carnumber);
                                break;
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void ShowConfrimDialog(String carname, String carnumber) {
        FragmentManager fm = getSupportFragmentManager();
        ConfirmDialog dlg = ConfirmDialog.newInstance(
                "Are you want to dalete this car ?",
                "Yes",
                "No",
                mArrayListMyCars,
                mArrayListCarsNumber,
                carname,
                carnumber
        );
        dlg.show(fm, null);
        dlg.setOnFinishDialogListener(new ConfirmDialog.OnFinishDialogListener() {
            @Override
            public void OnFinishDialog(ArrayList<String> carsList, ArrayList<String> numberList) {
                mArrayListMyCars = carsList;
                mArrayListCarsNumber = numberList;
                mArrayAdapter.notifyDataSetChanged();
            }
        });

    }



}
