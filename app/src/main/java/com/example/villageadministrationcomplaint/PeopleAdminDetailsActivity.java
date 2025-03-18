package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PeopleAdminDetailsActivity extends AppCompatActivity {
    TextView adminName,adminPin,adminVillage;

    SharedPreferences sharedPreferences;


    String _adminName;
    String  _adminPin;
    String _villageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people_admin_details);

        adminName=findViewById(R.id.peopleAdminDetails_name);
        adminPin=findViewById(R.id.peopleAdminDetails_pin);
        adminVillage=findViewById(R.id.peopleAdminDetails_village);

        TextView linkTextView = findViewById(R.id.link);

        // method to redirect to provided link
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        // method to change color of link

        linkTextView.setLinkTextColor(Color.YELLOW);

        sharedPreferences =getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
       _adminName=sharedPreferences.getString("adminName","null");
       _adminPin=sharedPreferences.getString("adminPin","null");
       _villageName=sharedPreferences.getString("villageName","null");

       adminName.setText(_adminName);
       adminPin.setText(_adminPin);
       adminVillage.setText(_villageName);


       getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
           @Override
           public void handleOnBackPressed() {

               Intent intent=new Intent(PeopleAdminDetailsActivity.this,PeopleMainMenuActivity.class);
               startActivity(intent);
               finish();
           }
       });


    }



}
