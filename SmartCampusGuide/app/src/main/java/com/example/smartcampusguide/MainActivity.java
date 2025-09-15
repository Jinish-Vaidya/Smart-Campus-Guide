package com.example.smartcampusguide;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartcampusguide.ml.ModelEfficientnetb0;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 101;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    private ImageView imageView;
    private Button select,camera,detail;
    private TextView tv;
    private Bitmap img;
    String predictedImage=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView) findViewById(R.id.imageView);
        tv=(TextView) findViewById(R.id.textView);
        select=(Button) findViewById(R.id.select);
        camera=(Button) findViewById(R.id.camera);
        detail=(Button) findViewById(R.id.getDetails);
        if (savedInstanceState != null) {
            img = (Bitmap) savedInstanceState.getParcelable("image");
            imageView.setImageBitmap(img);
            predict();
        }

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,100);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askcamerapermission();
            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (predictedImage==null)
                    {
                        Toast.makeText(MainActivity.this, "No Output Found", Toast.LENGTH_SHORT).show();
                    }
                    else{/*if (predictedImage=="Sophos Lab"){*/
                        Intent i = new Intent(getApplicationContext(), SophosLabActivity.class);
                        i.putExtra("activityResult",predictedImage);
                        startActivity(i);
                    }
//                    else if (predictedImage=="Sophos Rack"){
//                        Intent i = new Intent(getApplicationContext(), SophosRackActivity.class);
//                        startActivity(i);
//                    }
//                    else if (predictedImage=="Apple Lab"){
//                        Intent i = new Intent(getApplicationContext(), AppleLabActivity.class);
//                        startActivity(i);
//                    }
//                    else if (predictedImage=="Virtual Reality Lab"){
//                        Intent i = new Intent(getApplicationContext(), VirtualRealityLabActivity.class);
//                        startActivity(i);
//                    }
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "Try method called", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void predict(){
        if (img == null) {
            tv.setText("Please select image");
            int orientation = getResources().getConfiguration().orientation;
            if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape
                int landscape = getResources().getIdentifier("img11", "drawable", this.getPackageName());
                imageView.setImageResource(landscape);
            } else {
                // In portrait
                int portrait = getResources().getIdentifier("no_image", "drawable", this.getPackageName());
                imageView.setImageResource(portrait);
            }
        } else {
            img = Bitmap.createScaledBitmap(img, 224, 224, true);
            try {
                ModelEfficientnetb0 model = ModelEfficientnetb0.newInstance(getApplicationContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                img = img.copy(Bitmap.Config.ARGB_8888,true) ;
                tensorImage.load(img);
                ByteBuffer byteBuffer = tensorImage.getBuffer();
                inputFeature0.loadBuffer(byteBuffer);

                // Runs model inference and gets result.
                ModelEfficientnetb0.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                // Releases model resources if no longer used.
                model.close();
                if(outputFeature0.getFloatArray()[0]>0.99 || outputFeature0.getFloatArray()[1]>0.99||outputFeature0.getFloatArray()[2]>0.99||outputFeature0.getFloatArray()[2]>0.99 ||outputFeature0.getFloatArray()[3]>0.99)
                {
                    if (outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[1]) {
                        if (outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[2]) {
                            if (outputFeature0.getFloatArray()[0] > outputFeature0.getFloatArray()[3]) {
                                tv.setText("Apple Lab");
                                predictedImage="Apple Lab";
                            } else {
                                tv.setText("Virtual Reality Lab");
                                predictedImage="Virtual Reality Lab";
                            }
                        } else {
                            if (outputFeature0.getFloatArray()[2] > outputFeature0.getFloatArray()[3]) {
                                tv.setText("Sophos Rack");
                                predictedImage="Sophos Rack";
                            } else {
                                tv.setText("Virtual Reality Lab");
                                predictedImage="Virtual Reality Lab";
                            }
                        }
                    } else {
                        if (outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[2]) {
                            if (outputFeature0.getFloatArray()[1] > outputFeature0.getFloatArray()[3]) {
                                tv.setText("Sophos Lab");
                                predictedImage="Sophos Lab";
                            } else {
                                tv.setText("Virtual Reality Lab");
                                predictedImage="Virtual Reality Lab";
                            }
                        } else {
                            if (outputFeature0.getFloatArray()[2] > outputFeature0.getFloatArray()[3]) {
                                tv.setText("Sophos Rack");
                                predictedImage="Sophos Rack";
                            } else {
                                tv.setText("Virtual Reality Lab");
                                predictedImage="Virtual Reality Lab";
                            }
                        }
                    }
                }
                else {
                    tv.setText("No Match Found");
                    predictedImage = null;
                }
            }
            catch (IOException e) {
//                 TODO Handle the exception
            }
        }
    }
    private void askcamerapermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else
        {
            openCamera();
        }
    }

    private void openCamera() {
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
            else
            {
                Toast.makeText(this, "Camera permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if(requestCode==100)
        {
            imageView.setImageURI(data.getData());
            Uri uri=data.getData();
            try {
                img= MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                predict();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode==CAMERA_REQUEST_CODE) {
            img = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(img);
            predict();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (img != null) {
            outState.putParcelable("image", img);
        }
    }
}