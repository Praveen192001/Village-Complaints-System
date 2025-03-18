package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PeopleViewComplaintActivity extends AppCompatActivity {

    ListView listView;
    ImageView complaint;

    FirebaseFirestore firebaseFirestore;

    ArrayList<ListViewData> arrayList=new ArrayList<>();
    ListViewAdapter adapter;

    ArrayList<ArrayData> StoreData =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peopleviewcomplaint);

        firebaseFirestore=FirebaseFirestore.getInstance();

        listView=findViewById(R.id.peopleViewComplaintListView);
        complaint=findViewById(R.id.peopleViewComplaint);

        retrieveAndInsertInListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedTitle=StoreData.get(position).getStoreNo().toString();

//                Toast.makeText(PeopleViewComplaintActivity.this, "you clicked "+clickedTitle, Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(PeopleViewComplaintActivity.this,PeopleComplaintDetailsActivity.class);
                intent.putExtra("clickedTitle",clickedTitle);
                startActivity(intent);
                finish();
            }
        });

        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent=new Intent(PeopleViewComplaintActivity.this,peopleComplaintActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PeopleViewComplaintActivity.this,PeopleMainMenuActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    public void ListViewAdapter(String title, String complaint, String status){

        arrayList.add(new ListViewData(title,complaint,status));

        adapter=new ListViewAdapter(PeopleViewComplaintActivity.this,R.layout.listviewdata,arrayList);
        listView.setAdapter(adapter);



    }

    public void retrieveAndInsertInListView(){
        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");

        firebaseFirestore.collection("people complaint").document(userName+" complaint").collection(userName+" complaint").orderBy("complaintNo").get()
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
