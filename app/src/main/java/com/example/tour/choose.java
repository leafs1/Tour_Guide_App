package com.example.tour;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

public class choose extends AppCompatActivity {
    private LinearLayout list;
    private ArrayList<String> locations;
    private EditText location_text;
    private EditText remove_index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        locations = new ArrayList<String>();
        list = findViewById(R.id.list);
        location_text = findViewById(R.id.location_text);
        remove_index = findViewById(R.id.remove_index);
    }
    public void add_location(View view) {
        if (!location_text.getText().toString().trim().equals("")) {
            String name = location_text.getText().toString().trim();
            locations.add(name);
            TextView list_item = new TextView(this);
            list_item.setText(locations.size() + ". " + name);
            list_item.setAllCaps(true);
            list_item.setTextSize(18);
            list_item.setTextColor(Color.BLACK);
            list_item.setId(locations.size());
            list_item.setPadding(0,0,0,10);
//        valueTV.setLayoutParams(new LayoutParams(
//                LayoutParams.FILL_PARENT,
//                LayoutParams.WRAP_CONTENT));
            list.addView(list_item);
            location_text.setText("");
        }
    }
    public void remove(View view) {
//        if (!remove_index.getText().toString().trim().equals("")&&locations.size()>0) {
//            int index = Integer.parseInt(remove_index.getText().toString().trim());
//            locations.remove(index-1);
//            int index_id = getResources().getIdentifier(index+"", "id", getPackageName());
//            TextView list_item = findViewById(index_id);
//            list.removeView(list_item);
//            for (int i = index;i<locations.size();i++){
//                int change_id = getResources().getIdentifier(i+"", "id", getPackageName());
//                TextView change_item = findViewById(change_id);
//                String newText = Character.getNumericValue(change_item.getText().toString().charAt(0))-1+change_item.getText().toString().substring(1);
//                change_item.setText(newText);
//                change_item.setId(index);
//            }
//            remove_index.setText("");
//        }
    }
    public void submit(View view) {
        for (int i = 0;i<locations.size();i++)
            System.out.println(locations.get(i));
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        intent.putExtra("locations", locations);
        startActivity(intent);
        //receive data using:         ArrayList<String> locations = getIntent().getStringArrayListExtra("locations");
    }

}
