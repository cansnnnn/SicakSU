package com.example.sicaksumobileapp.activities.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicaksumobileapp.R;
import com.example.sicaksumobileapp.SicakSuApp;
import com.example.sicaksumobileapp.activities.EventDetailActivity;
import com.example.sicaksumobileapp.models.SicakSuEvent;
import com.example.sicaksumobileapp.models.SicakSuProfile;
import com.example.sicaksumobileapp.repository.JoinedEventRepo;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class JoinedEventAdapter extends RecyclerView.Adapter<JoinedEventAdapter.JoinedViewHolder> {
    private List<SicakSuEvent> data;
    Context context;
    JoinedEventRepo joinedEventRepo;

    public JoinedEventAdapter(Context context, List<SicakSuEvent> events) {
        this.data = events;
        this.context = context;
        SicakSuApp app = (SicakSuApp)((Activity)context).getApplication();
        joinedEventRepo = new JoinedEventRepo(app.getUserProfile().getId());
    }

    public void setData(List<SicakSuEvent> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public JoinedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_joined_events_row, parent, false);
        return new JoinedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedViewHolder holder, int position) {
        SicakSuEvent event = data.get(position);
        holder.txtHeadline.setText(event.getHeadline());
        holder.txtContent.setText(event.getContent());
        Log.d("JoinedEventAdapter", "Event headline: " + event.getHeadline());
        Log.d("JoinedEventAdapter", "Event content: " + event.getContent());
        SicakSuApp app = (SicakSuApp) ((Activity) context).getApplication();

        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the DetailActivity and pass necessary data
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("event", event);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class JoinedViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout row;
        TextView txtHeadline;
        TextView txtContent;
        // Declare other views in the item layout

        public JoinedViewHolder(View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.joined_row_list_item);
            txtContent = itemView.findViewById(R.id.joinedRowContent);
            txtHeadline = itemView.findViewById(R.id.joinedRowHeadline);
        }
    }

    public void fetchUpdatedDataFromBackend() {
        joinedEventRepo.retrieveJoinedEvents(new JoinedEventRepo.JoinedEventCallback() {
            @Override
            public void onSuccess(List<SicakSuEvent> updatedData) {
                setData(updatedData);
                notifyDataSetChanged();
            }
            @Override
            public void onError(String errorMessage) {
                // Handle error if needed
            }

        });
    }
}
