package com.example.sicaksumobileapp.activities.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.List;

public class CreatedEventAdapter extends RecyclerView.Adapter<CreatedEventAdapter.CreatedViewHolder> {
    private List<SicakSuEvent> data;
    Context context;

    public CreatedEventAdapter(Context context, List<SicakSuEvent> events) {
        this.data = events;
        this.context = context;
    }

    public void setData(List<SicakSuEvent> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CreatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_created_events_row, parent, false);
        return new CreatedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CreatedViewHolder holder, int position) {
        SicakSuEvent event = data.get(position);
        holder.txtHeadline.setText(event.getHeadline());
        holder.txtContent.setText(event.getContent());
        Log.d("CreatedEventAdapter", "Event headline: " + event.getHeadline());
        Log.d("CreatedEventAdapter", "Event content: " + event.getContent());
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

    static class CreatedViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout row;
        TextView txtHeadline;
        TextView txtContent;
        // Declare other views in the item layout

        public CreatedViewHolder(View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.created_row_list_item);
            txtContent = itemView.findViewById(R.id.createdRowContent);
            txtHeadline = itemView.findViewById(R.id.createdRowHeadline);
        }
    }
}
