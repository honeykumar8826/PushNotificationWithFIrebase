package notification.com.pushnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {
    private EditText userMessage;
    private Button sendNotification;
    private FirebaseFirestore mFirestore;
    private String currentId;
    private TextView toUserName;
    private StorageReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        databaseRef = FirebaseStorage.getInstance().getReference().child("/Notification");
        mFirestore = FirebaseFirestore.getInstance();
        currentId = FirebaseAuth.getInstance().getUid();
        final String toId = getIntent().getStringExtra("toId");
        final String toName = getIntent().getStringExtra("toName");
        toUserName = findViewById(R.id.tv_message);
           sendNotification = findViewById(R.id.btn_send);
           userMessage = findViewById(R.id.et_message);
           toUserName.setText("Send To"+toName);
           sendNotification.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   String message = userMessage.getText().toString();
                   if(!TextUtils.isEmpty(message))
                   {
                     Map<String,Object> notificationMessage = new HashMap<>();
                       notificationMessage.put("notificationMessage",message);
                       notificationMessage.put("notification_id",currentId);
                       // storing the notification inside the database

                     mFirestore.collection("users/"+toId+"/Notifications").add(notificationMessage)
                             .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                 @Override
                                 public void onSuccess(DocumentReference documentReference) {
                                     Toast.makeText(SendActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();
                                 }
                             });

                   }
               }
           });
//        id.setText(senderID);
    }
}
