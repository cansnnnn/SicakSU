package com.example.sicaksumobileapp.models;

public class AuthenticationPayload {
    String status;
    SicakSuProfile profile;
    public AuthenticationPayload() {
    }

    public AuthenticationPayload(String status, SicakSuProfile profile) {
        this.status = status;
        this.profile = profile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SicakSuProfile getProfile() {
        return profile;
    }

    public void setProfile(SicakSuProfile profile) {
        this.profile = profile;
    }
}
