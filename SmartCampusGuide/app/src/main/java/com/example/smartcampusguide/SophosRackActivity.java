package com.example.smartcampusguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class SophosRackActivity extends AppCompatActivity {
    ListView imageDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sophos_rack);
        imageDetailList=findViewById(R.id.imageDetailList);
        String [] HeadingData = {"Sophos Rack"};
        String [] DetailData = {"CPU: Core i5 12500H, RAM : 32 GB DDR4, Storage : 2TB SSD, Operating System : Ubuntu 20.04, Web Server : Apache2, Database : MongoDB, Programming : ASP.NET6"};
        Integer [] ImageData={R.drawable.sophos_rack_image};
        Integer [] speakers={R.drawable.ic_microphone,R.drawable.ic_microphone,R.drawable.ic_microphone};
        Integer [] speakersOff={R.drawable.ic_baseline_volume_off_24,R.drawable.ic_baseline_volume_off_24,R.drawable.ic_baseline_volume_off_24};


        CustomListAdapter_DetailList customAdapter=new CustomListAdapter_DetailList(getApplicationContext(),HeadingData,DetailData,ImageData,speakers,speakersOff);
        try {
            imageDetailList.setAdapter(customAdapter);
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}