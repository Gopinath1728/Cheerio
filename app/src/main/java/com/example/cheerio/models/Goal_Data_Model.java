package com.example.cheerio.models;

public class Goal_Data_Model {
    String goal,time,date;
    Boolean status;

    public Goal_Data_Model() {
    }

    public Goal_Data_Model(String goal, String time, String date, Boolean status) {
        this.goal = goal;
        this.time = time;
        this.date = date;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
