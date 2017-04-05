package com.example.android.inclassassignment08;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class profileEdit extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    String TAG = "Profile Edit";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    String gender;
    boolean checked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    myRef = database.getReference(user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(profileEdit.this, UserInfo.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    public void onRadioButtonClicked(View view) {

         checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.female:
                if (checked)
                    gender = "female";
                break;
            case R.id.male:
                if (checked)
                    gender = "male";
                break;
            case R.id.nb:
                if (checked)
                    gender = "non-binary";
                break;
        }
    }


    public void updateInfo(View view) {

        EditText updatedName = (EditText) findViewById(R.id.CurrentName);
        String newName = updatedName.getText().toString();
        if (!updatedName.getText().toString().isEmpty()) {
            myRef.child("name").setValue(newName);
        }
        EditText updatedAge = (EditText) findViewById(R.id.CurrentAge);
        String newAge = updatedAge.getText().toString();
        if (!updatedAge.getText().toString().isEmpty()) {

            myRef.child("age").setValue(newAge);
        }
if (checked) {
    myRef.child("gender").setValue(gender);
}
        Toast.makeText(this, "Info Updated!", Toast.LENGTH_SHORT).show();
        finish();


    }


    @Override
    public void onStart() {
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
}

