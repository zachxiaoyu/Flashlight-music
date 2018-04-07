package com.sit.a97022.Flashlight;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sit.a97022.Flashlight.R;

public class MainActivity extends AppCompatActivity {

    Camera.Parameters parameter=null;
    TextView tvShow;
    Camera camera=null;
    Button btnRequest;
    Button BtnRequest;
    Handler handler;
    boolean isOnclick=true;
    boolean isLight=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    tvShow = (TextView) findViewById(R.id.tv_show);
    BtnRequest = (Button) findViewById(R.id.btn_request1);
    btnRequest = (Button) findViewById(R.id.btn_request);
    btnRequest.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

               if (isOnclick)
               {
                   OpenLightOn();
                   new Light_light().start();
                   isOnclick=false;
               }

        }
    });
    BtnRequest.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            isLight=false;
            CloseLightOff();
        }
    });
    handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what ==456)
            {
                OpenLightOn();


            }
            return false;
        }
    });
    }
    class Light_light extends Thread{
        int time;
        @Override
        public void run() {
            while(isLight)
            {
                time = 50+(int )Math.random()*200;
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CloseLightOff();
                handler.sendEmptyMessage(456);
            }

        }
    }


    //请求网络数据
    private void getMovie() {


    }
    private void OpenLightOn()    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},1
                    );
        }else{
            //
            if(camera==null)
            {
                camera = Camera.open();
                camera.startPreview();
                Camera.Parameters parameter = camera.getParameters();
                parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameter);
                Log.d("smile","闪光灯打开");
            }

        }



    }

    private void CloseLightOff()   {

        try {
            if(camera.getParameters().getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                //关闭闪光灯
                Log.d("smile", "closeCamera()");
                camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(camera.getParameters());
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
//            Snackbar snackbar = Snackbar
//                    .make(this.findViewById(R.id.main1), "闪光灯未能打开！", Snackbar.LENGTH_LONG)
//                    .setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Toast.makeText(MainActivity.this,"闪光灯未在工作！",Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//
//            snackbar.show();
        }
    }

    @Override
    protected void onDestroy() {
      if(camera!=null)
      {
          CloseLightOff();
      }
        super.onDestroy();

    }
}
