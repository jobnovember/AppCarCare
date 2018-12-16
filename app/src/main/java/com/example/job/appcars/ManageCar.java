package com.example.job.appcars;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.job.appcars.models.Car;
import com.example.job.appcars.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageCar extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;
    private String mUid;

    private ArrayList<String> mCars;
    private ArrayList<String> mNumber;
    private ArrayList<String> mArrayListCars;
    private ArrayAdapter<String> mArrayAdapter;

    private ListView mListView;

    private ImageView mAddBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_car);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mUid = mUser.getUid();
        mArrayListCars = new ArrayList<String>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mListView = (ListView)findViewById(R.id.listView);
        mAddBtn = (ImageView)findViewById(R.id.addBtn);

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCarDialog();
            }
        });
        fetchCars();
        fetch_data();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(ManageCar.this, SecondActivity.class));
    }

    private void fetch_data() {

        mDatabaseRef.child("users").child(mUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                mCars = user.getCars();
                mNumber = user.getCars_number();

                mArrayAdapter =  new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, mCars);
                mListView.setAdapter(mArrayAdapter);
                mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                        //mCars.get(position);
                        String carname = mCars.get(position);
                        String carnumber = mNumber.get(position);
                        createItemDialog(carname, carnumber);
                        return true;
                    }
                });
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String carnumber = mNumber.get(position);
                        showInfoDialog(carnumber);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createItemDialog(final String carname, final String carnumber) {
        final String[] menu = {"Delete","Info"};
        new AlertDialog.Builder(ManageCar.this)
                .setIcon(R.drawable.ic_settings_black_24dp)
                .setTitle("เลือกเมนู")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                showConfrimDialog(carname, carnumber);
                                break;
                            case 1:
                                showInfoDialog(carnumber);
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showConfrimDialog(String carname, String carnumber) {
        FragmentManager fm = getSupportFragmentManager();
        ConfirmDialog dlg = ConfirmDialog.newInstance(
                "Are you want to dalete this car ?",
                "Yes",
                "No",
                mCars,
                mNumber,
                carname,
                carnumber
        );
        dlg.show(fm, null);
        dlg.setOnFinishDialogListener(new ConfirmDialog.OnFinishDialogListener() {
            @Override
            public void OnFinishDialog(ArrayList<String> carsList, ArrayList<String> numberList) {
                mCars = carsList;
                mNumber = numberList;
                mArrayAdapter.notifyDataSetChanged();
                updateData();
            }
        });

    }

    private void showInfoDialog(final String carnumber) {
        FragmentManager fm = getSupportFragmentManager();
        InfoDialog dialog = InfoDialog.newInstance("ทะเบียนรถ", carnumber);
        dialog.show(fm, null);
    }

    private void showCarDialog() {
        FragmentManager fm = getSupportFragmentManager();
        CarDialog dialog = CarDialog.newInstance(mArrayListCars);
        dialog.show(fm, null);
        dialog.setOnFinishDialogListener(new CarDialog.OnFinishDialogListener() {
            @Override
            public void OnFinishDialog(String carname, String carnumber) {
                mCars.add(carname);
                mNumber.add(carnumber);
                mArrayAdapter.notifyDataSetChanged();
                updateData();
            }
        });
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

    private void updateData() {
        mDatabaseRef.child("users").child(mUid).child("cars").setValue(mCars);
        mDatabaseRef.child("users").child(mUid).child("cars_number").setValue(mNumber);
    }

    private void show(String text) {
        Toast.makeText(ManageCar.this, text, Toast.LENGTH_SHORT).show();
    }

}
