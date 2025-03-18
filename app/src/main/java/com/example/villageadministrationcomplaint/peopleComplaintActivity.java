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

public class peopleComplaintActivity extends AppCompatActivity {

    EditText title;
    EditText content;
    ImageView send;

    FirebaseFirestore firebaseFirestore;

    String lastComplaintNo;
    int No;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplecomplaint);

        firebaseFirestore=FirebaseFirestore.getInstance();

        title= findViewById(R.id.peopleComplaintTitle);
        content = findViewById(R.id.peopleComplaintContent);
        send= findViewById(R.id.peopleComplaintSend);



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
        Intent intent=new Intent(peopleComplaintActivity.this,PeopleViewComplaintActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public void  checkFieldEmptyOrNot(String title, String content){

        if (title.length()!=0 && content.length()!=0 ){

            checkDatabaseEmptyOrNot(title,content);


        }else{
            Toast.makeText(this, "Filed Can't Be Empty", Toast.LENGTH_SHORT).show();
        }


    }

    public void checkDatabaseEmptyOrNot(String title,String content){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String AdminName=sharedPreferences.getString("adminName","null");
        String userName=sharedPreferences.getString("PeopleUserName","null");

        firebaseFirestore.collection(AdminName+" VAO").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            RetrieveAndStoreInDatabaseForVao(title,content);

                        }else{

                            int number=1;

                            storingInDatabaseForVaoAccess(title,content,number);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(peopleComplaintActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });



        firebaseFirestore.collection("people complaint").document(userName+" complaint").collection(userName+" complaint").get()
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

                        Toast.makeText(peopleComplaintActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void storingInDatabaseForVaoAccess(String title, String content, int No){

        int Number= No;
        String complaintNo=Integer.toString(Number);


        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");


        HashMap<String,Object>data=new HashMap<>();
        data.put("title",title);
        data.put("content",content);
        data.put("type","complaint");
        data.put("complaintNo",complaintNo);
        data.put("people name",userName);
        data.put("status","pending");


        firebaseFirestore.collection(AdminName+" VAO").document(userName+" people "+"complaint no "+complaintNo).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(peopleComplaintActivity.this, "successfully stored", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(peopleComplaintActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void RetrieveAndStoreInDatabaseForVao(String title, String content){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(AdminName+" VAO").orderBy("complaintNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                lastComplaintNo=documentSnapshot.getString("complaintNo");

                                Toast.makeText(peopleComplaintActivity.this, "last complaint no in string "+lastComplaintNo, Toast.LENGTH_SHORT).show();

                            }

                            No=Integer.parseInt(lastComplaintNo);
                            No=No+1;

//                            Toast.makeText(peopleComplaintActivity.this, "last complaint increased int value "+No, Toast.LENGTH_SHORT).show();


                        }

                        storingInDatabaseForVaoAccess(title,content,No);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(peopleComplaintActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void storingInDatabaseForPeopleAccess(String title, String content, int No){

        int Number= No;
        String complaintNo=Integer.toString(Number);


        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");


        HashMap<String,Object>data=new HashMap<>();
        data.put("title",title);
        data.put("content",content);
        data.put("type","complaint");
        data.put("complaintNo",complaintNo);
        data.put("people name",userName);
        data.put("status","pending");
        data.put("admin name",AdminName);


        firebaseFirestore.collection("people complaint").document(userName+" complaint").collection(userName+" complaint").document("complaint no"+complaintNo).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(peopleComplaintActivity.this, "successfully stored", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(peopleComplaintActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });




    }

    public void retrieveAndStoreInDatabaseForPeopleAccess(String title, String content){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection("people complaint").document(userName+" complaint").collection(userName+" complaint").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                lastComplaintNo=documentSnapshot.getString("complaintNo");

//                                Toast.makeText(peopleComplaintActivity.this, "last complaint no in string "+lastComplaintNo, Toast.LENGTH_SHORT).show();

                            }

                            No=Integer.parseInt(lastComplaintNo);
                            No=No+1;

//                            Toast.makeText(peopleComplaintActivity.this, "last complaint increased value is "+No, Toast.LENGTH_SHORT).show();

                        }

                        storingInDatabaseForPeopleAccess(title,content,No);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(peopleComplaintActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }


}

