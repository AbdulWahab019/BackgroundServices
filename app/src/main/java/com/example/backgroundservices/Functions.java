package com.example.backgroundservices;

import android.text.format.DateFormat;

public class Functions {

    public String toMinutesAndSeconds(long millis) {
        long minutes = (millis / 1000)  / 60;
        int seconds = (int) ((millis / 1000) % 60);

        return ""+minutes+":"+seconds;
    }

    public String convertDate(String dateInMilliseconds) {
        return DateFormat.format("dd/MM/yyyy hh:mm:ss", Long.parseLong(dateInMilliseconds)).toString();
    }

}
