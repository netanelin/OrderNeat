package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Checkout extends AppCompatActivity {

    String val ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             val = extras.getString("SumUp") +"$";
            //The key argument here must match that used in the other activity
        }

        TextView tv = findViewById(R.id.textView4);
        tv.setText(val);

    }
}
