package com.example.backgroundservices.CallLogs;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.backgroundservices.Functions;
import com.example.backgroundservices.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

import java.util.ArrayList;

public class CallLogActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ListView callsListView;
    private ArrayList<String> callLogsList = new ArrayList<>();
    private Functions fn = new Functions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);

        callsListView = findViewById(R.id.list);

        settingAPIClient();
        initialization();
    }

    private void initialization() {
        readCallLogs();
        showCallLogs();
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

    private void showCallLogs() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                callLogsList);

        callsListView.setAdapter(arrayAdapter);
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

    private void settingAPIClient() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();
    }

    public Cursor callLogs(ContentResolver cr){
        return cr.query(CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DEFAULT_SORT_ORDER);
    }

    @Override
    protected void onPause() { super.onPause(); }

    @Override
    public void onConnected(@Nullable Bundle bundle) { initialization(); }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
}
