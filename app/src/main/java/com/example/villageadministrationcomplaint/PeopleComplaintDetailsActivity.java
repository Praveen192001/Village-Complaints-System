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

import java.util.List;

public class PeopleComplaintDetailsActivity extends AppCompatActivity {

    TextView adminName,titleName,status,content;

    FirebaseFirestore firebaseFirestore;

    String Status;
    String Title;
    String Content;
    String AdminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplecomplaintdetails);

        firebaseFirestore=FirebaseFirestore.getInstance();

        adminName=findViewById(R.id.PeopleComplaintAdminName);
        titleName=findViewById(R.id.PeopleComplaintTitleName);
        status=findViewById(R.id.PeopleComplaintStatusName);
        content=findViewById(R.id.PeopleComplaintContent);

        retrieveDetailsFromDatabase();

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PeopleComplaintDetailsActivity.this,PeopleMainMenuActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    public void retrieveDetailsFromDatabase(){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");

        String clickedTitle=getIntent().getStringExtra("clickedTitle");

        Toast.makeText(this, "clicked Title is "+clickedTitle, Toast.LENGTH_SHORT).show();

        firebaseFirestore.collection("people complaint").document(userName+" complaint").collection(userName+" complaint").whereEqualTo("complaintNo",clickedTitle).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                AdminName = documentSnapshot.getString("admin name");
                                Title = documentSnapshot.getString("title");
                                Status = documentSnapshot.getString("status");
                                Content = documentSnapshot.getString("content");

                            }
                        }

                        adminName.setText(AdminName);
                        titleName.setText(Title);
                        status.setText(Status);
                        content.setText(Content);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleComplaintDetailsActivity.this, "error"+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

