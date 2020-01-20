package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class manager_panel extends AppCompatActivity {
    Button button_manage_employees;
    Button button_manage_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_panel);

        button_manage_employees = findViewById(R.id.button_manage_employees);
        button_manage_products = findViewById(R.id.button_manage_products);

        button_manage_employees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent direct_to_Employees_managment = new Intent(manager_panel.this, Employees_management.class);
                startActivity(direct_to_Employees_managment);
            }
        });

        button_manage_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent direct_to_Products_management = new Intent(manager_panel.this, Products_management.class);
                startActivity(direct_to_Products_management);
            }
        });
    }
}
