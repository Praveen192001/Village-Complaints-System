package com.example.villageadministrationcomplaint;

public class FeedBackArrayData {

    String FeedbackTitle;
    String FeedbackType;

    String FeedbackStatus;

    String FeedbackNo;

    public FeedBackArrayData(String feedbackTitle, String feedbackType, String feedbackStatus, String feedbackNo) {
        FeedbackTitle = feedbackTitle;
        FeedbackType = feedbackType;
        FeedbackStatus = feedbackStatus;
        FeedbackNo = feedbackNo;
    }

    public String getFeedbackTitle() {
        return FeedbackTitle;
    }

    public String getFeedbackType() {
        return FeedbackType;
    }

    public String getFeedbackStatus() {
        return FeedbackStatus;
    }

    public String getFeedbackNo() {
        return FeedbackNo;
    }
}
