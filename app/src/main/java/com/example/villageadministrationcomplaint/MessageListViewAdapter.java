package com.example.villageadministrationcomplaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MessageListViewAdapter extends ArrayAdapter {
   Context context;
   int resource;
    ArrayList<MessageListViewData>messageListViewData=new ArrayList<>();
    public MessageListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<MessageListViewData>messageListViewData) {
        super(context, resource, messageListViewData);

        this.context=context;
        this.resource=resource;
        this.messageListViewData=messageListViewData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(context).inflate(resource,parent,false);

        TextView fromMsg=view.findViewById(R.id.MessageFromName);
        TextView msg=view.findViewById(R.id.msg);

        fromMsg.setText(messageListViewData.get(position).getMessageFromName());
        msg.setText(messageListViewData.get(position).getMsg());

        return view;
    }
}
