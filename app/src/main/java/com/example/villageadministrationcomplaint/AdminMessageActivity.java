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

public class AdminMessageActivity extends AppCompatActivity {

    EditText adminChatText;
    ImageView send;
    ListView listView;

    FirebaseFirestore firebaseFirestore;

    ArrayList<AdminChatMessageListViewData> arrayList = new ArrayList<>();
    AdminChatMessageListViewAdapter adminChatMessageListViewAdapter;

    String lastChatNumber;
    String NumberForHandler;

    String chatNo = "null";

    String SpLastNUmber = "null";

    Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);

        firebaseFirestore = FirebaseFirestore.getInstance();

        retrieveDataInDatabase();

        adminChatText = findViewById(R.id.MessageChatText);
        send = findViewById(R.id.MessageSend);
        listView = findViewById(R.id.MessageListView);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String adminChat = adminChatText.getText().toString();

                retrieveForLastChatNumberAndStoreInDatabase(adminChat);
            }
        });

        handler();


    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferencesForChat = getApplicationContext().getSharedPreferences("adminChatNo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesForChat.edit();

        handler.removeCallbacks(runnable);

        editor.clear();
        editor.apply();

        Intent intent = new Intent(AdminMessageActivity.this, AdminViewMessageActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }

    public void ListViewAdapter(String AdminChat, String peopleChat) {

        arrayList.add(new AdminChatMessageListViewData(AdminChat, peopleChat));

        adminChatMessageListViewAdapter = new AdminChatMessageListViewAdapter(AdminMessageActivity.this, R.layout.adminchatmessagelistviewdata, arrayList);
        listView.setAdapter(adminChatMessageListViewAdapter);


    }

    public void retrieveDataInDatabase() {
        SharedPreferences sharedPreferencesForChat = getApplicationContext().getSharedPreferences("adminChatNo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencesForChat.edit();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");

        String userName = getIntent().getStringExtra("peopleName");

//        Toast.makeText(this, "intent people name is "+ userName, Toast.LENGTH_SHORT).show();

        firebaseFirestore.collection(AdminName + " vao chat").document(AdminName + " and " + userName + " chat").collection(AdminName + " and " + userName + " chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String peopleChat = documentSnapshot.getString("peopleChat");
                                String adminChat = documentSnapshot.getString("adminChat");
                                SpLastNUmber = documentSnapshot.getString("chatNo");


                                ListViewAdapter(adminChat, peopleChat);

                            }
                            editor.putString("DbChatNo", SpLastNUmber);
                            editor.apply();

                        } else {

                            editor.putString("DbChatNo", SpLastNUmber);
                            editor.apply();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminMessageActivity.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void StorePeopleTextFromDatabase(String adminChat, String no) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");

        String userName = getIntent().getStringExtra("peopleName");

        int No = Integer.parseInt(no);
        No = No + 1;
        chatNo = Integer.toString(No);


        HashMap<String, Object> data = new HashMap<>();
        data.put("adminChat", adminChat);
        data.put("peopleChat", "");
        data.put("chatNo", chatNo);
        data.put("who's typed", "vao");


        firebaseFirestore.collection(AdminName + " vao chat").document(AdminName + " and " + userName + " chat").collection(AdminName + " and " + userName + " chat").document(AdminName + " chat " + chatNo).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AdminMessageActivity.this, "successfully Stored", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminMessageActivity.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void retrieveForLastChatNumberAndStoreInDatabase(String adminChat) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");

        String userName = getIntent().getStringExtra("peopleName");

        firebaseFirestore.collection(AdminName + " vao chat").document(AdminName + " and " + userName + " chat").collection(AdminName + " and " + userName + " chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                lastChatNumber = documentSnapshot.getString("chatNo");

                            }

//                            Toast.makeText(AdminMessageActivity.this, "last chat number is "+lastChatNumber, Toast.LENGTH_SHORT).show();

                            StorePeopleTextFromDatabase(adminChat, lastChatNumber);


                        } else {

                            String number = "0";
                            StorePeopleTextFromDatabase(adminChat, number);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminMessageActivity.this, "error: " + e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void retrieveInDatabaseForHandler() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");

        String userName = getIntent().getStringExtra("peopleName");


        firebaseFirestore.collection(AdminName + " vao chat").document(AdminName + " and " + userName + " chat").collection(AdminName + " and " + userName + " chat").orderBy("chatNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                NumberForHandler = documentSnapshot.getString("chatNo");

                            }
                            checkCondition(NumberForHandler);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminMessageActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void checkCondition(String LastNo) {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminChatNo", MODE_PRIVATE);
        String sp = sharedPreferences.getString("DbChatNo", "null");
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        Toast.makeText(this, "sp No"+sp, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "Db last No "+LastNo, Toast.LENGTH_SHORT).show();

        if (!sp.equals(LastNo)) {

            checkInDatabaseForHandler(LastNo);

            editor.putString("DbChatNo", LastNo);
            editor.apply();
        }


    }

    public void checkInDatabaseForHandler(String No) {


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
        String AdminName = sharedPreferences.getString("adminName", "null");

        String userName = getIntent().getStringExtra("peopleName");

        firebaseFirestore.collection(AdminName + " vao chat").document(AdminName + " and " + userName + " chat").collection(AdminName + " and " + userName + " chat").whereEqualTo("chatNo", No).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

//                            Toast.makeText(AdminMessageActivity.this, "checkInDatabaseForHandler if works", Toast.LENGTH_SHORT).show();

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String peopleChat = documentSnapshot.getString("peopleChat");
                                String adminChat = documentSnapshot.getString("adminChat");


                                ListViewAdapter(adminChat, peopleChat);

                            }

                            chatNo = "null";

                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(AdminMessageActivity.this, "error; " + e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void handler() {

        retrieveInDatabaseForHandler();

        runnable = new Runnable() {
            @Override
            public void run() {

                retrieveInDatabaseForHandler();

                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

    }

}

