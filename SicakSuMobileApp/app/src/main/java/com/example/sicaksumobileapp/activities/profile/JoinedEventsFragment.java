package com.example.sicaksumobileapp.activities.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sicaksumobileapp.R;
import com.example.sicaksumobileapp.SicakSuApp;
import com.example.sicaksumobileapp.activities.FeedListAdapter;
import com.example.sicaksumobileapp.models.SicakSuEvent;
import com.example.sicaksumobileapp.repository.EventRepo;
import com.example.sicaksumobileapp.repository.JoinedEventRepo;

import java.util.ArrayList;
import java.util.List;

public class JoinedEventsFragment extends Fragment {

    private RecyclerView recyclerView;

    private JoinedEventAdapter adp;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            List<SicakSuEvent> data = (List<SicakSuEvent>) msg.obj;
            adp = new JoinedEventAdapter(requireContext(),data);
            recyclerView.setAdapter(adp);

            return true;
        }
    });
    @Override
    public void onResume() {
        super.onResume();
        adp.fetchUpdatedDataFromBackend();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_joined_events, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewJoined);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adp = new JoinedEventAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adp);

        SicakSuApp app = (SicakSuApp)((Activity)requireContext()).getApplication();
        // Retrieve the created events
        JoinedEventRepo repo = new JoinedEventRepo(app.getUserProfile().getId());
        repo.retrieveJoinedEvents(new JoinedEventRepo.JoinedEventCallback() {
            @Override
            public void onSuccess(List<SicakSuEvent> data) {
                adp.setData(data);
                adp.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error if needed
                Log.e("JoinedEventsFragment", "Error retrieving joined events: " + errorMessage);
            }
        });
        return rootView;
    }
}
