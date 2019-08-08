package com.example.backgroundservices.messageReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver {
    private static MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        assert data != null;
        Object[] pdus = (Object[]) data.get("pdus");
        assert pdus != null;
        for (Object pdus1 : pdus) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus1, data.getString("format"));
            String message = "Sender:   " + smsMessage.getDisplayOriginatingAddress()
                    + "\nMessage: " + smsMessage.getMessageBody();

            mListener.messageReceived(message);
        }
    }

    public static void bindListener(MessageListener listener){ mListener = listener; }
}
