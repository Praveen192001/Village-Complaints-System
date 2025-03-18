package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class PeopleFeedBackActivity extends AppCompatActivity {

    EditText title;
    EditText content;
    ImageView send;

    FirebaseFirestore firebaseFirestore;

    String lastFeedbackNo;
    int No;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplefeedback);

        firebaseFirestore = FirebaseFirestore.getInstance();

        title = findViewById(R.id.peopleFeedBackTitle);
        content = findViewById(R.id.peopleFeedBackContent);
        send = findViewById(R.id.peopleFeedBackSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Title=title.getText().toString();
                String Content=content.getText().toString();

                checkFieldEmptyOrNot(Title,Content);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PeopleFeedBackActivity.this,PeopleMainMenuActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    public void checkFieldEmptyOrNot(String title, String content) {

        if (title.length() != 0 && content.length() != 0) {

            checkDatabaseEmptyOrNot(title,content);

        } else {
            Toast.makeText(this, "Filed Can't Be Empty", Toast.LENGTH_SHORT).show();
        }


    }

    public void checkDatabaseEmptyOrNot(String title, String content) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("peopleName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");
        String userName = sharedPreferences.getString("PeopleUserName", "null");

        firebaseFirestore.collection(AdminName + " VAO" + " feedback").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            RetrieveAndStoreInDatabaseForVao(title,content);

                        } else {

                            int number = 1;

                            storingInDatabaseForVaoAccess(title,content,number);


                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleFeedBackActivity.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });

        firebaseFirestore.collection("people feedback").document(userName+" feedback").collection(userName+" feedback").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            retrieveAndStoreInDatabaseForPeopleAccess(title,content);

                        }else{

                            int number=1;

                            storingInDatabaseForPeopleAccess(title,content,number);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleFeedBackActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void storingInDatabaseForVaoAccess(String title, String content, int No){

        int Number= No;
        String feedbackNo=Integer.toString(Number);


        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");


        HashMap<String,Object> data=new HashMap<>();
        data.put("title",title);
        data.put("content",content);
        data.put("type","feedback");
        data.put("feedbackNo",feedbackNo);
        data.put("people name",userName);
        data.put("status","");


        firebaseFirestore.collection(AdminName+" VAO"+" feedback").document(userName+" people "+"feedback no "+feedbackNo).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(PeopleFeedBackActivity.this, "successfully stored", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleFeedBackActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void RetrieveAndStoreInDatabaseForVao(String title, String content){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(AdminName+" VAO"+" feedback").orderBy("feedbackNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                lastFeedbackNo=documentSnapshot.getString("feedbackNo");

//                                Toast.makeText(PeopleFeedBackActivity.this, "last feedback no in string "+ lastFeedbackNo, Toast.LENGTH_SHORT).show();

                            }

                            No=Integer.parseInt(lastFeedbackNo);
                            No=No+1;

//                            Toast.makeText(PeopleFeedBackActivity.this, "last feedback increased int value "+No, Toast.LENGTH_SHORT).show();


                        }

                        storingInDatabaseForVaoAccess(title,content,No);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(PeopleFeedBackActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void storingInDatabaseForPeopleAccess(String title, String content, int No){

        int Number= No;
        String feedbackNo =Integer.toString(Number);


        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");


        HashMap<String,Object>data=new HashMap<>();
        data.put("title",title);
        data.put("content",content);
        data.put("type","feedback");
        data.put("feedbackNo", feedbackNo);
        data.put("people name",userName);
        data.put("status","");
        data.put("admin name",AdminName);


        firebaseFirestore.collection("people feedback").document(userName+" feedback").collection(userName+" feedback").document("feedback no"+ feedbackNo).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(PeopleFeedBackActivity.this, "successfully stored", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleFeedBackActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });




    }

    public void retrieveAndStoreInDatabaseForPeopleAccess(String title, String content){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection("people feedback").document(userName+" feedback").collection(userName+" feedback").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                lastFeedbackNo=documentSnapshot.getString("feedbackNo");

                                Toast.makeText(PeopleFeedBackActivity.this, "last feedback no in string "+lastFeedbackNo, Toast.LENGTH_SHORT).show();

                            }

                            No=Integer.parseInt(lastFeedbackNo);
                            No=No+1;


                        }

                        storingInDatabaseForPeopleAccess(title,content,No);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(PeopleFeedBackActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

}



