package com.example.villageadministrationcomplaint;

public class AdminChatMessageListViewData {

    String AdminChat;
    String peopleChat;

    public AdminChatMessageListViewData(String adminChat, String peopleChat) {
        AdminChat = adminChat;
        this.peopleChat = peopleChat;
    }

    public String getAdminChat() {
        return AdminChat;
    }

    public String getPeopleChat() {
        return peopleChat;
    }
}
