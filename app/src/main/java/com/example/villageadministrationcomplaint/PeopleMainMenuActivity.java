package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



import androidx.appcompat.app.AppCompatActivity;

public class PeopleMainMenuActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    TextView peopleName, villageName;
    ImageView peopleMenu;

    ListView listView;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    ArrayList<ListViewData> arrayList = new ArrayList();
    ListViewAdapter adapter;

    ArrayList<ArrayData> StoreData = new ArrayList<>();
    ArrayList<FeedBackArrayData> feedBackList = new ArrayList<>();


    int lastComplaintNumber;

    int number=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplemainmenu);

        toolbar = findViewById(R.id.mainToolbar);
        drawerLayout = findViewById(R.id.mainDrawerLayout);
        navigationView = findViewById(R.id.mainNavigation);
        peopleMenu = findViewById(R.id.peopleMenu);
        listView = findViewById(R.id.peopleMainMenuListView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        settingAdminNameAndVillageNameInHeader();
        navigationDrawerAndItemListener();

        retrieveAndInsertInListView();


        peopleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String complaint = arrayList.get(position).getTitle();
                String type = arrayList.get(position).getType();

                if (type.equals("complaint")) {

                    Toast.makeText(PeopleMainMenuActivity.this, "complaint title is " + complaint, Toast.LENGTH_SHORT).show();

                    String clickedTitle = StoreData.get(position).getStoreNo();

                    Toast.makeText(PeopleMainMenuActivity.this, "position number is " + position, Toast.LENGTH_SHORT).show();

                    Toast.makeText(PeopleMainMenuActivity.this, "clicked title database no is " + clickedTitle, Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(PeopleMainMenuActivity.this,PeopleComplaintDetailsActivity.class);
                    intent.putExtra("clickedTitle",clickedTitle);
                    startActivity(intent);
                    finish();

                } else {
                    gettingFeedbackNoFroIntent(position);
                }

            }
        });

    }

    public void navigationDrawerAndItemListener() {

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(PeopleMainMenuActivity.this, drawerLayout, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                if (item.getItemId()==R.id.menu_message) {
                    Toast.makeText(PeopleMainMenuActivity.this, "message is clicked", Toast.LENGTH_SHORT).show();

                       Intent messageIntent=new Intent(PeopleMainMenuActivity.this,PeopleMessageActivity.class);
                        startActivity(messageIntent);
                        finish();

                } else if (item.getItemId()==R.id.menu_feedback) {
                        Toast.makeText(PeopleMainMenuActivity.this, "feedback is clicked", Toast.LENGTH_SHORT).show();

                        Intent intentForFeedBack = new Intent(PeopleMainMenuActivity.this, PeopleViewFeedBackActivity.class);
                        startActivity(intentForFeedBack);
                        finish();

                    } else if(item.getItemId()==R.id.menu_complaint) {
                        Toast.makeText(PeopleMainMenuActivity.this, "complaint is clicked", Toast.LENGTH_SHORT).show();

                        Intent intentForComplaint = new Intent(PeopleMainMenuActivity.this, PeopleViewComplaintActivity.class);
                        startActivity(intentForComplaint);
                        finish();

                    } else if (item.getItemId()== R.id.menu_logout) {

                        LogOut();

                        Toast.makeText(PeopleMainMenuActivity.this, "logout is clicked", Toast.LENGTH_SHORT).show();

                    } else if(item.getItemId()== R.id.admin_details) {
                        Toast.makeText(PeopleMainMenuActivity.this, "admin is clicked", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(PeopleMainMenuActivity.this, PeopleAdminDetailsActivity.class);
                        startActivity(intent);
                        finish();

                    }


                return true;
            }
        });

    }

    public void settingAdminNameAndVillageNameInHeader() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("peopleName", MODE_PRIVATE);
        String gettingName = sharedPreferences.getString("PeopleUserName", "null");
        String gettingVillageName = sharedPreferences.getString("villageName", "null");

//        Toast.makeText(this, "set name is: " + gettingName, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "set village Name is: "+gettingVillageName, Toast.LENGTH_SHORT).show();

        View view = navigationView.getHeaderView(0);

        peopleName = view.findViewById(R.id.headerName);
        villageName = view.findViewById(R.id.headerVillageName);

        peopleName.setText(gettingName);
        villageName.setText(gettingVillageName);


    }

    public void ListViewAdapter(String title, String complaint, String status) {

        arrayList.add(new ListViewData(title, complaint, status));

        adapter = new ListViewAdapter(PeopleMainMenuActivity.this, R.layout.listviewdata, arrayList);
        listView.setAdapter(adapter);


    }

    public void retrieveAndInsertInListView() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("peopleName", MODE_PRIVATE);
        String userName = sharedPreferences.getString("PeopleUserName", "null");

        firebaseFirestore.collection("people complaint").document(userName + " complaint").collection(userName + " complaint").orderBy("complaintNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String title = documentSnapshot.getString("title");
                                String type = documentSnapshot.getString("type");
                                String status = documentSnapshot.getString("status");
                                String complaintNo = documentSnapshot.getString("complaintNo");

                                lastComplaintNumber=Integer.parseInt(complaintNo);

                                StoreData.add(new ArrayData(title, type, status, complaintNo));


                            }

                            for (int i = 0; i < StoreData.size(); i = i + 1) {

                                String title = StoreData.get(i).getStoreTitle();

                                String type = StoreData.get(i).getStoreType();

                                String status = StoreData.get(i).getStoreStatus();

                                ListViewAdapter(title, type, status);

                            }

                        }

                        retrieveAndInsertFeedBackInListView();
                    }
                });


    }

    public void retrieveAndInsertFeedBackInListView() {

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("peopleName", MODE_PRIVATE);
        String userName = sharedPreferences.getString("PeopleUserName", "null");

        firebaseFirestore.collection("people feedback").document(userName + " feedback").collection(userName + " feedback").orderBy("feedbackNo").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()) {

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : list) {

                                String title = documentSnapshot.getString("title");
                                String type = documentSnapshot.getString("type");
                                String status = documentSnapshot.getString("status");
                                String feedbackNo = documentSnapshot.getString("feedbackNo");


                                feedBackList.add(new FeedBackArrayData(title, type, status, feedbackNo));


                            }

                            for (int i = 0; i < feedBackList.size(); i = i + 1) {

                                String title = feedBackList.get(i).getFeedbackTitle();

                                String type = feedBackList.get(i).getFeedbackType();

                                String status = feedBackList.get(i).getFeedbackStatus();

                                ListViewAdapter(title, type, status);

                            }

                        }
                    }
                });


    }

    public void gettingFeedbackNoFroIntent(int position) {

        int number=0;

        lastComplaintNumber=lastComplaintNumber-1;

//        Toast.makeText(this, " decreased last complaint is "+lastComplaintNumber, Toast.LENGTH_SHORT).show();
//
//        Toast.makeText(this, "clicked position "+position, Toast.LENGTH_SHORT).show();

        for(int i=lastComplaintNumber;i<position;i++){

            number=number+1;


        }

        String  FeedbackNo=Integer.toString(number);

        Intent intent=new Intent(PeopleMainMenuActivity.this,PeopleFeedBackDetailsActivity.class);
        intent.putExtra("clickedFeedbackTitle",FeedbackNo);
        startActivity(intent);
        finish();






    }

    public void LogOut(){

        firebaseAuth.signOut();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("peopleName", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.clear();
        editor.apply();

        Intent intent=new Intent(PeopleMainMenuActivity.this,Home.class);
        startActivity(intent);
        finish();

    }



}
