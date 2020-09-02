package com.example.student.firebase_authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Email=(EditText)findViewById(R.id.email);
        Password=(EditText)findViewById(R.id.pass);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    TextMessage("Signed in ");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    TextMessage("Signed out");
                }
                // ...
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    public void Signin(View v)
    {
        String email=Email.getText().toString();
        String password=Password.getText().toString();
        if(!email.equals("")&&!password.equals(""))
        {
mAuth.signInWithEmailAndPassword(email,password);
            Email.setText(" ");
            Password.setText(" ");
        }
        else
        {
            Email.setError("Invalid");
            Password.setError("Invalid");
        }
    }

    public void Signout(View v)
    {
        mAuth.signOut();
        TextMessage("Signing Out....");
    }
    public void TextMessage(String text)
    {
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
    public void Additem(View v)
    {
        Intent i=new Intent(this,AddtoDatabase.class);
        startActivity(i);
    }
}
