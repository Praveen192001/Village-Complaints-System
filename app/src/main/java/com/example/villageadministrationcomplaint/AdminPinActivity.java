package com.example.villageadministrationcomplaint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View; import android.widget.EditText;
import android.widget.ImageView; import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener; import com.google.android.gms.tasks.OnSuccessListener; import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot; import com.google.firebase.firestore.FirebaseFirestore; import com.google.firebase.firestore.QuerySnapshot;
import org.jetbrains.annotations.NotNull; import java.util.HashMap;
import java.util.List;
public class AdminPinActivity extends AppCompatActivity { EditText adminPin;
    ImageView adminPinButton;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String existingPin; @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminpin);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        adminPin =findViewById(R.id.adminPin);
        adminPinButton =findViewById(R.id.adminPinButton);
        adminPinButton.setOnClickListener(new View.OnClickListener()
        { @Override
        public void onClick(View v) {

            String AdminPin=adminPin.getText().toString();
            storingAndCheckExistingPinInFirestore(AdminPin);
        }
        });



    }
    public void storingAndCheckExistingPinInFirestore(String pin){

        firebaseFirestore.collection("AdminDetails").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
                { @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots)
                {
                    if (!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot:list){
                        String DocumentPin =documentSnapshot.getString("pin");
                        if (pin.equals(DocumentPin)){
                            existingPin= DocumentPin;
                            Toast.makeText(AdminPinActivity.this, "this pin already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    checkExistingPin(existingPin);

                }
                    else
                    {
                    Toast.makeText(AdminPinActivity.this, "collection is empty", Toast.LENGTH_SHORT).show();
                    creatingEmailAndPasswordFirebaseAuth(pin);

                }
                }
                })
                .addOnFailureListener(new OnFailureListener() { @Override
                public void onFailure(@NonNull @NotNull Exception e)
                {
                    Toast.makeText(AdminPinActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                }
                });

    }
    public void checkExistingPin(String exPin){ String pin=adminPin.getText().toString();
        if (!pin.equals(exPin)){
        creatingEmailAndPasswordFirebaseAuth(pin);
    }
    }
    public void StoringPinAndAdminDetails(String pin,String id){
        String name=getIntent().getStringExtra("name");
        String email=getIntent().getStringExtra("email");
        String password=getIntent().getStringExtra("password");
        String villageName=getIntent().getStringExtra("villageName");
        HashMap<String,Object>data=new HashMap<>();
        data.put("userName",name);
        data.put("email",email);
        data.put("password",password);
        data.put("pin",pin);
        data.put("villageName",villageName);

        firebaseFirestore.collection("AdminDetails").document(id).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() { @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(AdminPinActivity.this, "successfully stored pin", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(AdminPinActivity.this,AdminLoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                })
                .addOnFailureListener(new OnFailureListener() { @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AdminPinActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                }
                });



    }
    public void creatingEmailAndPasswordFirebaseAuth(String pin){ String email=getIntent().getStringExtra("email");
        String password=getIntent().getStringExtra("password");
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() { @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(AdminPinActivity.this, "successfully created", Toast.LENGTH_SHORT).show();

                    String id=firebaseAuth.getUid();
                    StoringPinAndAdminDetails(pin,id);
                }
                })
                .addOnFailureListener(new OnFailureListener() { @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(AdminPinActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                }
                });
    }
}



