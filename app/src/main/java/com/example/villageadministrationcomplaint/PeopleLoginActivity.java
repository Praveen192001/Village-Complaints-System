package com.example.villageadministrationcomplaint;

import android.content.Intent;
import android.content.SharedPreferences;
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

import org.jetbrains.annotations.NotNull;

public class PeopleLoginActivity extends AppCompatActivity {

    EditText email,password;
    TextView loginToRegister;
    Button login;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peoplelogin);

        email=findViewById(R.id.emailInLogin);
        password=findViewById(R.id.passwordInLogin);
        loginToRegister=findViewById(R.id.loginToRegister);
        login=findViewById(R.id.loginButton);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String EmailLogin=email.getText().toString().trim();
                String PasswordLogin=password.getText().toString().trim();

                if (email(EmailLogin) && password(PasswordLogin)){

                    loginUsingFirebaseAuth(EmailLogin,PasswordLogin);

                }else{
                    Toast.makeText(PeopleLoginActivity.this, " Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PeopleLoginActivity.this, PeopleRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

    public boolean email(String Email){

        if (Email.length()!=0){

            return true;
        }else{
            Toast.makeText(this, "Email Field Is Empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public  boolean password(String Password){

        if (Password.length() !=0){

            if (Password.length()==6 || Password.length()>6){

                return  true;

            }else {
                Toast.makeText(this, "Password Must Be AtLeast 6 Character", Toast.LENGTH_SHORT).show();
                return false;
            }

        }else {
            Toast.makeText(this, "Password Field Empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public  void loginUsingFirebaseAuth(String email,String password){

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(PeopleLoginActivity.this, "successfully logged", Toast.LENGTH_SHORT).show();

                        String id=firebaseAuth.getUid();

                        retrieveEmailAndNameFromFireStore(id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(PeopleLoginActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public  void retrieveEmailAndNameFromFireStore(String id){

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("peopleName",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        firebaseFirestore.collection("userDetails").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String email=documentSnapshot.getString("email");
                        String userName=documentSnapshot.getString("userName");


                        editor.putString("PeopleUserName",userName);
                        editor.putString("peopleEmail",email);
                        editor.apply();


                        Intent intent=new Intent(PeopleLoginActivity.this,PeoplePinActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(PeopleLoginActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                    }
                });


    }
}




