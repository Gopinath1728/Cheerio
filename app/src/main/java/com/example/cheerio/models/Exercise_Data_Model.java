package com.example.cheerio.models;

import java.util.List;

public class Exercise_Data_Model {
    String name;
    List<Data_model> data_modelList;

    public Exercise_Data_Model() {
    }

    public Exercise_Data_Model(String name, List<Data_model> data_modelList) {
        this.name = name;
        this.data_modelList = data_modelList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Data_model> getData_modelList() {
        return data_modelList;
    }

    public void setData_modelList(List<Data_model> data_modelList) {
        this.data_modelList = data_modelList;
    }
}
