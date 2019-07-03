package notification.com.pushnotification;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    List<UserDetailModal> userDetailModalList;
    UserDetailAdapter userDetailAdapter;
    Context context;
    private static final String TAG = "AllUsersFragment";
    public AllUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
        Log.i(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: ");
        View view= inflater.inflate(R.layout.fragment_all_users, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = this.getActivity();
        userDetailModalList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyler_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userDetailAdapter = new UserDetailAdapter(userDetailModalList);
        recyclerView.setAdapter(userDetailAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        userDetailModalList.clear();
        firebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
               for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges())
               {
                   if(doc.getType()==DocumentChange.Type.ADDED)
                   {
                       String user_id = doc.getDocument().getId();
                       UserDetailModal userDetailModal = doc.getDocument().toObject(UserDetailModal.class).WithId(user_id);
                       userDetailModalList.add(userDetailModal);
                       userDetailAdapter.notifyDataSetChanged();
                       Log.i(TAG, "onEvent: ");
                   }
               }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }
}