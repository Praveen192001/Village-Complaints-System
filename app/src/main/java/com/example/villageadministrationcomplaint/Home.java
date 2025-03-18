package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {

    TextView admin, people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        admin=findViewById(R.id.homeAdmin);
        people=findViewById(R.id.homePeople);

        checkForPeopleLogged();
        checkForAdminLogged();
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "admin clicked", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Home.this,AdminRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        people.setOnClickListener(new View.OnClickListener() { @Override
        public void onClick(View v) {

            Toast.makeText(Home.this, "people clicked", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(Home.this,PeopleRegisterActivity.class);
            startActivity(intent);
            finish();
        }
        });
    }

    public void checkForPeopleLogged(){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);

        String name= sharedPreferences.getString("PeopleUserName","null");
        if (!name.equals("null")){
          Intent intent=new Intent(Home.this,PeopleMainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void checkForAdminLogged(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        String name=sharedPreferences.getString("adminName","null");
        if (!name.equals("null")){
            Intent intent=new Intent(Home.this,AdminMainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
