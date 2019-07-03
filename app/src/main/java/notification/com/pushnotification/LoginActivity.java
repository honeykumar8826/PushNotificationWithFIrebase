package notification.com.pushnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText userPassword, userEmail;
    Button login, createAccount;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userEmail = findViewById(R.id.et_email);
        userPassword = findViewById(R.id.et_password);
        login = findViewById(R.id.login);
        createAccount = findViewById(R.id.registration);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                                String tokenId = FirebaseInstanceId.getInstance().getToken();
                                String currentId = mAuth.getCurrentUser().getUid();
                                   Map<String,Object> mapValue = new HashMap<>();
                                   mapValue.put("tokenId",tokenId);
                                   mFirestore.collection("users").document(currentId).update(mapValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                           startActivity(intent);
                                           finish();
                                           Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                                       }
                                   });
                                                }
                       else
                       {
                           Toast.makeText(LoginActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
            }
        });
    }
}
