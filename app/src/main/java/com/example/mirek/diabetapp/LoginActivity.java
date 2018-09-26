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

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmailLogin;
    private EditText txtPasswordLogin;
    private FirebaseAuth firebaseAuth;
    private TextView textBack;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textBack = findViewById(R.id.txtBack);
        textBack.setPaintFlags(textBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txtEmailLogin = findViewById(R.id.txtEmailLogin);
        txtPasswordLogin = findViewById(R.id.txtPasswordLogin);
        firebaseAuth = FirebaseAuth.getInstance();

        sp = getSharedPreferences("login",MODE_PRIVATE);
        if(sp.getBoolean("logged",true)){
            goToMainActivity();
        }
    }

    public void btnUserLogin_Click(View v){
        if(txtEmailLogin.getText().toString().equals("") || txtPasswordLogin.getText().toString().equals("")){
            Toast.makeText(LoginActivity.this,"Email i hasło nie mogą być puste!", Toast.LENGTH_LONG).show();
        } else {
            final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "Proszę czekać...", "Logowanie w toku.", true);

            (firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString(), txtPasswordLogin.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Logowanie powiodło się.", Toast.LENGTH_LONG).show();
                                sp.edit().putBoolean("logged", true).apply();
                                goToMainActivity();
                            } else {
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void back(View v){
        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    public void goToMainActivity(){
        Intent i = new Intent(this,AddResult.class);
        startActivity(i);
    }
}
