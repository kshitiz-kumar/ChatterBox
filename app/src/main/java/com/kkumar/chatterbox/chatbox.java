package com.kkumar.chatterbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.support.constraint.Constraints.TAG;
import static com.kkumar.chatterbox.R.*;

public class chatbox extends AppCompatActivity {


    EditText messagetosend;
    ImageButton sendbutton;

    String phone;
    String myphone;
    CircleImageView recieverprofile;
    TextView reciever;
    String chatid;
    ListView messagelist;
    InputMethodManager keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_chatbox);
        messagetosend=(EditText)findViewById(id.messagesend);
        messagelist=(ListView)findViewById(id.messagelist);
        sendbutton= (ImageButton) findViewById(id.sendmessage);

        recieverprofile=(CircleImageView)findViewById(id.friendpic);
        reciever=(TextView)findViewById(id.reciever);
        keyboard=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        SharedPreferences pref = getApplication().getSharedPreferences("MyPref", MODE_PRIVATE);
        phone = pref.getString("chatwith", "");
        myphone= pref.getString("Phone", "");

        getinfo();

        if(Long.parseLong(phone)>Long.parseLong(myphone))
            chatid=phone+"_"+myphone;
        else
            chatid=myphone+"_"+phone;

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("message").child(chatid);

        FirebaseListAdapter<String> firebaseListAdapter=new FirebaseListAdapter<String>(this,String.class, layout.sentmessage,myref) {
            @Override
            protected void populateView(View v, String model, int position) {

                TextView hismessage=(TextView)v.findViewById(id.hismessage);
                TextView mymessage=(TextView)v.findViewById(id.mymessage);

                hismessage.setVisibility(View.GONE);
                mymessage.setVisibility(View.GONE);

                if(model.substring(0,10).equals(myphone))
                {
                    mymessage.setVisibility(View.VISIBLE);
                    mymessage.setText(model.substring(10));
                }
                else
                {
                    hismessage.setVisibility(View.VISIBLE);
                    hismessage.setText(model.substring(10));
                }

            }
        };    
        messagelist.setAdapter(firebaseListAdapter);

    }
    public void getinfo()
    {
        SharedPreferences pref = getApplication().getSharedPreferences("MyPref", MODE_PRIVATE);
        phone = pref.getString("chatwith", "");
        StorageReference storage=FirebaseStorage.getInstance().getReference(phone).child("Profilepic");
        DatabaseReference myref=FirebaseDatabase.getInstance().getReference(phone).child("Name");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                reciever.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;
        storage.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Glide.with(getApplicationContext()).load(finalLocalFile).into(recieverprofile);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });


    }

    public void sendmessage(View view)
    {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("message").child(chatid);
        myref.push().setValue(myphone+messagetosend.getText().toString().trim());
        messagetosend.setText("");
        keyboard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void back(View view)
    {
        finish();
    }
}
