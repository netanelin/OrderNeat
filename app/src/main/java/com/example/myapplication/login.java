package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login extends AppCompatActivity {
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final Button bt_login = findViewById(R.id.bt_login);
    private final Button bt_register = findViewById(R.id.bt_register);
    private final Button bt_employee_register = findViewById(R.id.bt_employee_register);
    private EditText et_email = findViewById(R.id.et_email);
    private EditText et_password = findViewById(R.id.et_password);
    private ProgressBar pb = findViewById(R.id.pb);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //redirection to costumer registration
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent direct_to_costumer_register_page = new Intent(login.this, costumer_register.class);
              startActivity(direct_to_costumer_register_page);
              finish();
            }
        });

        //redirection to employee registration
        bt_employee_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent direct_to_employee_register_page = new Intent(login.this, employee_register.class);
                startActivity(direct_to_employee_register_page);
                finish();
            }
        });

        //login process
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                //TO DO - write code for login process

            }
        });

    }


}
