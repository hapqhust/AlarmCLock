package com.hapq.alarmclock;

import java.io.Serializable;

public class Alarm_Infor implements Serializable {
    private String time;
    private String date;
    private boolean ON;
    private int hour;
    private int minute;
    private String repeat;
    private String label;
    private String content;
    private String password;

    public Alarm_Infor() {

    }

    public Alarm_Infor(String time, String date, boolean ON) {
        this.time = time;
        this.date = date;
        this.ON = ON;
    }

    public String getTime() {
        return time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTime(int hour, int minute) {
        time = "";
        if (hour < 10)
            time += "0";
        time += hour + ":";
        if (minute < 10)
            time += "0";
        time += minute;

        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getON(){
        return ON;
    }

    public void setON(boolean ON){
        this.ON = ON;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
