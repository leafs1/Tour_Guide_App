package com.example.tour;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

public class choose extends AppCompatActivity {
    private LinearLayout list;
    private ArrayList<String> locations;
    private TextView location_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        locations = new ArrayList<String>();
        list = findViewById(R.id.list);
        location_text = findViewById(R.id.location_text);

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
//        valueTV.setLayoutParams(new LayoutParams(
//                LayoutParams.FILL_PARENT,
//                LayoutParams.WRAP_CONTENT));
            list.addView(list_item);
            location_text.setText("");
        }
    }
    public void submit(View view) {
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        intent.putExtra("locations", locations);
        startActivity(intent);
        //receive data using:         ArrayList<String> locations = getIntent().getStringArrayListExtra("locations");
    }
}
