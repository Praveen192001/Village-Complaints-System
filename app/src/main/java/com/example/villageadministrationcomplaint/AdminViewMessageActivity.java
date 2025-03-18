package com.example.villageadministrationcomplaint;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AdminViewMessageActivity extends AppCompatActivity {

    ListView listView;

    FirebaseFirestore firebaseFirestore;

    ArrayList<MessageListViewData> arrayList = new ArrayList<>();
    MessageListViewAdapter messageListViewAdapter;

    String chatNo;

    String gettingChatNo;

    ArrayList<String> peopleNameList = new ArrayList<>();
    ArrayList<String> gettingNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminviewmessage);

        firebaseFirestore = FirebaseFirestore.getInstance();

        gettingAndCheckingForLatestMsg1();

        listView = findViewById(R.id.ViewMessageListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String peopleName = arrayList.get(position).getMessageFromName();

                Toast.makeText(AdminViewMessageActivity.this, "people name is " + peopleName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AdminViewMessageActivity.this, AdminMessageActivity.class);
                intent.putExtra("peopleName", peopleName);
                getAllPeopleNameWhoChat();
                gettingChatNumberAndStoreInSharePreference2(peopleName);
                startActivity(intent);
                finish();
            }
        });




    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminViewMessageActivity.this, AdminMainMenuActivity.class);
        startActivity(intent);
        getAllPeopleNameWhoChat();
        finish();

        super.onBackPressed();
    }

    public void listViewAdapter(String formName,String msg) {

        arrayList.add(new MessageListViewData(formName,msg));

        messageListViewAdapter = new MessageListViewAdapter(AdminViewMessageActivity.this, R.layout.messagelistviewdata, arrayList);
        listView.setAdapter(messageListViewAdapter);

    }


    public void getAllPeopleNameWhoChat() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");

        firebaseFirestore.collection(AdminName + " chat with people name").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : list) {

                                String peopleName = documentSnapshot.getString("chatPeopleName");

//                                Toast.makeText(AdminViewMessageActivity.this, "people Name is "+peopleName, Toast.LENGTH_SHORT).show();

                                peopleNameList.add(peopleName);

                            }

                            gettingChatNumberAndStoreInSharePreference1();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminViewMessageActivity.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void gettingChatNumberAndStoreInSharePreference1() {


        for (int i = 0; i < peopleNameList.size(); i++) {

            String peopleName = peopleNameList.get(i);

//            Toast.makeText(this, "people name is " + peopleName, Toast.LENGTH_SHORT).show();

            gettingChatNumberAndStoreInSharePreference3(peopleName);

        }


    }

    public void gettingChatNumberAndStoreInSharePreference2(String userName) {

        SharedPreferences sharedPreferencesForChatNo = getApplicationContext().getSharedPreferences("chatNo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesForChatNo.edit();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");


        firebaseFirestore.collection(AdminName + " vao chat").document(AdminName + " and " + userName + " chat").collection(AdminName + " and " + userName + " chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String typedPerson=documentSnapshot.getString("who's typed");

                                if (typedPerson.equals("people")){

                                    chatNo = documentSnapshot.getString("chatNo");
                                }

                            }

//                            Toast.makeText(AdminViewMessageActivity.this, userName + " chat No " + chatNo, Toast.LENGTH_SHORT).show();
                            String spNo=sharedPreferencesForChatNo.getString(userName,"null");

                            editor.putString(userName, chatNo);
                            editor.apply();



                        }

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminViewMessageActivity.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void gettingChatNumberAndStoreInSharePreference3(String userName) {

        SharedPreferences sharedPreferencesForChatNo = getApplicationContext().getSharedPreferences("chatNo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesForChatNo.edit();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");


        firebaseFirestore.collection(AdminName + " vao chat").document(AdminName + " and " + userName + " chat").collection(AdminName + " and " + userName + " chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String typedPerson=documentSnapshot.getString("who's typed");

                                if (typedPerson.equals("people")){

                                    chatNo = documentSnapshot.getString("chatNo");
                                }

                            }

//                            Toast.makeText(AdminViewMessageActivity.this, userName + " chat No " + chatNo, Toast.LENGTH_SHORT).show();
                            String spNo=sharedPreferencesForChatNo.getString(userName,"null");

                            if (spNo.equals(chatNo)){
                                editor.putString(userName, chatNo);
                                editor.apply();
                            }


                        }

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminViewMessageActivity.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void gettingAndCheckingForLatestMsg1() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");

        firebaseFirestore.collection(AdminName + " chat with people name").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot : list) {

                                String peopleName = documentSnapshot.getString("chatPeopleName");

                                gettingNameList.add(peopleName);

                            }

                            gettingAndCheckingForLatestMsg2();

                        }else{
                            Toast.makeText(AdminViewMessageActivity.this, "collection is empty", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminViewMessageActivity.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void gettingAndCheckingForLatestMsg2(){

        for (int i=0; i<gettingNameList.size();i++){

            String name=gettingNameList.get(i);

            gettingBasicData(name);
        }

    }


    public void gettingBasicData(String userName) {


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");

//        Toast.makeText(this, "user Name "+userName, Toast.LENGTH_SHORT).show();


        firebaseFirestore.collection(AdminName + " vao chat").document(AdminName + " and " + userName + " chat").collection(AdminName + " and " + userName + " chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String typedPerson=documentSnapshot.getString("who's typed");

                                if (typedPerson.equals("people")){

                                    gettingChatNo = documentSnapshot.getString("chatNo");
                                }

                            }

                            checkNewMsgUsingSharePreference(userName, gettingChatNo);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminViewMessageActivity.this, "error " + e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void checkNewMsgUsingSharePreference(String userName, String No) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("chatNo", MODE_PRIVATE);


        String no = sharedPreferences.getString(userName, "null");

//        Toast.makeText(this, "database name " + userName, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "database no " + No, Toast.LENGTH_SHORT).show();

//        Toast.makeText(this, "sp no " + no, Toast.LENGTH_SHORT).show();

        if (!no.equals(No)) {

//                Toast.makeText(this, userName+" new msg", Toast.LENGTH_SHORT).show();

            listViewAdapter(userName,"new msg");

        } else {
//                Toast.makeText(this, userName+" no msg", Toast.LENGTH_SHORT).show();

            listViewAdapter(userName,"no msg");

        }
    }
}


