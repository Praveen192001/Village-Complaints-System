package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;

public class PeopleMessageActivity extends AppCompatActivity {

    EditText peopleChatText;
    ImageView send;
    ListView listView;

    FirebaseFirestore firebaseFirestore;

    ArrayList<PeopleChatMessageListViewData>arrayList=new ArrayList<>();
    PeopleChatMessageListViewAdapter chatMessageListViewAdapter;

    String lastChatNumber;
    String ChatPeopleName="null";

    String chatNo="null";

    String NumberForHandler;
    String SpLastNUmber="null";

    Handler handler=new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        firebaseFirestore=FirebaseFirestore.getInstance();

        retrieveDataInDatabase();

        peopleChatText=findViewById(R.id.MessageChatText);
        send=findViewById(R.id.MessageSend);
        listView=findViewById(R.id.MessageListView);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String peopleChat= peopleChatText.getText().toString();

                CheckAndStorePeopleNameForAdminViewMessages();

                retrieveForLastChatNumberAndStoreInDatabase(peopleChat);

            }
        });

        handler();

    }

    @Override
    public void onBackPressed() {

        SharedPreferences sharedPreferencesForChat=getApplicationContext().getSharedPreferences("peopleChatNo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferencesForChat.edit();

        handler.removeCallbacks(runnable);

        editor.clear();
        editor.apply();

        Intent intent=new Intent(PeopleMessageActivity.this,PeopleMainMenuActivity.class);
        startActivity(intent);
        finish();


        super.onBackPressed();
    }

    public void ListViewAdapter(String peopleChat, String AdminChat){

        arrayList.add(new PeopleChatMessageListViewData(peopleChat,AdminChat));

        chatMessageListViewAdapter=new PeopleChatMessageListViewAdapter(PeopleMessageActivity.this,R.layout.peoplechatmessagelistviewdata,arrayList);
        listView.setAdapter(chatMessageListViewAdapter);



    }

    public void StorePeopleTextFromDatabase(String peopleChat,String no){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        int No=Integer.parseInt(no);
        No=No+1;
        chatNo=Integer.toString(No);


        HashMap<String,Object>data=new HashMap<>();
        data.put("peopleChat",peopleChat);
        data.put("adminChat","");
        data.put("chatNo",chatNo);
        data.put("who's typed","people");


        firebaseFirestore.collection(AdminName+" vao chat").document(AdminName+" and "+userName+" chat").collection(AdminName+" and "+userName+" chat").document(userName+" chat "+chatNo).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(PeopleMessageActivity.this, "successfully Stored", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleMessageActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void retrieveForLastChatNumberAndStoreInDatabase(String peopleChat){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(AdminName+" vao chat").document(AdminName+" and "+userName+" chat").collection(AdminName+" and "+userName+" chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                lastChatNumber=documentSnapshot.getString("chatNo");

                            }

//                            Toast.makeText(PeopleMessageActivity.this, "last chat number is "+lastChatNumber, Toast.LENGTH_SHORT).show();

                            StorePeopleTextFromDatabase(peopleChat,lastChatNumber);


                        }else{

                            String number="0";
                            StorePeopleTextFromDatabase(peopleChat,number);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleMessageActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public  void retrieveDataInDatabase(){
        SharedPreferences sharedPreferencesForChat=getApplicationContext().getSharedPreferences("peopleChatNo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferencesForChat.edit();

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(AdminName+" vao chat").document(AdminName+" and "+userName+" chat").collection(AdminName+" and "+userName+" chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String peopleChat=documentSnapshot.getString("peopleChat");
                                String adminChat=documentSnapshot.getString("adminChat");
                                SpLastNUmber=documentSnapshot.getString("chatNo");

                                ListViewAdapter(peopleChat,adminChat);
                            }
                            editor.putString("DbChatNo",SpLastNUmber);
                            editor.apply();

                        }else{

                            editor.putString("DbChatNo",SpLastNUmber);
                            editor.apply();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleMessageActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void storePeopleChatNameForAdminViewMessages(String peopleChatName){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        HashMap<String,Object>data=new HashMap<>();

        data.put("chatPeopleName", peopleChatName);

        firebaseFirestore.collection(AdminName+" chat with people name").document(peopleChatName).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(PeopleMessageActivity.this, "successfully stored", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleMessageActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });




    }

    public  void CheckAndStorePeopleNameForAdminViewMessages(){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(AdminName+" chat with people name").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot documentSnapshot:list){

                                String peopleName=documentSnapshot.getString("chatPeopleName");

                                if (userName.equals(peopleName)){

                                    ChatPeopleName=peopleName;
                                }
                            }

                            if (!ChatPeopleName.equals(userName)){

                                storePeopleChatNameForAdminViewMessages(userName);

                            }
//                            else{
//                                Toast.makeText(PeopleMessageActivity.this, "this name exists", Toast.LENGTH_SHORT).show();
//                            }

                        }else{

                            storePeopleChatNameForAdminViewMessages(userName);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleMessageActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void retrieveInDatabaseForHandler(){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");

        firebaseFirestore.collection(AdminName+" vao chat").document(AdminName+" and "+userName+" chat").collection(AdminName+" and "+userName+" chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                NumberForHandler =documentSnapshot.getString("chatNo");

                            }
                            checkCondition(NumberForHandler);


                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleMessageActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void checkCondition(String LastNo){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("peopleChatNo",MODE_PRIVATE);
        String sp= sharedPreferences.getString("DbChatNo","null");
        SharedPreferences.Editor editor=sharedPreferences.edit();

//        Toast.makeText(this, "sp No"+sp, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Db last No "+LastNo, Toast.LENGTH_SHORT).show();

        if (!sp.equals(LastNo)){

            checkInDatabaseForHandler(LastNo);

            editor.putString("DbChatNo",LastNo);
            editor.apply();
        }

    }

    public void checkInDatabaseForHandler(String No){

        SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        String userName=sharedPreferences.getString("PeopleUserName","null");
        String AdminName=sharedPreferences.getString("adminName","null");


        firebaseFirestore.collection(AdminName+" vao chat").document(AdminName+" and "+userName+" chat").collection(AdminName+" and "+userName+" chat").whereEqualTo("chatNo",No).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

//                            Toast.makeText(PeopleMessageActivity.this, "checkInDatabaseForHandler if works", Toast.LENGTH_SHORT).show();

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String peopleChat=documentSnapshot.getString("peopleChat");
                                String adminChat=documentSnapshot.getString("adminChat");


                                ListViewAdapter(peopleChat,adminChat);

                            }

                            chatNo="null";

                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleMessageActivity.this, "error; "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }



    public  void handler(){

        retrieveInDatabaseForHandler();

        runnable=new Runnable() {
            @Override
            public void run() {

                retrieveInDatabaseForHandler();

                handler.postDelayed(this,1000);
            }
        };
        handler.post(runnable);

    }



}





