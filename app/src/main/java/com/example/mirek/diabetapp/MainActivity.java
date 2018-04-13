package com.example.mirek.diabetapp;

import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    Button btnDodaj, btnPokaz;
    EditText etResult, etDate;
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
    String date = sdf.format(new Date());
    int sms=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        btnDodaj = findViewById(R.id.btnDodaj);
        btnPokaz = findViewById(R.id.btnPokaz);

        etResult = findViewById(R.id.etResult);
        etDate = findViewById(R.id.etDate);
        //enOceny = findViewById(R.id.enOceny);

        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double result = Double.parseDouble(etResult.getText().toString());
                if(result>60){
                    sms=1;
                }
                boolean powodzenie;
                powodzenie = db.wstawdane(etResult.getText().toString(),date,sms);
                if (powodzenie) {
                    Toast.makeText(MainActivity.this, "Powodzenie", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Nie udało się", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnPokaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteCursor kursor = db.pokazdane();
                if (kursor.getCount() > 0) {
                    StringBuffer buff = new StringBuffer();
                    while (kursor.moveToNext()) {
                        buff.append("ID: " + kursor.getString(0) + "\n");
                        buff.append("Wynik: " + kursor.getString(1) + "\n");
                        buff.append("Data: " + kursor.getString(2) + "\n");
                        buff.append("Czy wysłano SMS?: " + kursor.getString(3) + "\n");
                    }
                    PokazWiadomosc("Rekord",buff.toString());
                } else {
                    PokazWiadomosc("Brak", "Brak rekordów");
                }
            }
        });
    }
    public void PokazWiadomosc(String tytul, String wiadomosc) {
        AlertDialog.Builder budowniczy = new AlertDialog.Builder(this);
        budowniczy.setCancelable(true);
        budowniczy.setMessage(wiadomosc);
        budowniczy.setTitle(tytul);
        budowniczy.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    public void appExitmenu(MenuItem item) {
        finish();
    }
    public void settings(MenuItem item){
        startActivity(new Intent(getApplicationContext(),Settings.class));
    }
}

