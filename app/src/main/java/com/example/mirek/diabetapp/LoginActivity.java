package com.example.mirek.diabetapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
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

    public void passwordRemember(View v){
        final Dialog d = new Dialog(LoginActivity.this);
        d.setContentView(R.layout.password_remember);
        final EditText editText = d.findViewById(R.id.rememberEmail);
        CardView cardView = d.findViewById(R.id.sendPassword);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText.getText().toString();
                if(!email.equals("")) {
                    sendPassword(email);
                } else {
                    Toast.makeText(LoginActivity.this, "Wpisz adres e-mail!", Toast.LENGTH_SHORT).show();
                }
                d.dismiss();
            }
        });
        d.show();
    }

    public void sendPassword(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Hasło zostało wysłane na wskazy adres e-mail.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Błędny adres e-mail.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToMainActivity(){
        Intent i = new Intent(this,AddResult.class);
        startActivity(i);
    }
}
