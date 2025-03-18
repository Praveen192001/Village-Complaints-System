package com.example.villageadministrationcomplaint;

public class PeopleChatMessageListViewData {

    String peopleChat;
    String adminChat;

    public PeopleChatMessageListViewData(String peopleChat, String adminChat) {
        this.peopleChat = peopleChat;
        this.adminChat = adminChat;
    }

    public String getPeopleChat() {
        return peopleChat;
    }

    public String getAdminChat() {
        return adminChat;
    }
}
