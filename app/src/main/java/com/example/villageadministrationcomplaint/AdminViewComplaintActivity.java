package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminViewComplaintActivity extends AppCompatActivity {

    ListView listView;

    FirebaseFirestore firebaseFirestore;

    ArrayList<ListViewData>arrayList=new ArrayList<>();
    ListViewAdapter adapter;

    ArrayList<ArrayData> StoreData =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminviewcomplaint);

        firebaseFirestore=FirebaseFirestore.getInstance();

        listView=findViewById(R.id.adminViewComplaintListView);

        retrieveAndInsertInListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedTitle=StoreData.get(position).getStoreNo().toString();

//                Toast.makeText(AdminViewComplaintActivity.this, "you clicked "+clickedTitle, Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(AdminViewComplaintActivity.this,AdminComplaintStatusActivity.class);
                intent.putExtra("clickedTitle",clickedTitle);
               startActivity(intent);
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AdminViewComplaintActivity.this,AdminMainMenuActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    public void ListViewAdapter(String title, String complaint, String status){

        arrayList.add(new ListViewData(title,complaint,status));

        adapter=new ListViewAdapter(AdminViewComplaintActivity.this,R.layout.listviewdata,arrayList);
        listView.setAdapter(adapter);



    }

    public void retrieveAndInsertInListView(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        String adminName= sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(adminName+" VAO").orderBy("complaintNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                String title=documentSnapshot.getString("title");
                                String type=documentSnapshot.getString("type");
                                String status=documentSnapshot.getString("status");
                                String complaintNo=documentSnapshot.getString("complaintNo");


                                StoreData.add(new ArrayData(title,type,status,complaintNo));


                            }

                            for (int i=0; i<StoreData.size();i=i+1) {

                                String title= StoreData.get(i).getStoreTitle();

                                String type=StoreData.get(i).getStoreType();

                                String status=StoreData.get(i).getStoreStatus();

                                ListViewAdapter(title,type,status);

                            }


                        }
                    }
                });

    }

}

