package com.example.villageadministrationcomplaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminRegisterActivity extends AppCompatActivity {

    EditText adminUserName, adminEmail, adminPassword,adminVillage;
    TextView adminRegisterToLogin;
    Button adminRegisterButton;

    String existingName="null";
    String existingEmail="null";
    String ExistingUserName="null";
    String ExistingEmail="null";

    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminregister);

        adminUserName =findViewById(R.id.adminUserNameInRegister);
        adminEmail =findViewById(R.id.adminEmailInRegister);
        adminPassword =findViewById(R.id.adminPasswordInRegister);
        adminVillage=findViewById(R.id.adminVillageInRegister);
        adminRegisterButton =findViewById(R.id.adminRegisterButton);
        adminRegisterToLogin =findViewById(R.id.adminRegisterToLogin);

        firebaseFirestore=FirebaseFirestore.getInstance();

        adminRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            String UserName= adminUserName.getText().toString().trim();
            String RegisterEmail= adminEmail.getText().toString().trim();
            String RegisterPassword= adminPassword.getText().toString().trim();
            String VillageName=adminVillage.getText().toString().trim();
            if (userName(UserName) && email(RegisterEmail) && password(RegisterPassword) && villageName(VillageName)){

                gettingUserDetailsFromFirestoreAndExistingNameAndEmailCheck(UserName,RegisterEmail,RegisterPassword,VillageName);
            }else{
                Toast.makeText(AdminRegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
            }
        }
        });

        adminRegisterToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {

            Intent intent=new Intent(AdminRegisterActivity.this, AdminLoginActivity.class);
            startActivity(intent);
            finish();
        }
        });
    }

    public boolean userName(String Name){
        if (Name.length()!=0){
        return true;
    }else{

        Toast.makeText(this, "Name Filed Is Empty!", Toast.LENGTH_SHORT).show();
        return false;
    }

    }
    public boolean email(String Email){
        if (Email.length() !=0){
        return true;
    }else{
        Toast.makeText(this, "Email Filed Is Empty!", Toast.LENGTH_SHORT).show();
        return false;
    }
    }

    public boolean password(String Password){
        if (Password.length() !=0){
        if (Password.length()==6 || Password.length()>6){
            return true;
        }else {
            Toast.makeText(this, "Password Must Be AtLeast 6 Character", Toast.LENGTH_SHORT).show();
            return false;
        }
    }else {
        Toast.makeText(this, "Password Field Is Empty!", Toast.LENGTH_SHORT).show();
        return false;
    }
    }

    public boolean villageName(String villageName){
        if (villageName.length()!=0){
        return true;
    }else{
        Toast.makeText(this, "Village Filed Is Empty", Toast.LENGTH_SHORT).show();
        return false;
    }


    }

    public void PassingUserDetailsToAdminPin(String FName, String FEmail, String FPassword,String villageName){
        Intent intent=new Intent(AdminRegisterActivity.this, AdminPinActivity.class);
        intent.putExtra("name",FName);
        intent.putExtra("email",FEmail);
        intent.putExtra("password",FPassword);
        intent.putExtra("villageName",villageName);
        startActivity(intent);
        finish();

    }

    public void gettingUserDetailsFromFirestoreAndExistingNameAndEmailCheck(String exUserName, String exEmail, String Password,String villageName){
        firebaseFirestore.collection("AdminDetails").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot: list){
                        String documentUserName =documentSnapshot.getString("userName");
                        String documentEmail=documentSnapshot.getString("email");
                        if (exUserName.equals(documentUserName) ){
                            existingName=documentUserName;
                            Toast.makeText(AdminRegisterActivity.this, "This Name Already Exists",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (exEmail.equals(documentEmail)){
                            existingEmail=documentEmail;
                            Toast.makeText(AdminRegisterActivity.this, "This Email Is Already Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    existingNameAndEmailCheck(existingName,existingEmail,Password,villageName);

                }
                else
                {
                    Toast.makeText(AdminRegisterActivity.this, "Admin collection is empty", Toast.LENGTH_SHORT).show();
                    existingNameAndEmailCheckFromUserDetails(exUserName,exEmail,Password,villageName);
                }
                }
                })
                .addOnFailureListener(new OnFailureListener() { @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AdminRegisterActivity.this, "error: "+e,
                        Toast.LENGTH_SHORT).show();
                }
                });



    }
    public void existingNameAndEmailCheck(String exName,String exEmail,String Password,String villageName)
    {

        String Name= adminUserName.getText().toString().trim();
        String Email= adminEmail.getText().toString().trim();

        if (!Name.equals(exName) && !Email.equals(exEmail)){
//		Toast.makeText(this, "existingNameAndEmailCheck", Toast.LENGTH_SHORT).show();
		existingNameAndEmailCheckFromUserDetails(Name,Email,Password,villageName);
        }
    }

    public void existingNameAndEmailCheckFromUserDetails(String name,String email,String password,String villageName ){
//		Toast.makeText(this, "userDetails work", Toast.LENGTH_SHORT).show();
     	String Name=adminUserName.getText().toString().trim();
        String Email=adminEmail.getText().toString().trim();

        firebaseFirestore.collection("userDetails").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() { @Override

                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (!queryDocumentSnapshots.isEmpty()){

                    List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot documentSnapshot:list){
                        String existingName=documentSnapshot.getString("userName");
                        String existingEmail=documentSnapshot.getString("email");

                        if (name.equals(existingName)){
                            ExistingUserName=existingName;
                            Toast.makeText(AdminRegisterActivity.this, "This Name Already Exists", Toast.LENGTH_SHORT).show();
                        }

                        if (email.equals(existingEmail)){
                            ExistingEmail=existingEmail;
                            Toast.makeText(AdminRegisterActivity.this, "This Email Already Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!Name.equals(ExistingUserName) && !Email.equals(ExistingEmail)){
                        PassingUserDetailsToAdminPin(Name,Email,password,villageName);
                    }
                }
                    else
                    {
                    Toast.makeText(AdminRegisterActivity.this, "people collection is empty", Toast.LENGTH_SHORT).show();
                    PassingUserDetailsToAdminPin(Name,Email,password,villageName);
                }

                }
                })
                .addOnFailureListener(new OnFailureListener() { @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AdminRegisterActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                }

                });
    }



}