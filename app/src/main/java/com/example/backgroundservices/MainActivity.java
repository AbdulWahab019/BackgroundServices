package com.example.backgroundservices;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Intent serviceIntent;
    TextView totalMemory, availableMemory, RAM;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalMemory = findViewById(R.id.totalMemory);
        availableMemory = findViewById(R.id.availableMemory);
        RAM = findViewById(R.id.RAMSize);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                availableMemory.setText(BackgroundService.getAvailableInternalMemorySize());
                totalMemory.setText(BackgroundService.getTotalInternalMemorySize());

            }
        }, 1000);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (serviceIntent == null) {
//            serviceIntent = new Intent(this, BackgroundService.class);
//            bindService(serviceIntent, myService, Context.BIND_AUTO_CREATE);
//            startService(serviceIntent);
//        }
//
//        availableMemory.setText(BackgroundService.getAvailableInternalMemorySize());
//        totalMemory.setText(BackgroundService.getTotalInternalMemorySize());
//    }

//    private ServiceConnection myService = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service){
//            BackgroundService.storageBinder binder = (BackgroundService.storageBinder) service;
//
//            binder.getService();
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) { }
//    };
}
