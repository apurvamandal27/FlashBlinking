package com.example.apurva.flashblinking;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    ToggleButton toggleButton,blink;
    android.hardware.Camera camera;
    android.hardware.Camera.Parameters parameters;
    Boolean isFlash=false,isOn=false;
    EditText speed;
    Button start_blink,stop_blink;
    boolean count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleButton);

        start_blink=findViewById(R.id.btn_blink);
        stop_blink=findViewById(R.id.btn_stop_blink);
        speed=findViewById(R.id.speed);


//        AdView mAdView=findViewById(R.id.myAdView);
//
//        //request ad to admob server
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        //add ad to Adview object
//        mAdView.loadAd(adRequest);



        start_blink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count=true;
                ThreadEx ex=new ThreadEx(v);
                ex.start();
            }
        });

        stop_blink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=false;
               ThreadEx2 ex2=new ThreadEx2(v);
               ex2.start();
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    toggleButton.setBackgroundResource(R.drawable.on);
                    flashLightOn(buttonView);
                }
                else {
                    toggleButton.setBackgroundResource(R.drawable.off);
                    flashLightOff(buttonView);
                }
            }
        });


    }
    //turn on flash light
    public void flashLightOn(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null; // Usually back camera is at 0 position.
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true);   //Turn ON
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //Turn off flash light

    public void flashLightOff(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null; // Usually back camera is at 0 position.
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, false);   //Turn ON
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }


    class ThreadEx extends Thread{
        View v;
        int seconds=1;
        ToneGenerator tone=new ToneGenerator(AudioManager.STREAM_MUSIC,100);
       public ThreadEx(View vw){
           v=vw;
           seconds= Integer.parseInt(speed.getText().toString());
           seconds=seconds*50;
       }
        @Override
        public void run() {



            for (int i=1;i<100;i++){
                if (count){

                    tone.startTone(ToneGenerator.TONE_CDMA_PIP,150);
                    flashLightOn(v);
                    flashLightOff(v);

                    try{
                        Thread.sleep(seconds);
                    }catch(InterruptedException e){}
                }
            }

        }
    }
    class ThreadEx2 extends Thread{
        View v;
        public ThreadEx2(View vw){
            v=vw;
        }

        @Override
        public void run() {
            flashLightOff(v);
        }
    }

}
