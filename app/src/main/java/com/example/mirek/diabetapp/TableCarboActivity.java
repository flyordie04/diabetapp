package com.example.mirek.diabetapp;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.mirek.diabetapp.models.ThreeLevelListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TableCarboActivity extends AppCompatActivity {

    private ExpandableListView mExpandableListView;
    private List<String> dataHeader;
    private List<String> listDataHeader;
    private List<String> listDataHeader2;
    private List<String> listDataHelper;
    private HashMap<String, List<String>> listHash;


    LinkedHashMap<String, String[]> thirdLevel = new LinkedHashMap<>();
    List<String[]> secondLevel = new ArrayList<>();
    List<LinkedHashMap<String, String[]>> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_carbo);

        setUpAdapter();

        //MENU
        Toolbar toolbar = findViewById(R.id.tableToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Tabela wymienników");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpAdapter(){
        listDataHeader = new ArrayList<>();
        listDataHelper = new ArrayList<>();
        dataHeader = new ArrayList<>();
        listDataHeader2 = new ArrayList<>();
        listHash = new HashMap<>();
        String[] products = new String[50];
        String[] hash = new String[153];
        String[] headers = new String[12];
        String jsonData = loadJSONFromAsset();
        if(jsonData != null){
            try{
                JSONArray jsonArray = new JSONArray(jsonData);
                int j = 0;
                int k = 0;
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jo_inside = jsonArray.getJSONObject(i);
                    String products_value = jo_inside.optString("Produkty");
                    String product_value = jo_inside.optString("Produkt");
                    String product_quantity = jo_inside.optString("Ilość w gramach");
                    String product_carboQuantity = jo_inside.optString("Ilość WW w porcji");

                    if(!products_value.equals("")){
                        dataHeader.add(products_value);
                        headers[k] = products_value;
                        listDataHelper.clear();
                        k++;

                    }

                    if(!product_value.equals("")) {
                        j++;
                        listDataHeader.add(product_value);
                        listDataHelper.add(product_value);
                        listDataHeader2.add("Ilość w gramach: " + product_quantity);
                        listDataHeader2.add("Ilość WW w porcji: " + product_carboQuantity);
                        hash[j] = listDataHeader2.toString();
                        listDataHeader2.clear();
                        products[k] = listDataHelper.toString();
                    }



                }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }

        for(int i = 0; i<12; i++){

            StringBuilder stringBuilder = new StringBuilder(products[i+1]);
            stringBuilder.deleteCharAt(0);
            stringBuilder.deleteCharAt(products[i+1].length()-2);
            products[i+1] = stringBuilder.toString();
            secondLevel.add(Arrays.asList(products[i + 1].split(", ")).toArray(new String[Arrays.asList(products[i + 1].split(", ")).size()]));
            for(int j=0;j< Arrays.asList(products[i + 1].split(", ")).size();j++) {
                //stringBuilder = new StringBuilder(hash[j+1]);
                //stringBuilder.deleteCharAt(0);
                //stringBuilder.deleteCharAt(hash[j+1].length()-2);
                //hash[i+1] = stringBuilder.toString();
                thirdLevel.put(Arrays.asList(products[i + 1].split(", ")).get(j), new String[] {hash[j+1]});
                data.add(thirdLevel);
            }

        }

        mExpandableListView = findViewById(R.id.listViewTable);
        ThreeLevelListAdapter threeLevelListAdapter = new ThreeLevelListAdapter(this, headers, secondLevel, data);
        mExpandableListView.setAdapter(threeLevelListAdapter);
        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int i) {
                if(i != previousGroup){
                    mExpandableListView.collapseGroup(previousGroup);
                }
                previousGroup = i;
            }
        });


    }

    public String loadJSONFromAsset() {
        String json = null;

        try {
            InputStream is = getBaseContext().getAssets().open("table.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.e("json", json);
        return json;

    }
}
