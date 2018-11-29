package com.example.mirek.diabetapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mirek.diabetapp.models.Doctors;
import com.example.mirek.diabetapp.models.FirebaseCallback;
import com.example.mirek.diabetapp.models.FirebaseCallbackDoctors;
import com.example.mirek.diabetapp.models.ThreeLevelListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorsChooserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private TextView txtName;
    private TextView txtSurname;
    private TextView txtCity;
    private TextView txtPlace;
    private TextView txtPhoneNumber;


    private ListView mListView;

    String[] result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_chooser);

        txtName = findViewById(R.id.txtName);
        txtSurname = findViewById(R.id.txtSurname);
        txtCity = findViewById(R.id.txtCity);
        txtPlace = findViewById(R.id.txtPlace);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


        FirebaseCallbackDoctors firebaseCallbackDoctors = new FirebaseCallbackDoctors() {
            @Override
            public void onCallback(String[] value) {
                result = value;
            }
        };

        getDoctors(firebaseCallbackDoctors);


        //MENU
        Toolbar toolbar = findViewById(R.id.doctorsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Wybór lekarza prowadzącego");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void getDoctors(final FirebaseCallbackDoctors firebaseCallbackDoctors){
        mDatabaseReference.child("doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                int i = 0;

                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    i++;
                }
                Log.e("i",String.valueOf(i));
                String[] doctors = new String[i];
                int k = 0;
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    if(!String.valueOf(mDataSnapshot.getValue()).equals("")){
                        doctors[k] = String.valueOf(mDataSnapshot.getValue());
                        Log.e("i",doctors[k]);
                        k++;
                    }
                }
                if(doctors.length > 0){
                    firebaseCallbackDoctors.onCallback(doctors);
                } else {
                    firebaseCallbackDoctors.onCallback(null);
                }

                mListView = findViewById(R.id.doctorsListView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(DoctorsChooserActivity.this, R.layout.list_item, R.id.lblListItem, doctors);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(DoctorsChooserActivity.this);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
            }
        });
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id){
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(DoctorsChooserActivity.this).create();

        String[] doctor = result[ Integer.parseInt(String.valueOf(id))].split(",");
        String[] surname = doctor[0].split("=");
        String[] email = doctor[1].split("=");
        String[] place = doctor[2].split("=");
        String[] name = doctor[3].split("=");
        String[] phone_number = doctor[4].split("=");
        String[] city = doctor[5].split("=");


        alertDialog.setTitle(name[1] + " " + surname[1]);
        alertDialog.setMessage("Czy chcesz bezpowrotnie usunąć konto?");

        Log.e("position", String.valueOf(position));
        Log.e("id", String.valueOf(id));



        alertDialog.show();
    }

}
