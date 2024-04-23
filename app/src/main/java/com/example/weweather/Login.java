package com.example.weweather;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    String password,username,Susername,Spassword;
    EditText Epassword,Eusername;
    Button Blogin;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Epassword=findViewById(R.id.Password);
        Eusername=findViewById(R.id.Username);
        Blogin=findViewById(R.id.btn_login);
        DatabaseReference rootRef = db.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("Users");
        DatabaseReference data = usersRef.child("username");
        Blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password= Epassword.getText().toString();
                username = Eusername.getText().toString();
                if(password.isEmpty() && username.isEmpty()){
                    Toast.makeText(Login.this, "Please put your information in", Toast.LENGTH_SHORT).show();
                }
                else {
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Susername= dataSnapshot.child("Users").child("username").child("username").getValue(String.class);
                            Spassword= dataSnapshot.child("Users").child("username").child("password").getValue(String.class);
                            if(Susername.equals(username) && Spassword.equals(password)){
                                Toast.makeText(Login.this, "You have successfully loged in "+Susername, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, ListDays.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                        }
                    };
                    rootRef.addListenerForSingleValueEvent(valueEventListener);
                }
            }
        });
    }
}