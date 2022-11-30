package com.example.midterm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midterm.Common.Common;
import com.example.midterm.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class IntroInterface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_interface);

        TextView signUp = (TextView) findViewById(R.id.orderButton);
        TextView logIn = (TextView) findViewById(R.id.signInButton);

        Paper.init(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(IntroInterface.this, SignUp.class);
                startActivity(signUp);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = Paper.book().read(Common.USER_KEY);
                String pwd = Paper.book().read(Common.PWD_KEY);
                if(user != null && pwd != null && user != "User" && pwd != "Password"){
                    if(!user.isEmpty() && !pwd.isEmpty()){
                        Log.d("PAPER", user);
                        Log.d("PAPER2", pwd);
                        login(user, pwd);
                    }
                }
                else {
                    Intent signIn = new Intent(IntroInterface.this, SignIn.class);
                    startActivity(signIn);
                }
            }
        });

    }

    private void login(String phone, String pwd) {
        final ProgressDialog mDialog = new ProgressDialog(IntroInterface.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("User").child(phone);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setPhone(phone);
                    if (user.getPassword().equals(pwd)) {
                        Toast.makeText(IntroInterface.this, "Sign in successfully!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(IntroInterface.this, MainActivity.class);
                        Common.currentUser = user;
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(IntroInterface.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mDialog.dismiss();
                    Toast.makeText(IntroInterface.this, "User not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userRef.addValueEventListener(postListener);
    }
}