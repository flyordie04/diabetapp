package com.example.mirek.diabetapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class TableCarboActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_carbo);

        mListView = findViewById(R.id.listViewTable);

        String jsonData = loadJSONFromAsset();
        String[] items = parseJson(jsonData);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        mListView.setAdapter(adapter);
        //setContentView(mListView);


    }

    private String[] parseJson(String jsonData){
        String[] items = new String[0];
        Log.e("jsonData", jsonData);
        if(jsonData != null){
            try{
                JSONArray jsonArray = new JSONArray(jsonData);

                ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> m_li;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo_inside = jsonArray.getJSONObject(i);
                    String products_value = jo_inside.optString("Produkty");
                    String product_value = jo_inside.optString("Produkt");
                    String product_quantity = jo_inside.optString("Ilość w gramach");
                    String product_carboQuantity = jo_inside.optString("Ilość WW w porcji");

                    if(!products_value.equals("")){
                        m_li = new HashMap<String, String>();
                        m_li.put("products", "Kategoria: " + products_value);
                        formList.add(m_li);
                    }
                    if(!product_value.equals("")) {
                        m_li = new HashMap<String, String>();
                        m_li.put("product_value", product_value);
                        formList.add(m_li);
                    }
                    if(!product_quantity.equals("")) {
                        m_li = new HashMap<String, String>();
                        m_li.put("product_quantity", "Gramów: " + product_quantity);
                        formList.add(m_li);
                    }
                    if(!product_carboQuantity.equals("")) {
                        m_li = new HashMap<String, String>();
                        m_li.put("product_carboQuantity", "Ilość wymienników: " + product_carboQuantity);
                        formList.add(m_li);
                    }
                }
                String sum = "";
                for (HashMap<String, String> hash : formList) {
                    for (String current : hash.values()) {
                        sum = sum + current + "<#>";
                    }
                }
                if(!sum.equals(null)) {
                    items = sum.split("<#>");
                }
                return items;
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return items;
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
