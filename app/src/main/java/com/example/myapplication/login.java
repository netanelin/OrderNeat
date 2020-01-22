package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;


public class login extends AppCompatActivity {
    private FirebaseAuth user_auth ;
    private Button bt_login ;
    private Button bt_register ;
    private EditText et_email ;
    private EditText et_password ;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_auth = FirebaseAuth.getInstance();
        bt_login = findViewById(R.id.bt_login);
        bt_register = findViewById(R.id.bt_sign_up);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        pb = findViewById(R.id.pb);


        //redirection to costumer registration
        bt_register.setOnClickListener(v -> {
          Intent direct_to_costumer_register_page = new Intent(login.this, register.class);
          startActivity(direct_to_costumer_register_page);
          finish();
        });

        //login process
        bt_login.setOnClickListener(v -> {
            login();

        });

    }


    public void login(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if(email.isEmpty()) //if email is empty
        {
            et_email.setError("email is required");
            et_email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) //if the email is not valid
        {
            et_email.setError("please enter a valid email");
            et_email.requestFocus();
            return;
        }

        if(password.isEmpty()) //if password is empty
        {
            et_password.setError("password is required");
            et_password.requestFocus();
            return;
        }

        pb.setVisibility(View.VISIBLE);
        user_auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(login.this, task -> {
                    //valid user
                    if(task.isSuccessful()){
                        //show success toast
                        Toast.makeText(login.this, "signed in successfully!",Toast.LENGTH_LONG).show();
                        //get user uid from auth
                        FirebaseUser  user = user_auth.getCurrentUser();
                        //role specific redirection
                        //get user document named as the user auth uid
                        DocumentReference docRef = db.collection("Users").document(user.getUid());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                //if we managed to contact firestore and got an answer
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    //if a user info file named us the auth uid is found
                                    if (document.exists()) {
                                        //get the document as a map
                                        Map m = document.getData();
                                        //get the role from the map
                                        Object oRole = m.get("role");
                                        //cast to string
                                        String sRole = (String)oRole;
                                        //if user is a costumer direct him to menu (in the future direct to qrscanner)
                                        if(sRole.equals("costumer")){
                                            Intent direct_to_QR__scanner = new Intent(login.this, QrScanner.class);
                                            startActivity(direct_to_QR__scanner);
                                            finish();
                                        }

                                        //if user is an employee direct him to orders_panel
                                        if(sRole.equals("employee")){

                                            if((boolean)m.get("approved")) {
                                                Intent direct_to_orders_panel = new Intent(login.this, orders_panel.class);
                                                startActivity(direct_to_orders_panel);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(login.this, "Waiting for manager approval", Toast.LENGTH_LONG).show();
                                                pb.setVisibility(View.INVISIBLE);
                                            }
                                        }

                                        //if user is a manager direct him to manager_panel
                                        if(sRole.equals("manager")){
                                            Intent direct_to_manager_panel = new Intent(login.this, manager_panel.class);
                                            startActivity(direct_to_manager_panel);
                                            finish();
                                        }
                                    } else {
                                        System.out.println("problem finding user role");
                                        Toast.makeText(login.this, "problem finding user role",Toast.LENGTH_LONG).show();
                                        pb.setVisibility(View.INVISIBLE);


                                    }
                                    //if we didnt manage to contact firestore
                                } else {
                                    ///--------debug--------
                                    System.out.println("cant reach firestore");
                                    ///--------debug--------

                                    Toast.makeText(login.this, "cant reach firestore",Toast.LENGTH_LONG).show();
                                    pb.setVisibility(View.INVISIBLE);


                                }

                            }
                        });
                    }
                    //invalid user
                    else{
                        //show fail toast
                        Toast.makeText(login.this, "invalid user",Toast.LENGTH_LONG).show();
                        pb.setVisibility(View.INVISIBLE);

                    }
                });
    }


}
