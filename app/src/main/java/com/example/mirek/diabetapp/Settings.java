package com.example.mirek.diabetapp;

import android.database.sqlite.SQLiteCursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    DatabaseHelper db;
    Button btnSave,btnPokaz;
    EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new DatabaseHelper(this);
        btnSave = findViewById(R.id.btnSave);
        btnPokaz = findViewById(R.id.btnPokaz);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean powodzenie;
                powodzenie = db.aktualizujUstawienia(etPhoneNumber.getText().toString());
                if (powodzenie) {
                    Toast.makeText(Settings.this, "Powodzenie", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Settings.this, "Nie udało się", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnPokaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Settings","1");
                SQLiteCursor kursor = db.pokazUstawienia();
                Log.d("Settings","2");
                if (kursor.getCount() > 0) {
                    Log.d("Settings","3");
                    StringBuffer buff = new StringBuffer();
                    while (kursor.moveToNext()) {
                        buff.append("ID: " + kursor.getString(0) + "\n");
                        buff.append("Numer: " + kursor.getString(1) + "\n");
                    }
                    PokazWiadomosc("Rekord",buff.toString());
                } else {
                    PokazWiadomosc("Brak", "Brak rekordów");
                }
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void PokazWiadomosc(String tytul, String wiadomosc) {
        AlertDialog.Builder budowniczy = new AlertDialog.Builder(this);
        budowniczy.setCancelable(true);
        budowniczy.setMessage(wiadomosc);
        budowniczy.setTitle(tytul);
        budowniczy.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
