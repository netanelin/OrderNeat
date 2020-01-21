package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import java.util.List;

public class QrScanner extends AppCompatActivity {
    CameraView camera_view;
    boolean isDetected ;
    Button btn_start_again;

    FirebaseVisionBarcodeDetectorOptions options;
    FirebaseVisionBarcodeDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);
        isDetected = false;
        Dexter.withActivity(this)
                .withPermissions(new String []{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        setupCamera();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private void setupCamera() {
        btn_start_again = findViewById(R.id.btn_scan_again);
        btn_start_again.setEnabled(isDetected);
        btn_start_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDetected = !isDetected;
            }
        });
        camera_view = findViewById(R.id.cameraView);
        camera_view.setLifecycleOwner(this);
        camera_view.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                processImage(getVisionImageFromFrame(frame));
            }
        });

        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
    }

    private void processImage(FirebaseVisionImage image) {
        if(!isDetected){
            detector.detectInImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                            processResult(firebaseVisionBarcodes);
                        }
                    });
        }
    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if(firebaseVisionBarcodes.size()>0){
            isDetected = true;
            btn_start_again.setEnabled(isDetected);
            for(FirebaseVisionBarcode item: firebaseVisionBarcodes){
                int value_type = item.getValueType();
                switch(value_type){
                    case FirebaseVisionBarcode.TYPE_TEXT:
                        createDialog(item.getRawValue());
                        break;
                    //here we can define behavior for more code types
                }
            }
        }
    }

    private void createDialog(String text){
        int table_number = Integer.parseInt(text.substring(text.indexOf(": ")+2));
        String message = "Are you sitting at table number " + table_number + "? Please confirm.";
        AlertDialog alertDialog = new AlertDialog.Builder(QrScanner.this).create();
        alertDialog.setTitle("Table check in\n");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(QrScanner.this,menu.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("table_number", table_number);
                        i.putExtras(bundle);
                        startActivity(i);
                        finish();
                    }
                });
        alertDialog.show();

    }

    private FirebaseVisionImage getVisionImageFromFrame(Frame frame) {
        byte[] data = frame.getData();
        FirebaseVisionImageMetadata metadata =  new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setHeight(frame.getSize().getHeight())
                .setWidth(frame.getSize().getWidth())
                .build();
        return FirebaseVisionImage.fromByteArray(data, metadata);
    }
}
