package com.example.villageadministrationcomplaint;
import android.content.Intent;
import android.content.SharedPreferences; import android.os.Bundle;
import android.view.View; import android.widget.Button; import android.widget.EditText; import android.widget.TextView; import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener; import com.google.android.gms.tasks.OnSuccessListener; import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot; import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;
public class AdminLoginActivity extends AppCompatActivity { EditText adminEmail, adminPassword;
    TextView adminLoginToRegister; Button adminLogin;

    FirebaseFirestore firebaseFirestore; FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminlogin);

        adminEmail =findViewById(R.id.adminEmailInLogin);
        adminPassword =findViewById(R.id.adminPasswordInLogin);
        adminLoginToRegister =findViewById(R.id.adminLoginToRegister);
        adminLogin =findViewById(R.id.adminLoginButton);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        adminLogin.setOnClickListener(new View.OnClickListener() { @Override
        public void onClick(View v) {
            String EmailLogin= adminEmail.getText().toString().trim();
            String PasswordLogin= adminPassword.getText().toString().trim();

            if (email(EmailLogin) && password(PasswordLogin)){ loginUsingFirebaseAuth(EmailLogin,PasswordLogin);
            }
            else
            {
                Toast.makeText(AdminLoginActivity.this, " Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
        });

        adminLoginToRegister.setOnClickListener(new View.OnClickListener()
        { @Override
        public void onClick(View v) {

            Intent intent=new Intent(AdminLoginActivity.this, AdminRegisterActivity.class); startActivity(intent);
            finish();

        }
        });


    }

    public boolean email(String Email){ if (Email.length()!=0){
        return true;

    }
    else
    {
        Toast.makeText(this, "Email Field Is Empty!", Toast.LENGTH_SHORT).show(); return false;
    }

    }

    public boolean password(String Password){ if (Password.length() !=0)
    {
        if (Password.length()==6 || Password.length()>6){ return true;
        }
        else
        {
            Toast.makeText(this, "Password Must Be AtLeast 6 Character", Toast.LENGTH_SHORT).show();
            return false;

        }
    }
    else
    {
        Toast.makeText(this, "Password Field Empty!", Toast.LENGTH_SHORT).show(); return false;
    }
    }
    public void loginUsingFirebaseAuth(String email,String password){ firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() { @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(AdminLoginActivity.this, "successfully logged", Toast.LENGTH_SHORT).show();
                String id=firebaseAuth.getUid();

                retrieveEmailAndNameFromFireStore(id);
            }
            })
            .addOnFailureListener(new OnFailureListener() { @Override
            public void onFailure(@NonNull @NotNull Exception e) { Toast.makeText(AdminLoginActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
            }
            });


    }
    public void retrieveEmailAndNameFromFireStore(String id){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("adminName",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        firebaseFirestore.collection("AdminDetails").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() { @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String email=documentSnapshot.getString("email");
                    String userName=documentSnapshot.getString("userName"); String villageName=documentSnapshot.getString("villageName");
//	Toast.makeText(AdminLoginActivity.this, "current logged email is "+email, Toast.LENGTH_SHORT).show();
//	Toast.makeText(AdminLoginActivity.this, "current userName is "+userName, Toast.LENGTH_SHORT).show();

                    editor.putString("adminName",userName); editor.putString("adminEmail",email); editor.putString("villageName",villageName); editor.apply();
                    Intent intent=new Intent(AdminLoginActivity.this,AdminMainMenuActivity.class); startActivity(intent);
                    finish();
                }
                })
                .addOnFailureListener(new OnFailureListener() { @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AdminLoginActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                }
                });

    }
}



