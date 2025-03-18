package com.example.villageadministrationcomplaint;

import androidx.appcompat.app.AppCompatActivity;

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


    public class AdminMainMenuActivity extends AppCompatActivity {
        Toolbar toolbar;
        DrawerLayout drawerLayout;
        NavigationView navigationView;
        ListView listView;

        TextView adminName, VillageName;
        ImageView adminMenu;

        FirebaseFirestore firebaseFirestore;
        FirebaseAuth firebaseAuth;

        ArrayList<ListViewData> arrayList = new ArrayList();
        ListViewAdapter adapter;

        ArrayList<ArrayData> StoreData = new ArrayList<>();
        ArrayList<FeedBackArrayData> feedBackList = new ArrayList<>();

        int lastComplaintNumber = 0;

        int number = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.adminmainmenu);

            toolbar = findViewById(R.id.adminMainToolbar);
            drawerLayout = findViewById(R.id.adminMainDrawerLayout);
            navigationView = findViewById(R.id.adminNavigationView);
            adminMenu = findViewById(R.id.adminMenu);
            listView = findViewById(R.id.adminListView);

            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();

            retrieveAndInsertInListView();

            settingAdminNameAndVillageNameInHeader();
            NavigationDrawerAndListener();

            adminMenu.setOnClickListener(new View.OnClickListener() {
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

//                    Toast.makeText(AdminMainMenuActivity.this, "complaint title is " + complaint, Toast.LENGTH_SHORT).show();

                        String clickedTitle = StoreData.get(position).getStoreNo();

//                    Toast.makeText(AdminMainMenuActivity.this, "position number is " + position, Toast.LENGTH_SHORT).show();
//
//                    Toast.makeText(AdminMainMenuActivity.this, "clicked title database no is " + clickedTitle, Toast.LENGTH_SHORT).show();

                       Intent intent = new Intent(AdminMainMenuActivity.this, AdminComplaintStatusActivity.class);
                        intent.putExtra("clickedTitle", clickedTitle);
                        startActivity(intent);
                        finish();

                    } else {

                        gettingFeedbackNoFroIntent(position);
                    }

                }
            });

        }

//        @Override
//        public void onBackPressed() {
//
//            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//
//                drawerLayout.closeDrawer(GravityCompat.START);
//            }
//            if (number == 0) {
//                number++;
//            } else {
//
//                super.onBackPressed();
//            }
//
//
//        }

        public void NavigationDrawerAndListener() {

            navigationView.bringToFront();
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(AdminMainMenuActivity.this, drawerLayout, R.string.navigation_open, R.string.navigation_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

                    if (item.getItemId()==R.id.adminComplaint) {

                            Toast.makeText(AdminMainMenuActivity.this, "complaint is clicked", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AdminMainMenuActivity.this, AdminViewComplaintActivity.class);
                            startActivity(intent);
                            finish();

                    }else if (item.getItemId()==R.id.adminMessage) {


                        Toast.makeText(AdminMainMenuActivity.this, "message is clicked", Toast.LENGTH_SHORT).show();

                          Intent messageIntent = new Intent(AdminMainMenuActivity.this, AdminViewMessageActivity.class);
                           startActivity(messageIntent);
                            finish();

                    }else if(item.getItemId()==R.id.adminFeedback) {


                        Toast.makeText(AdminMainMenuActivity.this, "feedback is clicked", Toast.LENGTH_SHORT).show();

                           Intent FeedbackIntent = new Intent(AdminMainMenuActivity.this, AdminViewFeedBackActivity.class);
                            startActivity(FeedbackIntent);
                            finish();

                    }else if(item.getItemId()==R.id.adminLogout) {


                        LogOut();

                        Toast.makeText(AdminMainMenuActivity.this, "logout is clicked", Toast.LENGTH_SHORT).show();

                    }

                    return true;
                }
            });
        }

        public void settingAdminNameAndVillageNameInHeader() {

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
            String name = sharedPreferences.getString("adminName", "null");
            String villageName = sharedPreferences.getString("villageName", "null");

            View view = navigationView.getHeaderView(0);

            adminName = view.findViewById(R.id.adminHeaderName);
            VillageName = view.findViewById(R.id.adminHeaderVillageName);

            adminName.setText(name);
            VillageName.setText(villageName);
        }

        public void ListViewAdapter(String title, String complaint, String status) {

            arrayList.add(new ListViewData(title, complaint, status));

            adapter = new ListViewAdapter(AdminMainMenuActivity.this, R.layout.listviewdata, arrayList);
            listView.setAdapter(adapter);


        }

        public void retrieveAndInsertInListView() {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
            String adminName = sharedPreferences.getString("adminName", "null");


            firebaseFirestore.collection(adminName + " VAO").orderBy("complaintNo").get()
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

                                    lastComplaintNumber = Integer.parseInt(complaintNo);

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
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);
            String adminName = sharedPreferences.getString("adminName", "null");

//        Toast.makeText(this, "admin name "+adminName, Toast.LENGTH_SHORT).show();

            firebaseFirestore.collection(adminName + " VAO" + " feedback").orderBy("feedbackNo").get()
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

            int number = 0;

            lastComplaintNumber = lastComplaintNumber - 1;

//        Toast.makeText(this, " decreased last complaint is "+lastComplaintNumber, Toast.LENGTH_SHORT).show();
//
//        Toast.makeText(this, "clicked position "+position, Toast.LENGTH_SHORT).show();

            for (int i = lastComplaintNumber; i < position; i++) {

                number = number + 1;


            }

            String FeedbackNo = Integer.toString(number);

            Intent intent = new Intent(AdminMainMenuActivity.this, AdminFeedBackStatusActivity.class);
            intent.putExtra("clickedFeedbackTitle", FeedbackNo);
            startActivity(intent);
            finish();

//        Toast.makeText(this, "final value of number is "+number, Toast.LENGTH_SHORT).show();


        }

        public void LogOut() {

            firebaseAuth.signOut();

            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("adminName", MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.clear();
            editor.apply();


            Intent intent = new Intent(AdminMainMenuActivity.this, Home.class);
            startActivity(intent);
            finish();


        }

    }

