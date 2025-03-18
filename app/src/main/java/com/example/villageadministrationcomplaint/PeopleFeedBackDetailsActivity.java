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

public class PeopleFeedBackDetailsActivity extends AppCompatActivity {

    TextView adminName, titleName,status,content;

    FirebaseFirestore firebaseFirestore;

    String Status;
    String Title;
    String Content;
    String AdminName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplefeedbackdetails);

        firebaseFirestore=FirebaseFirestore.getInstance();

        adminName=findViewById(R.id.PeopleFeedbackAdminName);
        titleName =findViewById(R.id.PeopleFeedbackTitleName);
        status=findViewById(R.id.PeopleFeedbackStatusName);
        content=findViewById(R.id.PeopleFeedbackContent);

        retrieveDetailsFromDatabase();

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PeopleFeedBackDetailsActivity.this,PeopleMainMenuActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    public void retrieveDetailsFromDatabase(){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");

        String clickedFeedbackTitle =getIntent().getStringExtra("clickedFeedbackTitle");

//        Toast.makeText(this, "clicked Title no is "+ clickedFeedbackTitle, Toast.LENGTH_SHORT).show();

        firebaseFirestore.collection("people feedback").document(userName+" feedback").collection(userName+" feedback").whereEqualTo("feedbackNo", clickedFeedbackTitle).get()
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

                        Toast.makeText(PeopleFeedBackDetailsActivity.this, "error"+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

