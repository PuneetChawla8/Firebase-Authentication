package com.example.student.firebase_authentication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddtoDatabase extends AppCompatActivity {
    private static final String TAG = "AddtoDatabase";
    private FirebaseDatabase mFirebaseDatabase;  //to refer to firebase database
    private FirebaseAuth mAuth;  //to authenticate user
    private FirebaseAuth.AuthStateListener mAuthListener; //to add to user
    private EditText food;
    private DatabaseReference mRef; //to refer specific database
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addto_database);
        food = (EditText) findViewById(R.id.food);
        mAuth = FirebaseAuth.getInstance();  //initializing user authentication
        mFirebaseDatabase = FirebaseDatabase.getInstance(); //initializing firebase database
        mRef = mFirebaseDatabase.getReference();//initial reference to database

        //user sign in check up
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

            }
        };
        // read data from database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
Object value=dataSnapshot.getValue();
//                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }
    // adding data to database
    public void onadd(View v)
    {
     String Food=food.getText().toString();
        if(!Food.equals(""))
        {
            // getting user's uid
            FirebaseUser user=mAuth.getCurrentUser();
            String userID=user.getUid();

            // now use reference to database to add data to database
// ref.child(firstpart).child(secondpart).child(thirdpart)
            mRef.child(userID).child("food").child("favourite food").child(Food).setValue("true");
            TextMessage("Adding "+Food+"to database...");
            food.setText("");
        }
    }
    // Read from the database

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

    public void TextMessage(String text)
    {
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}