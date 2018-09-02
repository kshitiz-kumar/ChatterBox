package com.kkumar.chatterbox;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class friendAdapter extends BaseAdapter {
    Context context;
     ArrayList<friend> chats;
    private StorageReference mStorageRef;
    public friendAdapter(Context context, ArrayList<friend> values) {
        this.context = context;
        this.chats = values;}
    public friendAdapter() { }
    @Override
    public int getCount() {
        return chats.size();
    }
    @Override
    public Object getItem(int position) {
        return chats.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        if (inflater != null) {
            rowView = inflater.inflate(R.layout.model,parent,false);
        }
        friend fi= new friend();
        fi=chats.get(position);
        String name=fi.getName();
        String phone=fi.getPhone();
        TextView Name=(TextView)rowView.findViewById(R.id.cname);
        TextView Phone=(TextView)rowView.findViewById(R.id.cphone);
        final CircleImageView photo=(CircleImageView)rowView.findViewById(R.id.cpic);
        Name.setText(name);
        Phone.setText(phone);
        mStorageRef = FirebaseStorage.getInstance().getReference(phone).child("Profilepic");
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg"); } catch (IOException e) {
            e.printStackTrace(); }
        final File finalLocalFile = localFile;
        mStorageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(context).load(finalLocalFile).into(photo);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }});
        return rowView;
    }}
