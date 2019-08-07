package com.example.backgroundservices;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CallLogActivity extends AppCompatActivity {
    private ListView callsListView;
    private ArrayList<String> callLogsList = new ArrayList<>();
    private Functions fn = new Functions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);

        callsListView = findViewById(R.id.list);

        initialization();
    }

    private void initialization() {
        readCallLogs();
        showCallLogs();
    }

    private void showCallLogs() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                                                     android.R.layout.simple_list_item_1,
                                                     callLogsList);

        callsListView.setAdapter(arrayAdapter);
    }

    private void readCallLogs(){
        callLogsList.clear();
        Cursor callLogCursor = callLogs(getContentResolver());

        if (callLogCursor != null){
            while(callLogCursor.moveToNext()){
                String name = callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));

                String contactNumber = callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.NUMBER));

                String dateTimeMillis = callLogCursor.getString(callLogCursor.getColumnIndex(CallLog.Calls.DATE));

                long durationMillis = callLogCursor.getLong(callLogCursor.getColumnIndex(CallLog.Calls.DURATION));

                /*Call Type – Incoming, Outgoing, Missed*/
                int callTypeInt = callLogCursor.getInt(callLogCursor.getColumnIndex(CallLog.Calls.TYPE));

                String duration = fn.toMinutesAndSeconds(durationMillis*1000);
                String dateString = fn.convertDate(dateTimeMillis);
                String callType = getCallType(callTypeInt);

                CallLogModel log = new CallLogModel(name, contactNumber, duration, dateString, callType);
                callLogsList.add(log.toString());
            }
            callLogCursor.close();
        }
    }

    private String getCallType(int type){
        switch (type) {
            case CallLog.Calls.OUTGOING_TYPE:
                return "Outgoing";
            case CallLog.Calls.INCOMING_TYPE:
                return "Incoming";
            case CallLog.Calls.MISSED_TYPE:
                return "Missed";
        }
        return null;
    }

    public Cursor callLogs(ContentResolver cr){
        return cr.query(CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DEFAULT_SORT_ORDER);
    }
}
