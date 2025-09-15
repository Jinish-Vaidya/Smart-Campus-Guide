package com.example.smartcampusguide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Locale;

public class SophosLabActivity extends AppCompatActivity {
    ListView imageDetailList;
    TextToSpeech textToSpeech;
    FirebaseListAdapter<DataModel> listAdapter;
    DataModel dataModel;
    String activityResult;
    int flag=0;
    String newDB="yes";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sophos_lab);
        Intent i=getIntent();
        activityResult=i.getStringExtra("activityResult");

        imageDetailList=findViewById(R.id.imageDetailList);

        Query query= FirebaseDatabase.getInstance().getReference().child("Labs").child(activityResult);
        FirebaseListOptions<DataModel> listOptions=new FirebaseListOptions.Builder<DataModel>()
                .setLayout(R.layout.custom_list_image_detail).setQuery(query,DataModel.class).build();
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(new Locale("HINDI", "IND"));
                }
            }
        });
        try {
            listAdapter = new FirebaseListAdapter<DataModel>(listOptions) {
                @Override
                protected void populateView(@NonNull View v, @NonNull DataModel model, int position) {
                    TextView t1, t2;
                    ImageView iv1,speaker;

                    t1 = (TextView) v.findViewById(R.id.listHeading);
                    t2 = (TextView) v.findViewById(R.id.listDetails);
                    iv1 = (ImageView) v.findViewById(R.id.listImage);
                    speaker=(ImageView)v.findViewById(R.id.textToSpeech);

                    dataModel = (DataModel) model;

                    t1.setText(dataModel.getHead());
                    t2.setText(dataModel.getBody());
                    Glide.with(v.getContext()).load(dataModel.getPhotoUrl()).into(iv1);
                    speaker.setImageResource(R.drawable.ic_microphone);
                    speaker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (flag==1) {
//                                speaker.setImageResource(speakers[i]);
                                speaker.setImageResource(R.drawable.ic_microphone);
                                flag=0;
                                textToSpeech.stop();
                            }
                            else
                            {
                                flag = 1;
                                speaker.setImageResource(R.drawable.ic_baseline_volume_off_24);
                                textToSpeech.speak("Specification of "+t1.getText().toString()+" are "+t2.getText().toString(), TextToSpeech.QUEUE_FLUSH,null);
                            }
                        }
                    });
                }
            };
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        try {
            imageDetailList.setAdapter(listAdapter);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        listAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listAdapter.stopListening();
        textToSpeech.stop();
    }
}