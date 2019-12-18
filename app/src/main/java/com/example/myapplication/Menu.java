package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Menu extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    double[] Entrees ={0.00,18.00,17.00,12.00,12.00};
    double[] Main_Dishes = {0.00,20.00,25.00,30.00,32.00};
    double [] DESSERT = {0.00,7.00,5.00,10.00,12.00};
    double sumUpOrder , firstDish,mainDish, lastDish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button checkOut = findViewById(R.id.btnChceckout);

        Spinner spinnnerEntrees = findViewById(R.id.spinner);
        Spinner spinnnerMainDishes = findViewById(R.id.spinner2);
        Spinner spinnnerDessert = findViewById(R.id.spinner3);


        spinnnerEntrees.setOnItemSelectedListener(this);
        spinnnerDessert.setOnItemSelectedListener(this);
        spinnnerMainDishes.setOnItemSelectedListener(this);


        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Menu.this,Checkout.class);
                i.putExtra("SumUp",Double.toString(sumUpOrder));
                startActivity(i);
            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> parant, View v, int pos, long id) {
        int walla = parant.getId();

        if (walla == R.id.spinner){
            firstDish =  Entrees[pos];
        }
        if (walla == R.id.spinner2){
            mainDish =  Main_Dishes[pos];
        }
        if (walla == R.id.spinner3){
            lastDish =  DESSERT[pos];
        }
        sumUpOrder = firstDish+mainDish+lastDish;

        TextView tv = findViewById(R.id.textView3);
        String s = "SUM: "+ Double.toString(sumUpOrder) + "$";
        tv.setText(s);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
