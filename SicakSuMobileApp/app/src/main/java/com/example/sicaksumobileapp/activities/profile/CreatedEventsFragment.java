package com.example.sicaksumobileapp.activities.profile;

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
import com.example.sicaksumobileapp.activities.profile.CreatedEventAdapter;
import com.example.sicaksumobileapp.models.SicakSuEvent;
import com.example.sicaksumobileapp.models.SicakSuProfile;
import com.example.sicaksumobileapp.repository.CreatedEventRepo;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatedEventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CreatedEventAdapter adp;
    private String profileId;

    public CreatedEventsFragment() {
        // Required empty public constructor
    }

    public static CreatedEventsFragment newInstance(String profileId) {
        CreatedEventsFragment fragment = new CreatedEventsFragment();
        Bundle args = new Bundle();
        args.putString("profileId", profileId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profileId = getArguments().getString("profileId");
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.e("Adapter", "UNSET");
            List<SicakSuEvent> data = (List<SicakSuEvent>) msg.obj;
            adp = new CreatedEventAdapter(requireContext(), data);
            recyclerView.setAdapter(adp);
            Log.e("Adapter", "SET");

            return true;
        }
    });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_created_events, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewCreated);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set an empty adapter
        adp = new CreatedEventAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adp);

        // Retrieve the created events
        CreatedEventRepo repo = new CreatedEventRepo();
        SicakSuApp app = ((SicakSuApp) requireActivity().getApplication());
        SicakSuProfile yourProfile = app.getUserProfile();
        Log.e("Event", "CREATED");
        repo.retrieveCreatedEvents(app.srv, handler,yourProfile.getId());
        Log.e("Event", "RETRIEVED");
        return rootView;
    }
}
