package com.example.sicaksumobileapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicaksumobileapp.R;
import com.example.sicaksumobileapp.SicakSuApp;
import com.example.sicaksumobileapp.activities.profile.ProfileActivity;
import com.example.sicaksumobileapp.models.SicakSuEvent;
import com.example.sicaksumobileapp.models.SicakSuProfile;
import com.example.sicaksumobileapp.repository.EventRepo;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedListViewHolder>{
    Context context;
    List<SicakSuEvent> data;

    public FeedListAdapter(Context context, List<SicakSuEvent> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public FeedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root=
                LayoutInflater.from(context).inflate(R.layout.activity_feed_row,parent,false);
        FeedListViewHolder holder = new FeedListViewHolder(root);

        /*
         * Images are recycled by the framework
         * we manage the images ourselves so we don't recycle them
         *  */
        holder.setIsRecyclable(false);

        return holder;
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FeedListViewHolder holder, int position) {

        SicakSuApp app = (SicakSuApp)((Activity)context).getApplication();
        SicakSuProfile yourProfile = app.getUserProfile();
        // Create a handler to handle the join event response
        Handler leaveHandler = new Handler(message -> {
            if ("leaved".equals(message.obj)) {
                // The join event was successful
                List<SicakSuProfile> newPro = new ArrayList<>();
                newPro.addAll(data.get(position).getJoinedPeople());
                newPro.remove(yourProfile);

                data.get(position).setJoinedPeople(newPro);
                data.get(position).setJoinCount(data.get(position).getJoinCount() - 1);
                String joinCount = String.valueOf(data.get(position).getJoinCount()) + "/" + String.valueOf(data.get(position).getLimit());
                holder.rowJoinCount.setText(joinCount);
                Snackbar.make(holder.itemView, "Leaved", Snackbar.LENGTH_SHORT).show();
                holder.joinButton.setText("Join");
            } else if ("notLeaved".equals(message.obj)) {
                // The join event failed
                Snackbar.make(holder.itemView, "Not leaved", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        });
        // Create a handler to handle the join event response
        Handler joinHandler = new Handler(message -> {
            if ("joined".equals(message.obj)) {
                // The join event was successful
                List<SicakSuProfile> newPro = new ArrayList<>();
                newPro.addAll(data.get(position).getJoinedPeople());
                newPro.add(yourProfile);

                data.get(position).setJoinedPeople(newPro);
                data.get(position).setJoinCount(data.get(position).getJoinCount() + 1);
                String joinCount = String.valueOf(data.get(position).getJoinCount()) + "/" + String.valueOf(data.get(position).getLimit());
                holder.rowJoinCount.setText(joinCount);
                Snackbar.make(holder.itemView, "Joined", Snackbar.LENGTH_SHORT).show();
                holder.joinButton.setText("Leave");
            } else if ("notJoined".equals(message.obj)) {
                // The join event failed
                Snackbar.make(holder.itemView, "Not Joined", Snackbar.LENGTH_SHORT).show();
            }
            return true;
        });
        // join button
        holder.joinButton.setOnClickListener(v->{
            EventRepo repo = new EventRepo();
            if(data.get(position).getJoinedPeople().contains(yourProfile)){
                repo.leaveEvent(app.srv, leaveHandler, data.get(position).getId(), yourProfile.getId());
                Log.e("LeaveButton","pressed");
            }else{
                repo.joinEvent(app.srv, joinHandler, data.get(position).getId(), yourProfile.getId());
                Log.e("JoinButton","pressed");
            }
        });
        // go to the profile detailed page
        holder.rowProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("pp","BASILDIM");

                int clickedPosition = holder.getAdapterPosition();

                // Handle the click on the profile picture
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("id",data.get(clickedPosition).getCreatedBy().getId());

                (context).startActivity(i);
                Snackbar.make(holder.itemView, "Profile Picutre pressed", Snackbar.LENGTH_SHORT).show();

            }
        });

        // go to the activity detailed page
        holder.row.setOnClickListener(v->{
            Log.e("row","BASILDIM");

            data.get(position).getJoinedPeople().forEach(x->{Log.e("index:"+String.valueOf(position), x.toString());});
            int clickedPosition = holder.getAdapterPosition();

            // Handle the click on the profile picture
            Intent i = new Intent(context,EventDetailActivity.class);
            i.putExtra("event",data.get(clickedPosition));

            (context).startActivity(i);
            Snackbar.make(holder.itemView, "Event pressed", Snackbar.LENGTH_SHORT).show();

            Log.e("Beyza","BASILDIM");
        });
        holder.downloadImage(app.srv,data.get(position).getCreatedBy().getImageUrl());
        holder.rowHeadline.setText(data.get(position).getHeadline());
        holder.rowContent.setText(data.get(position).getContent());
        String joinCount = String.valueOf(data.get(position).getJoinCount())+"/"+ String.valueOf(data.get(position).getLimit());
        holder.rowJoinCount.setText(joinCount);
        // Define the desired date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        // Format the LocalDateTime object as a string
        String formattedDateTime = data.get(position).getRequestDate().format(formatter);
        holder.rowRequestDate.setText(formattedDateTime);
        holder.name.setText(data.get(position).getCreatedBy().getName());

        //if it is your event do not show join button
        if(data.get(position).getCreatedBy().equals(yourProfile)){
            holder.joinButton.setVisibility(View.INVISIBLE);
        }else{
            if(data.get(position).getJoinedPeople().contains(yourProfile)){
                holder.joinButton.setText("Leave");
            }else{
                holder.joinButton.setText("Join");
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class FeedListViewHolder extends RecyclerView.ViewHolder{
        LinearLayoutCompat row;
        TextView rowHeadline;
        TextView rowContent;
        TextView rowRequestDate;
        TextView rowJoinCount;
        TextView name;
        ImageView rowProfilePicture;
        Button joinButton;
        boolean imageDownloaded = false;

        public FeedListViewHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.row_list_item);
            rowHeadline = itemView.findViewById(R.id.row_headline);
            rowContent = itemView.findViewById(R.id.row_content);
            rowRequestDate = itemView.findViewById(R.id.row_request_date);
            rowJoinCount = itemView.findViewById(R.id.row_join_count);
            name = itemView.findViewById(R.id.row_name);
            rowProfilePicture = itemView.findViewById(R.id.row_profile_picture);
            joinButton = itemView.findViewById(R.id.row_join_button);
        }

        Handler imageHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                rowProfilePicture.setImageBitmap((Bitmap) msg.obj);
                imageDownloaded = true;

                return true;
            }
        });

        public void downloadImage(ExecutorService srv, String path){

            if(imageDownloaded==false){
                EventRepo repo = new EventRepo();
                repo.downloadImage(srv,imageHandler,path);
            }

        }

    }
}
