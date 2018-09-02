package com.kkumar.chatterbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import android.net.Uri;


import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class profileFragment extends Fragment {
    CircleImageView profilepic;
    TextView displayname,displayemail,displayphone,thought,thoughtbox,thoughtview;
    ImageButton setthought,changethought;
    Button logout;
    FirebaseDatabase database;
    DatabaseReference myRef,myRef1,myRef2,thoughtref;

    public profileFragment(){
        //empty constructor
    }
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_profile,container,false);
        profilepic=(CircleImageView)view.findViewById(R.id.displaypic);
        displayname=(TextView)view.findViewById(R.id.displayname);
        displayemail=(TextView)view.findViewById(R.id.displayemail);
        displayphone=(TextView)view.findViewById(R.id.displayphone);
        thought=(TextView)view.findViewById(R.id.thought);
        thoughtbox=(EditText)view.findViewById(R.id.thoughtbox);
        setthought=(ImageButton)view.findViewById(R.id.setthought);
        changethought=(ImageButton)view.findViewById(R.id.changethought);
        logout=(Button)view.findViewById(R.id.logout);
        thoughtview=(TextView)view.findViewById(R.id.thoughtview);
        final SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String phone = pref.getString("Phone", "");
        database = FirebaseDatabase.getInstance();
        thoughtref=database.getReference(phone).child("thought");
        thoughtref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

                thoughtview.setText(value);

                Log.d(TAG, "Value is: " + value);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        changethought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                changethought.setVisibility(View.GONE);
                thoughtview.setVisibility(View.GONE);
                thoughtbox.setVisibility(View.VISIBLE);
                setthought.setVisibility(View.VISIBLE);
            }
        });
        setthought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager=(InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0);
                String str=thoughtbox.getText().toString().trim();
                thoughtref=database.getReference(phone).child("thought");
                thoughtref.setValue(str);
                thoughtview.setText(str);
                thoughtview.setVisibility(View.VISIBLE);
                thoughtbox.setVisibility(View.GONE);
                changethought.setVisibility(View.VISIBLE);
                setthought.setVisibility(View.GONE);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isLoggedin", false);
                editor.apply();
                Intent intent =new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        getprofilepic();
        getdetails();



        return view;
    }



    public void getprofilepic(){
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        String phone = pref.getString("Phone", "");

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(phone).child("Profilepic");
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        mStorageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(getActivity()).load(finalLocalFile).into(profilepic);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }
    public void getdetails(){
        SharedPreferences pref = this.getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        String phone = pref.getString("Phone", "");
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference(phone).child("Name");
        myRef1=database.getReference(phone).child("Phone");
        myRef2=database.getReference(phone).child("Email");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                displayname.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                displayphone.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                displayemail.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    }




