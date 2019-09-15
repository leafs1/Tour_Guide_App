package com.example.tour;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;


public class suggest extends AppCompatActivity {
    private ArrayList<String> locations;
    private EditText number_of_locations;
    private String[] locations_array = {"Ripley's Aquarium Of Canada",
            "St. Lawrence Market",
            "CN Tower",
            "Royal Conservatory of Music",
            "Royal Alexandra Theatre",
            "Toronto Symphony Orchestra",
            "Steam Whistle Brewery",
            "Princess of Wales Theatre",
            "The Elgin & Winter Garden Theatre Centre",
            "Toronto Island Park",
            "Ed Mirvish Theatre",
            "High Park",
            "Toronto Public Library",
            "Four Seasons Centre for the Performing Arts",
            "University of Toronto"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        locations = new ArrayList<String>();
        number_of_locations = findViewById(R.id.number_of_locations);
    }
    public void submit(View view) {
        for (int i=0;i<Integer.parseInt(number_of_locations.getText().toString());i++){
            locations.add(locations_array[i]);
        }
        String trip = "";
        for (int i = 0;i<locations.size();i++)
            trip+=(i+1)+". "+locations.get(i)+"\n";
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(suggest.this);

        // Set the message show for the Alert time
        builder.setMessage(trip);

        // Set Alert Title
        builder.setTitle("Suggested Trip");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Let's Go!",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                                intent.putExtra("locations", locations);
                                startActivity(intent);
                            }
                        });
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
        //receive data using:         ArrayList<String> locations = getIntent().getStringArrayListExtra("locations");
    }
}
