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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter {

    Context context;
    int resource;

    ArrayList<ListViewData>list=new ArrayList<>();


    public ListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ListViewData>list) {
        super(context, resource, list);

        this.context=context;
        this.resource=resource;
        this.list=list;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view= LayoutInflater.from(context).inflate(resource,parent,false);

        TextView title=view.findViewById(R.id.listViewData_Titile);
        TextView type=view.findViewById(R.id.listViewData_Type);
        TextView status=view.findViewById(R.id.listView_status);

        title.setText(list.get(position).getTitle());
        type.setText(list.get(position).getType());
        status.setText(list.get(position).getStatus());


        return view;
    }
}
