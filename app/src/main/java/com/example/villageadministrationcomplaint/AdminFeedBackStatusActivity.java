package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class AdminFeedBackStatusActivity extends AppCompatActivity {

    TextView peopleName,titleName,status,content;

    FirebaseFirestore firebaseFirestore;

    String PeopleName;
    String Title;
    String Status;
    String Content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminfeedbackstatus);

        firebaseFirestore=FirebaseFirestore.getInstance();

        peopleName=findViewById(R.id.AdminFeedbackUserName);
        titleName=findViewById(R.id.AdminFeedbackTitleName);
        status=findViewById(R.id.AdminFeedbackStatusName);
        content=findViewById(R.id.AdminFeedbackContent);

        retrieveDetailsFromDatabase();


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AdminFeedBackStatusActivity.this,AdminMainMenuActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void retrieveDetailsFromDatabase(){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        String adminName=sharedPreferences.getString("adminName","null");

        String clickedFeedbackTitle =getIntent().getStringExtra("clickedFeedbackTitle");

//        Toast.makeText(this, "clicked Title is "+ clickedFeedbackTitle, Toast.LENGTH_SHORT).show();

        firebaseFirestore.collection(adminName+" VAO"+" feedback").whereEqualTo("feedbackNo", clickedFeedbackTitle).get()
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
                                String id=documentSnapshot.getId();

                                updateStatusInVao(id);
                                changeStatusInPeopleDatabase1(PeopleName);

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

                        Toast.makeText(AdminFeedBackStatusActivity.this, "error"+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void updateStatusInVao(String id){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        String adminName=sharedPreferences.getString("adminName","null");

//        String clickedFeedbackTitle =getIntent().getStringExtra("clickedFeedbackTitle");

        HashMap<String,Object>data=new HashMap<>();

        data.put("status","seen");

        firebaseFirestore.collection(adminName+" VAO"+" feedback").document(id).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AdminFeedBackStatusActivity.this, "changed successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminFeedBackStatusActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void changeStatusInPeopleDatabase1(String name){

        Toast.makeText(this, "changeStatusInPeopleDatabase1 module work", Toast.LENGTH_SHORT).show();

        String clickedFeedbackTitle =getIntent().getStringExtra("clickedFeedbackTitle");

        firebaseFirestore.collection("people feedback").document(name+" feedback").collection(name+" feedback").whereEqualTo("feedbackNo",clickedFeedbackTitle).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            Toast.makeText(AdminFeedBackStatusActivity.this, "if condition work", Toast.LENGTH_SHORT).show();

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

                        Toast.makeText(AdminFeedBackStatusActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void changeStatusInPeopleDatabase2(String name ,String id){

        HashMap<String,Object>data=new HashMap<>();
        data.put("status","seen");

        firebaseFirestore.collection("people feedback").document(name+" feedback").collection(name+" feedback").document(id).update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AdminFeedBackStatusActivity.this, "successfully changed people database", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminFeedBackStatusActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}

