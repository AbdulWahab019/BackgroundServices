package com.example.backgroundservices.CallLogs;

@SuppressWarnings({"NullableProblems", "WeakerAccess"})
public class CallLogModel {
    private String name;
    private String contactNumber;
    private String duration;
    private String date;
    private String type;

    CallLogModel(String name, String contactNumber, String duration, String date, String type) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.duration = duration;
        this.date = date;
        this.type = type;
    }

    public String getName() { return name; }

    public String getContactNumber() { return contactNumber; }

    public String getDuration() { return duration; }

    public String getDate() { return date; }

    public String getType() { return type; }

    public String toString(){
        return "Name: "+getName()+
                "\nContactNumber: " + getContactNumber()+
                "\nDuration: " + getDuration()+
                "\nDate: " + getDate()+
                "\nType: " + getType();
    }
}
