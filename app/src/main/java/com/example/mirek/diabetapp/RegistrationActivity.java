package com.example.mirek.diabetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity{

    private EditText txtEmailAddress;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;

    private TextView textBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textBack = findViewById(R.id.txtBack);
        textBack.setPaintFlags(textBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPasswordRegistration);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnRegistrationUser_Click(View v){
        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Proszę czekać...", "Rejestracja w toku.", true);

        (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this,"Rejestracja powiodła się!", Toast.LENGTH_LONG).show();
                    Intent i= new Intent(RegistrationActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                else{
                    Log.e("ERROR",task.getException().toString());
                    Toast.makeText(RegistrationActivity.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void back(View v){
        Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(i);
    }
}
