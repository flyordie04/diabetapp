package com.example.mirek.diabetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private EditText txtPasswordRe;
    private FirebaseAuth firebaseAuth;

    private TextView textBack;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        textBack = findViewById(R.id.txtBack);
        textBack.setPaintFlags(textBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPasswordRegistration);
        txtPasswordRe = findViewById(R.id.txtPasswordRegistrationRe);
        firebaseAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("login",MODE_PRIVATE);
    }

    public void btnRegistrationUser_Click(View v){
        if(txtEmailAddress.getText().toString().equals("") || txtPassword.getText().toString().equals("")){
            Toast.makeText(RegistrationActivity.this,"Email i hasło nie mogą być puste!", Toast.LENGTH_LONG).show();
        } else {
            if(!txtPassword.getText().toString().equals(txtPasswordRe.getText().toString())){
                Toast.makeText(RegistrationActivity.this,"Hasła nie mogą się różnić!", Toast.LENGTH_LONG).show();
            } else {
                final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Proszę czekać...", "Rejestracja w toku.", true);
                (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Rejestracja powiodła się!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(RegistrationActivity.this, Settings.class);
                                    sp.edit().putBoolean("logged", true).apply();
                                    startActivity(i);
                                } else {
                                    Log.e("ERROR", task.getException().toString());
                                    Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }

    }

    public void back(View v){
        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
