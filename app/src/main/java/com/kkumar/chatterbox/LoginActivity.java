package com.kkumar.chatterbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText takenumber;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnLogin;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //set view now
        setContentView(R.layout.activity_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        takenumber=(EditText)findViewById(R.id.takenumber);

    }

    public void verify(View view)
    {

        if(takenumber.getText().toString().length()!=10)
        {
            Toast.makeText(LoginActivity.this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
            takenumber.setError("Invalid Number");
            return;
        }
        else
        {
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
            SharedPreferences.Editor editor = preference.edit();
            editor.putString("UserPhone",takenumber.getText().toString());
            editor.apply();
            Intent intent=new Intent(LoginActivity.this,OtpVerification.class);
            startActivity(intent);
        }
    }
}
