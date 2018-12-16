package com.example.job.appcars;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private String mUid;

    private CircleImageView mImageView;
    private TextView mTextName;
    private TextView mTextPhone;
    private ListView mListview;
    private Button mInputBtn;
    private ArrayAdapter<String> mArrayAdapter;

    private String mName;
    private String mPhone;
    private ArrayList<String> mCars;
    private ArrayList<String> mNumber;
    private ArrayList<String> mArrayListCars;
    private String mUrl;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mUid= mUser.getUid();
        mArrayListCars = new ArrayList<String>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        fetchCars();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTextName = (TextView)view.findViewById(R.id.name);
        mTextPhone = (TextView)view.findViewById(R.id.phone);
        mListview = (ListView)view.findViewById(R.id.listView);
        mImageView = (CircleImageView) view.findViewById(R.id.img_profile);
        mInputBtn = (Button) view.findViewById(R.id.InputBtn);

        fetch_data();

        mInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getContext(), ManageCar.class));
            }
        });

    }

    private void show(String text) {
        Toast.makeText(getActivity(), text,Toast.LENGTH_SHORT).show();
    }

    private void fetch_data() {
        mStorageRef.child("images/users/"+mUid+"/"+mUid+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getActivity().getApplicationContext()).load(uri).into(mImageView);
            }
        });
        mDatabaseRef.child("users").child(mUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mName = user.getName();
                mPhone = user.getPhone();
                mUrl = user.getProfile_image();
                mCars = user.getCars();
                mNumber = user.getCars_number();

                mTextName.setText(mName);
                mTextPhone.setText(mPhone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createItemDialog(final String carname, final String carnumber) {
        final String[] menu = {"Delete","Info"};
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_settings_black_24dp)
                .setTitle("Menu")
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

    private void showInfoDialog(final String carnumber) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InfoDialog dialog = InfoDialog.newInstance("ทะเบียนรถ", carnumber);
        dialog.show(fm, null);
    }

    private void showCarDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
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


    private void showConfrimDialog(String carname, String carnumber) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
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


}
