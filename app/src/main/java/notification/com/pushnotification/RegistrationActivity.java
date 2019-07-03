package notification.com.pushnotification;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    EditText userPassword, userEmail, userName;
    Button login, createAccount;
    CircleImageView imageView;
    int PICK_IMG = 1;
    StorageReference mStorageRef;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    Uri imageUri;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("images");
        firebaseFirestore = FirebaseFirestore.getInstance();
        userName = findViewById(R.id.et_name);
        userEmail = findViewById(R.id.et_email);
        userPassword = findViewById(R.id.et_password);
        createAccount = findViewById(R.id.btn_registration);
        login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.img_user);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String user_id = mAuth.getCurrentUser().getUid();
                                final StorageReference user_profile = mStorageRef.child(user_id + ".jpg");

                                user_profile.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        user_profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(final Uri uri) {
                                                final String downloadedUrl = uri.toString();//actual this is uri
                                                        String token_id = FirebaseInstanceId.getInstance().getToken();
                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("name", name);
                                                        map.put("email", email);
                                                        map.put("password", password);
                                                        map.put("token_id", token_id);
                                                        map.put("image_url", downloadedUrl);
                                                        Log.i(TAG, "onSuccess: uri= " + uri.toString());
                                                        firebaseFirestore.collection("users").document(user_id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                sendToMainActivity();
                                                            }
                                                        });
                                                        Toast.makeText(RegistrationActivity.this, "Successfully Registered", Toast.LENGTH_SHORT).show();

                                                    }
                                        });
                                    }
                                });

                            } else {
                                Toast.makeText(RegistrationActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMG);
            }
        });
    }

    private void sendToMainActivity() {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                imageUri = data.getData();
                setImageFromGallery(imageUri);
            }
        }
    }

    private void setImageFromGallery(Uri imgUri) {
        imageView.setImageURI(imgUri);
    }
}