package com.example.villageadministrationcomplaint;

public class MessageListViewData {

    String MessageFromName;
    String msg;

    public MessageListViewData(String messageFromName, String msg) {
        MessageFromName = messageFromName;
        this.msg = msg;
    }

    public String getMessageFromName() {
        return MessageFromName;
    }

    public String getMsg() {
        return msg;
    }
}
