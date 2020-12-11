package com.example.shoppinglists;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignIn extends AppCompatActivity{
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String _password, _user_id, _list_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef= database.getReference();
    }

    //אם כבר נכנסת לחשבון לא תהיה התנתקות
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(SignIn.this, MainActivity.class));
        }
        else{
           myRef.child("admin_account").addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   _password = snapshot.child("password").getValue().toString();
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });
        }
    }

    public void register(View view) {
        EditText editEmail = findViewById(R.id.email);
        EditText editPassword = findViewById(R.id.password);
        mAuth.createUserWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            _user_id = user.getUid();
                            myRef.child("users").child(_user_id).child("user_email").setValue(editEmail.getText().toString());
                            _list_id = myRef.child("users").child(_user_id).child("lists").push().getKey();
                            myRef.child("users").child(_user_id).child("current_list").setValue(_list_id);
                            System.out.println("the list_id is: "+_list_id);
                            start_intent();
                        } else {
                            Toast.makeText(SignIn.this, "try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void login(View view) {
        EditText editEmail = findViewById(R.id.email);
        EditText editPassword = findViewById(R.id.password);
        mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String st = editEmail.getText().toString();

                        if (task.isSuccessful()) {
                                //startActivity(new Intent(SignIn.this, adminMenu.class));
                            FirebaseUser user = mAuth.getCurrentUser();
                            _user_id = user.getUid();
                            _list_id = myRef.child("users").child(_user_id).child("lists").push().getKey();
                            System.out.println("the list_id is: "+_list_id);
                            start_intent();
                            }

                        else {
                            Toast.makeText(SignIn.this, "login faild", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    public void admin_register(View view) {
        EditText editEmail = findViewById(R.id.email);
        EditText editPassword = findViewById(R.id.password);
                        String st = editEmail.getText().toString();
                        String pass = editPassword.getText().toString();
                            if (st.equals("admin_lists@gmail.com")) {
                                if (pass.equals(_password)) {
                                    startActivity(new Intent(SignIn.this, adminMenu.class));
                                }
                            }
                        else {
                            Toast.makeText(SignIn.this, "login faild", Toast.LENGTH_LONG).show();
                        }
                    }


    public void start_intent(){
        Intent intent = new Intent(SignIn.this, MainActivity.class);
        intent.putExtra("list_id",_list_id);
        intent.putExtra("user_id",_user_id);
        startActivity(intent);
    }









}



