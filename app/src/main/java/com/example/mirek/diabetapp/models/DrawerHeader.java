package com.example.mirek.diabetapp.models;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.mirek.diabetapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


@SuppressLint("Registered")
public class DrawerHeader extends AppCompatActivity {

    private TextView username;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        username = findViewById(R.id.drawer_username);
        String text = "Witaj " + user + "!";
        username.setText(text);

    }
}
