package notification.com.pushnotification;


import android.content.Context;
import android.content.Intent;
import android.media.midi.MidiDeviceService;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailAdapter  extends RecyclerView.Adapter<UserDetailAdapter.UserViewHolder>
{
    Context context;
    private final List<UserDetailModal> modalList;

    public UserDetailAdapter(List<UserDetailModal> modalList) {
        this.modalList = modalList;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_layout, parent, false);
        context = parent.getContext();
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final UserDetailModal studentInfoModalList = modalList.get(position);
        holder.userName.setText(studentInfoModalList.getName());
        final String senderId = studentInfoModalList.userId;
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,SendActivity.class);
                intent.putExtra("toId",senderId);
                intent.putExtra("toName",studentInfoModalList.getName());
                context.startActivity(intent);
            }
        });
       // holder.userProfilePic.setImageURI(Uri.parse(studentInfoModalList.getImageUrl()));

    }

    @Override
    public int getItemCount() {
        return modalList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        CircleImageView userProfilePic;
        RelativeLayout relativeLayout;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_profile_name);
            userProfilePic = itemView.findViewById(R.id.user_profile_img);
            relativeLayout = itemView.findViewById(R.id.relative_main);
        }
    }
}