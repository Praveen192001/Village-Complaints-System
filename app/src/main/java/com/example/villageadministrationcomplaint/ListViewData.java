package com.example.villageadministrationcomplaint;

public class ListViewData {

    String title;
    String complaint;
    String status;

    String Type="complaint";


    public ListViewData(String title, String complaint, String status) {
        this.title = title;
        this.complaint = complaint;
        this.status = status;

    }

    public String getTitle() {
        return title;
    }

    public String getComplaint() {
        return complaint;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return Type;
    }
}
