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

import java.util.List;

public class PeoplePinActivity  extends AppCompatActivity {

    EditText pin;
    ImageView pinButton;

    FirebaseFirestore firebaseFirestore;

    String AdminPin;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplepin);

        pin=findViewById(R.id.peoplePin);
        pinButton=findViewById(R.id.peoplePinButton);

        firebaseFirestore=FirebaseFirestore.getInstance();

        pinButton .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Pin=pin.getText().toString();

                checkingPin(Pin);

            }
        });

    }

    public  void checkingPin(String editTextPin){

        firebaseFirestore.collection("AdminDetails").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                String pin= documentSnapshot.getString("pin");

                                if (pin.equals(editTextPin)){

                                    AdminPin=pin;

                                    id=documentSnapshot.getId();

//                                   Toast.makeText(PeoplePinActivity.this, "pin is "+AdminPin, Toast.LENGTH_SHORT).show();
                                }
                            }

                            checkPinAndShowToastMsg(id,AdminPin);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(PeoplePinActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public  void checkPinAndShowToastMsg(String id, String Pin){

        String checkPin=pin.getText().toString();

        if (checkPin.equals(Pin)){

            gettingAdminName(id,Pin);
        }else{
            Toast.makeText(this, "Please Enter Valid Pin", Toast.LENGTH_SHORT).show();
        }


    }

    public  void gettingAdminName(String id,String Pin){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();


        firebaseFirestore.collection("AdminDetails").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String name=documentSnapshot.getString("userName");
                        String villageName=documentSnapshot.getString("villageName");

//                        Toast.makeText(PeoplePinActivity.this, "Admin name is: "+name, Toast.LENGTH_SHORT).show();

                        editor.putString("adminName",name);
                        editor.putString("adminPin",Pin);
                        editor.putString("villageName",villageName);
                        editor.apply();

                       Intent intent=new Intent(PeoplePinActivity.this, PeopleMainMenuActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeoplePinActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

