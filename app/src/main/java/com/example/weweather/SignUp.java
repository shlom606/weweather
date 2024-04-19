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
    Button Bsignup;
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
        Bsignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                password= Epassword.getText().toString();
                rpassword = Erpassword.getText().toString();
                username = Eusername.getText().toString();

                Toast.makeText(SignUp.this, username, Toast.LENGTH_SHORT).show();

                if (!password.isEmpty() && !rpassword.isEmpty() && !username.isEmpty() ) {
                    User users = new User(password, rpassword, username);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("Users");
                    reference.child("username").setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Epassword.setText("");
                            Erpassword.setText("");
                            Eusername.setText("");
                            if(!password.equals(rpassword)){
                                Toast.makeText(SignUp.this, "please repeat the password twice correctly", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SignUp.this, "Successfuly signed up", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, ListDays.class);
                                startActivity(intent);
                            }

                        }
                    });
                }
            }
        });
    }
}