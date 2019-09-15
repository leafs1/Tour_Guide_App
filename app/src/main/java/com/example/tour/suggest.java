package com.example.tour;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class suggest extends AppCompatActivity {
    private ArrayList<String> locations;
    private EditText number_of_locations;
    private String[] locations_array = {"CN Tower", "Hockey Hall of Fame", "Hospital for Sick Children", "Hilton Toronto", "Eaton Center"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        locations = new ArrayList<String>();
        number_of_locations = findViewById(R.id.number_of_locations);
    }
    public void submit(View view) {
        if (Integer.parseInt(number_of_locations.getText().toString())<=5 && !number_of_locations.getText().toString().equals("")) {
            for (int i = 0; i < Integer.parseInt(number_of_locations.getText().toString()); i++) {
                locations.add(locations_array[i]);
            }
            String trip = "Based on Tripadvisor's top 5 attractions in Toronto and your responses, we suggest:\n\n";
            for (int i = 0; i < locations.size(); i++)
                trip += (i + 1) + ". " + locations.get(i) + "\n";
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
                                                    int which) {

                                    Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                                    intent.putExtra("locations", locations);
                                    startActivity(intent);
                                }
                            });
            AlertDialog alertDialog = builder.create();

            // Show the Alert Dialog box
            alertDialog.show();
            //receive data using:         ArrayList<String> locations = getIntent().getStringArrayListExtra("locations");
        } else {
            Toast.makeText(getApplicationContext(),"Sorry, Wander can only accommodate a maximum of 5 locations ",Toast.LENGTH_LONG).show();
        }
    }
}
