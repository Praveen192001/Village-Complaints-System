package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class AdminComplaintStatusActivity extends AppCompatActivity {

    TextView peopleName,titleName,status,content;
    ImageView done;

    FirebaseFirestore firebaseFirestore;


    String PeopleName;
    String Title;
    String Status;
    String Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admincomplainstatus);

        firebaseFirestore=FirebaseFirestore.getInstance();

        peopleName=findViewById(R.id.AdminComplaintUserName);
        titleName=findViewById(R.id.AdminComplaintTitleName);
        status=findViewById(R.id.AdminComplaintStatusName);
        content=findViewById(R.id.AdminComplaintContent);
        done=findViewById(R.id.AdminComplaintDone);

        retrieveDetailsFromDatabase();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                done();


            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AdminComplaintStatusActivity.this,AdminMainMenuActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    public void retrieveDetailsFromDatabase(){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        String adminName=sharedPreferences.getString("adminName","null");

        String clickedTitle=getIntent().getStringExtra("clickedTitle");

//        Toast.makeText(this, "clicked Title is "+clickedTitle, Toast.LENGTH_SHORT).show();

        firebaseFirestore.collection(adminName+" VAO").whereEqualTo("complaintNo",clickedTitle).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                PeopleName = documentSnapshot.getString("people name");
                                Title = documentSnapshot.getString("title");
                                Status = documentSnapshot.getString("status");
                                Content = documentSnapshot.getString("content");

                            }
                        }

                        peopleName.setText(PeopleName);
                        titleName.setText(Title);
                        status.setText(Status);
                        content.setText(Content);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminComplaintStatusActivity.this, "error"+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void done(){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        String adminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(adminName+" VAO").whereEqualTo("title",Title).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                String id=documentSnapshot.getId();
                                String name=documentSnapshot.getString("people name");
                                changeStatusInDatabase(id);
                                changeStatusInPeopleDatabase1(name);

                            }

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminComplaintStatusActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void   changeStatusInDatabase(String id){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        String adminName=sharedPreferences.getString("adminName","null");


        HashMap<String,Object>data=new HashMap<>();

        data.put("status","completed");

        firebaseFirestore.collection(adminName+" VAO").document(id).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AdminComplaintStatusActivity.this, "successfully status changed", Toast.LENGTH_SHORT).show();

                        status.setText("completed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminComplaintStatusActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void changeStatusInPeopleDatabase1(String name){

        Toast.makeText(this, "changeStatusInPeopleDatabase1 module work", Toast.LENGTH_SHORT).show();

        firebaseFirestore.collection("people complaint").document(name+" complaint").collection(name+" complaint").whereEqualTo("title",Title).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

//                            Toast.makeText(AdminComplaintStatusActivity.this, "if condition work", Toast.LENGTH_SHORT).show();

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String id=documentSnapshot.getId();
                                changeStatusInPeopleDatabase2(name,id);

                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminComplaintStatusActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void changeStatusInPeopleDatabase2(String name ,String id){

        HashMap<String,Object>data=new HashMap<>();
        data.put("status","completed");

        firebaseFirestore.collection("people complaint").document(name+" complaint").collection(name+" complaint").document(id).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AdminComplaintStatusActivity.this, "successfully changed people database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminComplaintStatusActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}

