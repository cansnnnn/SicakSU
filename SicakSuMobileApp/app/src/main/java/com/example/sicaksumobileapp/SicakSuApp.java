package com.example.sicaksumobileapp;

import android.app.Application;

import com.example.sicaksumobileapp.models.SicakSuProfile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
// appin her yerinde ExecutorService kullanmak icin threadleri tek bir yerden yonetmek
// icin application classi icin de tanimlandi
public class SicakSuApp extends Application {
    private SicakSuProfile userProfile = new SicakSuProfile();

    public SicakSuProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(SicakSuProfile userProfile) {
        this.userProfile = userProfile;
    }
    public ExecutorService srv  = Executors.newCachedThreadPool();

}
