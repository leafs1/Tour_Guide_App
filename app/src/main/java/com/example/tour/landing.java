package com.example.tour;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class landing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }
    /**
     * This method runs when the choose button is clicked,
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void choose_button(View view) {
        startActivity(new Intent(getApplicationContext(), choose.class));
    }
    /**
     * This method runs when the suggest button is clicked,
     * @param View - a View object that needs to be passed for the onClick method to work when its associated button is pressed.
     */
    public void suggest_button(View view) {
        startActivity(new Intent(getApplicationContext(), suggest.class));
    }
}
