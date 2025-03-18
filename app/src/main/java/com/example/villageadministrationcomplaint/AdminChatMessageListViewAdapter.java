package com.example.villageadministrationcomplaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;

public class AdminChatMessageListViewAdapter extends ArrayAdapter {
    Context context;
    int resource;

    ArrayList<AdminChatMessageListViewData>adminChatMessageListViewData=new ArrayList<>();

    public AdminChatMessageListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AdminChatMessageListViewData>adminChatMessageListViewData) {
        super(context, resource, adminChatMessageListViewData);

        this.context=context;
        this.resource=resource;
        this.adminChatMessageListViewData=adminChatMessageListViewData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(context).inflate(resource,parent,false);

        TextView adminChat=view.findViewById(R.id.AdminChat);
        TextView peopleChat=view.findViewById(R.id.peopleChat);

        adminChat.setText(adminChatMessageListViewData.get(position).getAdminChat());
        peopleChat.setText(adminChatMessageListViewData.get(position).getPeopleChat());

        return view;
    }
}
