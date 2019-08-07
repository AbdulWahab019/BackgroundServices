package com.example.backgroundservices.InstalledApps;

import android.graphics.drawable.Drawable;

class AppList {

    private String name;
    private Drawable icon;

    AppList(String name, Drawable icon) {
        this.name = name;
        this.icon = icon;
    }

    String getName() { return name; }

    Drawable getIcon() { return icon; }
}
