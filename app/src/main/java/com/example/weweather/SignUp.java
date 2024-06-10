package com.example.weweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    String password,rpassword,username;
    EditText Epassword,Erpassword,Eusername;
    Button Bsignup,Bmainscreen;
    FirebaseDatabase db;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        Epassword=findViewById(R.id.Password);
        Erpassword=findViewById(R.id.Rpassword);
        Eusername=findViewById(R.id.Username);
        Bsignup =findViewById(R.id.btn_sign);
        Bmainscreen=findViewById(R.id.btn_mainscreen);
        Bmainscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
        Bsignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                password= Epassword.getText().toString();
                rpassword = Erpassword.getText().toString();
                username = Eusername.getText().toString();
                db = FirebaseDatabase.getInstance();


                Toast.makeText(SignUp.this, username, Toast.LENGTH_SHORT).show();

                if (!password.isEmpty() && !rpassword.isEmpty() && !username.isEmpty()) {//checks if the input of the user is not empty
                    if(!password.equals(rpassword)){//if the repeat password isn't the same as the password than tell it to the user
                        Toast.makeText(SignUp.this, "Please repeat the password twice correctly", Toast.LENGTH_SHORT).show();
                        Epassword.setText("");
                        Erpassword.setText("");
                        Eusername.setText("");
                    }
                    else{
                        User users = new User(password, rpassword, username);
                        db = FirebaseDatabase.getInstance();
                        reference = db.getReference("Users");

                        reference.child(username).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {//when the user is added to firebase continue to next screen
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(SignUp.this, "Successfuly signed up", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, MapActivity.class);
                                intent.putExtra("searchUsernameInData", username);
                                startActivity(intent);

                            }
                        });
                    }

                }
                else{// if the input is empty than clear the edittexts and ask for his information
                    Epassword.setText("");
                    Erpassword.setText("");
                    Eusername.setText("");
                    Toast.makeText(SignUp.this, "Please put your information in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}