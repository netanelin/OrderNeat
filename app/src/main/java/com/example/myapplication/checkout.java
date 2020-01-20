package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class checkout extends AppCompatActivity{
    private static final String TAG = "registerNewEmailPassword";

    private TextView tv_total_price;
    private EditText et_card_number;
    private EditText et_comments;
    private Button bt_place_order;
    private ProgressBar pb;
    private FirebaseAuth mAuth;
    Map<String,Object> order;
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        tv_total_price = findViewById(R.id.tv_total_price);
        et_card_number = findViewById(R.id.et_card_number);
        et_comments = findViewById(R.id.et_comments);
        bt_place_order = findViewById(R.id.bt_place_order);
        pb = findViewById(R.id.pb);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
         order = (Map<String,Object>) bundle.getSerializable("order");
        String totalPrice = order.get("total").toString();
        tv_total_price.setText(totalPrice+" ILS");

        bt_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                mAuth = FirebaseAuth.getInstance();

                String uid = mAuth.getUid().toString();
                String card = et_card_number.getText().toString();
                String comments = et_comments.getText().toString();

                order.put("comments", comments);
                order.put("card_number", card);
                order.put("served",false);

                ////should be a barcode!!!
                order.put("table",1);
                ////////////////////////////


                db.collection("Orders").document(order.get("OID").toString()).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(checkout.this,"your order is on it's way!",Toast.LENGTH_LONG).show();

                        Intent direct_to_final_page = new Intent(checkout.this, finalPage.class);
                        startActivity(direct_to_final_page);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(checkout.this,"Error writing order info to DB",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        et_comments.addTextChangedListener(new TextWatcher() {
            boolean isOnTextChanged = true;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isOnTextChanged) {
                    isOnTextChanged = false;
                    setNotify();
                }

            }
        });


    }

    public void setNotify(){

        String channelId = "MY_CHANNEL_ID";

        //Step 1
        Intent landingIntent = new Intent();
        PendingIntent pendingLandingIntent = PendingIntent.getActivity(this, 0, landingIntent,0);

        //Step 2
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Step 3
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            NotificationChannel channel = new NotificationChannel(channelId, "Note!", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Help us know exactly what you need\n");
            notificationManager.createNotificationChannel(channel);
            // builder.setChannelId(channelId);
        }

        //Step4
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(getApplicationContext(), channelId);
        Notification notification = builder.setContentIntent(pendingLandingIntent)
                .setSmallIcon(R.drawable.chef)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("TIP!")
                .setContentText("Clear comment will help us to give U our best service").build();


        //Step 5
        notificationManager.notify(1, notification);
    }
}
