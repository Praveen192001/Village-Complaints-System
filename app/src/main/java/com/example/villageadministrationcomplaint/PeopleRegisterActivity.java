package com.example.villageadministrationcomplaint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class PeopleRegisterActivity extends AppCompatActivity {

    EditText userName,email,password;
    TextView registerToLogin;
    Button registerButton;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;


    String existingName="null";
    String existingEmail="null";

    String ExistingUserName="null";
    String ExistingEmail="null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peopleregister);

        userName=findViewById(R.id.userNameInRegister);
        email=findViewById(R.id.emailInRegister);
        password=findViewById(R.id.passwordInRegister);
        registerButton=findViewById(R.id.registerButton);
        registerToLogin=findViewById(R.id.registerToLogin);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserName=userName.getText().toString().trim();
                String RegisterEmail=email.getText().toString().trim();
                String RegisterPassword=password.getText().toString().trim();

                if (userName(UserName) && email(RegisterEmail) && password(RegisterPassword)){

                    gettingUserDetailsFromFirestoreAndExistingNameAndEmailCheck(UserName,RegisterEmail,RegisterPassword);

                }else{

                    Toast.makeText(PeopleRegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();

                }

            }
        });


        registerToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PeopleRegisterActivity.this, PeopleLoginActivity.class);
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
            return  false;
        }

    }

    public boolean email(String Email){

        if (Email.length() !=0){

            return true;
        }else{
            Toast.makeText(this, "Email Filed Is Empty!", Toast.LENGTH_SHORT).show();
            return  false;

        }

    }

    public  boolean password(String Password){

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

    public void StoreUserDetailsInFirestore(String FName,String FEmail,String FPassword){

        String documentPathName =firebaseAuth.getUid();

        HashMap<String,Object>data=new HashMap<>();

        data.put("userName",FName);
        data.put("email",FEmail);
        data.put("password",FPassword);

        firebaseFirestore.collection("userDetails").document(documentPathName).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(PeopleRegisterActivity.this, "successfully stored in fireStore", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(PeopleRegisterActivity.this, PeopleLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                        Toast.makeText(PeopleRegisterActivity.this, "error:"+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public  void FirebaseAuth(String AuthUserName,String AuthEmail,String AuthPassword){

        firebaseAuth.createUserWithEmailAndPassword(AuthEmail,AuthPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(PeopleRegisterActivity.this, "Successfully Created Account", Toast.LENGTH_SHORT).show();

                        StoreUserDetailsInFirestore(AuthUserName,AuthEmail,AuthPassword);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(PeopleRegisterActivity.this, "error:"+e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void gettingUserDetailsFromFirestoreAndExistingNameAndEmailCheck(String exUserName, String exEmail, String Password){

        firebaseFirestore.collection("userDetails").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot: list){

                                String documentUserName =documentSnapshot.getString("userName");
                                String  documentEmail=documentSnapshot.getString("email");

                                if (exUserName.equals(documentUserName) ){

                                    existingName=documentUserName;

                                    Toast.makeText(PeopleRegisterActivity.this, "This Name Already Exists", Toast.LENGTH_SHORT).show();

                                }

                                if (exEmail.equals(documentEmail)){

                                    existingEmail=documentEmail;

                                    Toast.makeText(PeopleRegisterActivity.this, "This Email Is Already Exists", Toast.LENGTH_SHORT).show();

                                }

                            }

                            existingNameAndEmailCheck(existingName,existingEmail,Password);

                        }else{
                            Toast.makeText(PeopleRegisterActivity.this, "people collection is empty", Toast.LENGTH_SHORT).show();
                            existingNameAndEmailCheck(existingName,existingEmail,Password);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(PeopleRegisterActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void existingNameAndEmailCheck(String exName,String exEmail,String Password){

        String Name=userName.getText().toString().trim();
        String Email=email.getText().toString().trim();

        if (!Name.equals(exName) && !Email.equals(exEmail)){

            existingNameCheckInAdminDetails(Name,Email,Password);


        }

    }

    public void existingNameCheckInAdminDetails(String name,String Email,String password){

        String EditTextName =userName.getText().toString().trim();
        String EditTextEmail =email.getText().toString().trim();

        firebaseFirestore.collection("AdminDetails").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot:list){

                                String existingName=documentSnapshot.getString("userName");
                                String existingEmail=documentSnapshot.getString("email");

                                if (name.equals(existingName)){

                                    ExistingUserName=existingName;

                                    Toast.makeText(PeopleRegisterActivity.this, "This Name Already Exists", Toast.LENGTH_SHORT).show();
                                }

                                if (Email.equals(existingEmail)){

                                    ExistingEmail=existingEmail;

                                    Toast.makeText(PeopleRegisterActivity.this, "This Email Already Exists", Toast.LENGTH_SHORT).show();
                                }
                            }

                            if (!EditTextName.equals(ExistingUserName) && !EditTextEmail.equals(ExistingEmail)){

                                FirebaseAuth(EditTextName, EditTextEmail,password);

                            }

                        }else {
                            Toast.makeText(PeopleRegisterActivity.this, "admin collection is empty", Toast.LENGTH_SHORT).show();

                            FirebaseAuth(EditTextName,EditTextEmail,password);
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleRegisterActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });

    }

}



