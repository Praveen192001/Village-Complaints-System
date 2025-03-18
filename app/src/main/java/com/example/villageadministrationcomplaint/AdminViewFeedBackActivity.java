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

public class AdminViewFeedBackActivity extends AppCompatActivity {

    ListView listView;

    FirebaseFirestore firebaseFirestore;

    ArrayList<ListViewData> arrayList=new ArrayList<>();
    ListViewAdapter adapter;

    ArrayList<FeedBackArrayData>feedBackList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminviewfeedback);

        firebaseFirestore=FirebaseFirestore.getInstance();

        listView=findViewById(R.id.adminViewFeedbackListView);

        retrieveAndInsertInListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String clickedFeedbackTitle =feedBackList.get(position).getFeedbackNo();

//                Toast.makeText(AdminViewFeedBackActivity.this, "you clicked "+ clickedFeedbackTitle, Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(AdminViewFeedBackActivity.this,AdminFeedBackStatusActivity.class);
                intent.putExtra("clickedFeedbackTitle", clickedFeedbackTitle);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(AdminViewFeedBackActivity.this,AdminMainMenuActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    public void ListViewAdapter(String title, String complaint, String status){

        arrayList.add(new ListViewData(title,complaint,status));

        adapter=new ListViewAdapter(AdminViewFeedBackActivity.this,R.layout.listviewdata,arrayList);
        listView.setAdapter(adapter);



    }

    public void retrieveAndInsertInListView(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        String adminName= sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(adminName+" VAO"+" feedback").orderBy("feedbackNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                String title=documentSnapshot.getString("title");
                                String type=documentSnapshot.getString("type");
                                String status=documentSnapshot.getString("status");
                                String feedbackNo =documentSnapshot.getString("feedbackNo");


                                feedBackList.add(new FeedBackArrayData(title,type,status, feedbackNo));


                            }

                            for (int i=0; i<feedBackList.size();i=i+1) {

                                String title= feedBackList.get(i).getFeedbackTitle();

                                String type=feedBackList.get(i).getFeedbackType();

                                String status=feedBackList.get(i).getFeedbackStatus();

                                ListViewAdapter(title,type,status);

                            }


                        }
                    }
                });

    }

}

