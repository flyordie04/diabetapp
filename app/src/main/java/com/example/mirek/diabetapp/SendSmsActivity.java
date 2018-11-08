package com.example.mirek.diabetapp;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mirek.diabetapp.models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendSmsActivity extends AppCompatActivity {

    private TextView txtInfo;
    private TextView txtInfo2;
    private TextView txtInfoTitle;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String message = "Twój podopieczny ma niewłaściwy poziom cukru! Wynik: ";

    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        txtInfo = findViewById(R.id.txtInfo);
        txtInfo2 = findViewById(R.id.txtInfo2);
        txtInfoTitle = findViewById(R.id.txtInfoTitle);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        final String number;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            Log.e("extras",extras.toString());
            if(extras == null) {
                number = null;
            } else {
                number = extras.getString("STRING_I_NEED");
            }
        } else {
            number = (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getPhone_number() != null){
                String phoneNumber = dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getPhone_number();

                    send(number, phoneNumber);
                } else {
                    txtInfo.setText("Wiadomość alarmowa nie została wysłana, ponieważ nie podano numeru telefonu!");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
            }
        });

        if(Integer.parseInt(number) < 70){
            low();
        } else {
            high();
        }
    //MENU
    Toolbar toolbar = findViewById(R.id.sendSmsToolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Niewłaściwy poziom cukru");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
}
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void low(){
        txtInfoTitle.setText("Twój cukier jest za niski!");
        txtInfo2.setText("Zalecamy:\n- Przyjmij cukry proste np.: kostka cukru, coca-cola, woda z cukrem itp.\n- Zjedz wartościowszy posiłek np. kanapkę z pełnoziarnistego pieczywa.\n- Zmierz ponownie cukier za około 1 godzinę.");
    }
    private void high(){
        txtInfoTitle.setText("Twój cukier jest za wysoki!");
        txtInfo2.setText("Zalecamy:\n- Przyjmij odpowiednią dawkę insuliny, jeśli nie zrobiłeś/aś tego wcześniej.\n- Bezzwłocznie poinformuj lekarza.");
    }

    public void send(String number, String phoneNumber){


        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        else {
            txtInfo.setText("Wiadomość o złym stanie cukru została wysłana na wskazany numer telefonu: "+ phoneNumber);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null,  message + number , null, null);
        }
    }
}
