package com.example.cheerio.models;

public class Pain_data_model {
    String pain_intensity,pain_duration,day,year,month,pain_location;

    public Pain_data_model() {
    }

    public Pain_data_model(String pain_intensity, String pain_duration, String day, String year, String month, String pain_location) {
        this.pain_intensity = pain_intensity;
        this.pain_duration = pain_duration;
        this.day = day;
        this.year = year;
        this.month = month;
        this.pain_location = pain_location;
    }

    public String getPain_intensity() {
        return pain_intensity;
    }

    public void setPain_intensity(String pain_intensity) {
        this.pain_intensity = pain_intensity;
    }

    public String getPain_duration() {
        return pain_duration;
    }

    public void setPain_duration(String pain_duration) {
        this.pain_duration = pain_duration;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPain_location() {
        return pain_location;
    }

    public void setPain_location(String pain_location) {
        this.pain_location = pain_location;
    }
}
