package com.example.backgroundservices.InstalledApps;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.backgroundservices.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;

import java.util.ArrayList;
import java.util.List;

public class InstalledAppsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    ListView appsList;
    ArrayList<AppList> installedApps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_apps);

        appsList = findViewById(R.id.appList);
        settingAPIClient();
        initialization();
    }

    private void initialization(){
        installedApps.clear();

        installedApps = getInstalledApps();
        AppAdapter installedAppAdapter = new AppAdapter(InstalledAppsActivity.this, installedApps);
        appsList.setAdapter(installedAppAdapter);
    }

    private ArrayList<AppList> getInstalledApps() {

        ArrayList<AppList> res = new ArrayList<>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ( !isSystemPackage(p) ) {
                String appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getPackageManager());
                res.add(new AppList(appName, icon));
            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo p) {
        return (p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    private void settingAPIClient() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .build();
    }

    @Override
    public void onPause(){ super.onPause(); }

    @Override
    public void onConnected(@Nullable Bundle bundle) { initialization(); }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
}
