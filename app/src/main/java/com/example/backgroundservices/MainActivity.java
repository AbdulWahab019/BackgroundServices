package com.example.backgroundservices;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.backgroundservices.CallLogs.CallLogActivity;
import com.example.backgroundservices.InstalledApps.InstalledAppsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    TextView totalMemory, availableMemory, RAM, totalStepsToday;
    StorageService service = new StorageService();
    Handler handler = new Handler();
    Value val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();
    }

    private void initialization(){
        totalMemory = findViewById(R.id.totalMemory);
        availableMemory = findViewById(R.id.availableMemory);
        RAM = findViewById(R.id.RAMSize);
        totalStepsToday = findViewById(R.id.stepsToday);

        // Setting Up Google Fit API
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();

        handlingData();
    }

    private void handlingData(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                availableMemory.setText(StorageService.getAvailableInternalMemorySize());
                totalMemory.setText(StorageService.getTotalInternalMemorySize());
                RAM.setText(service.getTotalRamSize(getApplicationContext()));
                totalStepsToday.setText(String.format("%s", val));
                runTodaysStepCount();

                handler.postDelayed(this, 1000);
            }
        },1000);
    }

    private void runTodaysStepCount(){
        ViewTodaysStepCountTask.execute(new Runnable() {
            @Override
            public void run() {
                displayStepDataForToday();
            }
        });
    }

    @Override
    public void onClick(View v) { }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) { handlingData(); }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private void displayStepDataForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
        showDataSet(Objects.requireNonNull(result.getTotal()));
    }

    @SuppressLint("StaticFieldLeak")
    private class ViewTodaysStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayStepDataForToday();
            return null;
        }
    }

    private void showDataSet(DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                val = dp.getValue(field);
            }
        }
    }

    public void readCallLogs(View view) {
        Intent intent = new Intent(MainActivity.this, CallLogActivity.class);
        startActivity(intent);
    }

    public void checkInstalledApps(View view) {
        Intent intent = new Intent(MainActivity.this, InstalledAppsActivity.class);
        startActivity(intent);
    }
}