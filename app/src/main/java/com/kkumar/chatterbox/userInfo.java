package com.kkumar.chatterbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class userInfo extends AppCompatActivity {

    EditText name;
    EditText phone;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        name=(EditText)findViewById(R.id.name);
        mAuth =FirebaseAuth.getInstance();
    }

    public void userinfonext(View view)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Name", name.getText().toString().trim());
        editor.putString("Phone", phone.getText().toString().trim());
        editor.apply();



        String email = pref.getString("Email", "");
        String password = pref.getString("Password", "");
        String n = pref.getString("Name", "User");
        String mob = pref.getString("Phone", "");

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference(mob).child("Email");
        myRef.setValue(email);

        DatabaseReference myRef1 = database.getReference(mob).child("Password");
        myRef1.setValue(password);

        DatabaseReference myRef2 = database.getReference(mob).child("Name");
        myRef2.setValue(n);

        DatabaseReference myRef3 = database.getReference(mob).child("Phone");
        myRef3.setValue(mob);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(userInfo.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(userInfo.this, "Account created !", Toast.LENGTH_SHORT).show();

                if (!task.isSuccessful()) {
                    Toast.makeText(userInfo.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                } else {

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isLoggedin", true);
                    editor.apply();

                    Intent intent = new Intent(userInfo.this, profilepic.class);
                    startActivity(intent);
                }
            }});
        }
}
