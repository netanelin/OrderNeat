package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private static final String TAG = "registerNewEmailPassword";

    private Button sign_up;
    private Switch employee_switch;
    private EditText et_full_name;
    private EditText et_phone_number;
    private EditText et_email;
    private EditText et_password;

    private ProgressBar pb;

    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Map<String, Object> getUserAsMap(String first_name, String last_name, String phone_number, boolean employee){
        Map<String, Object> m =new HashMap<>();
        m.put("first_name", first_name);
        m.put("last_name", last_name);
        m.put("phone_num", phone_number);
        if(employee){
            m.put("role", "employee");
            m.put("approved", false);
        }else{
            m.put("role", "costumer");
        }

        return m;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up = findViewById(R.id.bt_sign_up);
        et_full_name = findViewById(R.id.et_full_name);
        et_phone_number = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        pb = findViewById(R.id.pb);
        employee_switch = findViewById(R.id.switch_employee);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
    }

    public void register(){

        String full_name = et_full_name.getText().toString();

        String phone_number  = et_phone_number.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

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

        if(password.length() < 6) //the password need to be over than 5
        {
            et_password.setError("minimum length of password should be 6");
            et_password.requestFocus();
            return;
        }
        if(full_name.isEmpty()) //if name is empty
        {
            et_full_name.setError("Full name is required");
            et_full_name.requestFocus();
            return;
        }

        if(full_name.indexOf(' ') == -1) //if user havnt insert 2 names
        {
            et_full_name.setError("Full name is required");
            et_full_name.requestFocus();
            return;
        }

        if(phone_number.isEmpty()) //if email is empty
        {
            et_phone_number.setError("phone number is required");
            et_phone_number.requestFocus();
            return;
        }

        if(phone_number.length() < 10) //the password need to be over than 5
        {
            et_phone_number.setError("minimum length of phone number should be 10");
            et_phone_number.requestFocus();
            return;
        }


        if(email.isEmpty()) //if email is empty
        {
            et_email.setError("email is required");
            et_email.requestFocus();
            return;
        }





        String first_name = full_name.substring(0, full_name.indexOf(' '));
        String last_name  = full_name.substring(full_name.indexOf(' ')+1);
       mAuth = FirebaseAuth.getInstance();
        pb.setVisibility(View.VISIBLE);
       //creating new user in firebase authentication
       mAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           // Sign in success
                           Log.d(TAG, "createUserWithEmail:success");
                           FirebaseUser user = mAuth.getCurrentUser();

                           //create new user info document on firestore
                           //creating map representing the new user
                           Map<String,Object> userM = getUserAsMap(first_name,
                                   last_name,
                                   et_phone_number.getText().toString(),
                                   employee_switch.isChecked());

                           //creating new document named as the user uid in "Users" collection
                           db.collection("Users").document(mAuth.getUid())
                                   .set(userM)
                                   .addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           Log.d(TAG, "DocumentSnapshot successfully written!");
                                           Toast.makeText(register.this,"welcome aboard!",Toast.LENGTH_LONG).show();

                                           //redirect the new user to next activity by role
                                           //redirect employee to order managment
                                           if(employee_switch.isChecked()){
                                               Intent direct_to_orders_panel = new Intent(register.this, login.class);
                                               startActivity(direct_to_orders_panel);
                                               Toast.makeText(register.this,"Please wait for approval",Toast.LENGTH_LONG).show();
                                               finish();
                                           }else{
                                               Intent direct_to_menu_page = new Intent(register.this, menu.class);
                                               startActivity(direct_to_menu_page);
                                               finish();
                                           }

                                       }

                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           Log.w(TAG, "Error writing document", e);
                                           Toast.makeText(register.this,"Error writing user info to DB",Toast.LENGTH_LONG).show();
                                       }
                                   });
                       } else {
                           // If sign in fails, display a message to the user.
                           Log.w(TAG, "createUserWithEmail:failure", task.getException());
                           Toast.makeText(register.this, "createUserWithEmail:failure.", Toast.LENGTH_SHORT).show();
                       }
                   }
               });

   }
        });
   }
}
