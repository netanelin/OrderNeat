package com.example.myapplication;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class login_window extends AppCompatActivity {

    Button button;
    ProgressBar pb;
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_window);

        button = findViewById(R.id.button2);
        pb = findViewById(R.id.progressBar2);
//////////////////////////////////////////////////////////////////////////////
        final Button logIn = findViewById(R.id.button);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etEmail = findViewById(R.id.editText);
                EditText etPassword = findViewById(R.id.editText2);
                pb.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                        .addOnCompleteListener(login_window.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(login_window.this, "signInWithEmail:success",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    ///Admin check!!
                                    if(user.getEmail().equals("admin@admin.com")){
                                        startActivity(new Intent(login_window.this, Admin.class));
                                        finish();
                                    }
                                    else {
                                        startActivity(new Intent(login_window.this, Menu.class));
                                        finish();
                                        }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(login_window.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(View.INVISIBLE);

                                }
                            }
                        });
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity( new Intent(login_window.this,registrarWindow.class));
        }
    });

    }
}