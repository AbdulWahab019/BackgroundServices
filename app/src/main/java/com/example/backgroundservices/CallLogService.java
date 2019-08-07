package com.example.backgroundservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CallLogService extends Service {
    final IBinder callLogBinder = new CallBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return callLogBinder;
    }

    class CallBinder extends Binder {
        CallLogService getService(){
            return CallLogService.this;
        }
    }
}
