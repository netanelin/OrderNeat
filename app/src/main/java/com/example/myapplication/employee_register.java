package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class employee_register extends AppCompatActivity {
    private static final String TAG = "registerNewEmailPassword";

    private Button _continue;
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_phone_number;
    private EditText et_email;
    private EditText et_password;
    private ProgressBar pb;
    private FirebaseAuth mAuth;
    private CheckBox cb_as_employee;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Map<String, Object> getUserUsMap(String first_name, String last_name, String phone_number, boolean employee){
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
        setContentView(R.layout.activity_employee_register);
        _continue = findViewById(R.id.btnContinue);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_phone_number = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        pb = findViewById(R.id.pb);
        cb_as_employee = findViewById(R.id.cb_as_employee);

        _continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                mAuth = FirebaseAuth.getInstance();

                FirebaseUser fbUser = mAuth.getCurrentUser();
                //if user isnt signed in
                if (fbUser == null) {
                    //creating new user in firebase authentication
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(employee_register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        //create new user info document on firestore
                                        //creating map representing the new user
                                        Map<String,Object> userM = getUserUsMap(et_first_name.getText().toString(),
                                                et_last_name.getText().toString(),
                                                et_phone_number.getText().toString(),
                                                cb_as_employee.isChecked());

                                        //creating new document named us the user uid in "Users" collection
                                        db.collection("Users").document(mAuth.getUid())
                                                .set(userM)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                                        Toast.makeText(employee_register.this,"welcome aboard!",Toast.LENGTH_LONG).show();
                                                        if(cb_as_employee.isChecked()){
                                                            //update the waiting for confirmation employees collection
                                                            Map<String, Object> employeeM = userM;
                                                            employeeM.remove("role");
                                                            employeeM.remove("approved");
                                                            employeeM.remove("phone_num");
                                                            //TODO pushing the new employee to a collection of waiting for approval
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error writing document", e);
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(employee_register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                //if user is already signed in
                } else {
                    Toast.makeText(employee_register.this, "you're already signed in", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
