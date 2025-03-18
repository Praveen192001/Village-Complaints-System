package com.example.villageadministrationcomplaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PeopleChatMessageListViewAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<PeopleChatMessageListViewData>peopleChatMessageListViewData=new ArrayList<>();
    public PeopleChatMessageListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PeopleChatMessageListViewData>peopleChatMessageListViewData) {
        super(context, resource, peopleChatMessageListViewData);

        this.context=context;
        this.resource=resource;
        this.peopleChatMessageListViewData=peopleChatMessageListViewData;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(context).inflate(resource,parent,false);

        TextView peopleChat=view.findViewById(R.id.people_peopleChat);
        TextView adminChat=view.findViewById(R.id.people_adminChat);

        peopleChat.setText(peopleChatMessageListViewData.get(position).getPeopleChat());
        adminChat.setText(peopleChatMessageListViewData.get(position).getAdminChat());

        return view;
    }
}
