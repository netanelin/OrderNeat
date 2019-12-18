package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registrarWindow extends AppCompatActivity {

///////////////init data base!///////////////////

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference dbRef = database.getReference("/Users");

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();


    ////////////**User object********/////////////////////////////////////////////
    public class User{
        public String   phone, email, password, id, fullName, key;

        public User(){ }

        public User(String phone,String password,String email , String id, String fullName){
            this.fullName = fullName;
            this.id = id;
            this.phone = phone;
            this.password = password;
            this.email = email;
            key = null;

        }
    }
////////////////////////////////////////////////////////////////




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_window);

        Button btC = findViewById(R.id.btnContinue);
        btC.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               EditText fn = findViewById(R.id.etFullName);
               EditText id = findViewById(R.id.etID);
               EditText Password = findViewById(R.id.etPassword);
               EditText Email = findViewById(R.id.etEmail);
               EditText Phone = findViewById(R.id.etPhone);
               ProgressBar pb = findViewById(R.id.progressBar);
///////////////////////////////////////////////////////////////////////////////////////////////////////////////


               /////////////////////////////////////////////////////////////////////

               User user = new User(Phone.getText().toString(), Password.getText().toString(), Email.getText().toString(),
                       id.getText().toString(), fn.getText().toString());

               user.key = dbRef.push().getKey();
               dbRef.child(id.getText().toString()).setValue(user, completionListener);
               pb.setVisibility(View.VISIBLE);
           }


               DatabaseReference.CompletionListener completionListener = new
                       DatabaseReference.CompletionListener() {
                           @Override
                           public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                               EditText Password = findViewById(R.id.etPassword);
                               EditText Email = findViewById(R.id.etEmail);

                               if (databaseError != null) {
                                   Toast.makeText(registrarWindow.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                               } else {
                                   Toast.makeText(registrarWindow.this, "Saved!!", Toast.LENGTH_LONG).show();
                                   mAuth.createUserWithEmailAndPassword(Email.getText().toString(),Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                       @Override
                                       public void onComplete(@NonNull Task<AuthResult> task) {
                                           if(task.isSuccessful()){
                                               Toast.makeText(registrarWindow.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                           }else {
                                               Toast.makeText(registrarWindow.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                           }
                                       }
                                   });
                                   Intent i = new Intent(registrarWindow.this,Menu.class);
                                   startActivity(i);
                                   finish();
                               }
                                               }

                                };

                            });
                    }
        }

   // User(String phone,String password,String email , String Id, String fullName)


