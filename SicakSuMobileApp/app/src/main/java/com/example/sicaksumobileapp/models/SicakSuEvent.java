package com.example.sicaksumobileapp.models;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class SicakSuEvent implements Serializable {
    String id;
    String content;
    String headline;
    int limit;
    int joinCount;
    List<SicakSuProfile> joinedPeople;
    LocalDateTime requestDate;
    SicakSuProfile createdBy;

    public SicakSuEvent(String id, String content, String headline, int limit, int joinCount, List<SicakSuProfile> joinedPeople, LocalDateTime requestDate, SicakSuProfile createdBy) {
        this.id = id;
        this.content = content;
        this.headline = headline;
        this.limit = limit;
        this.joinCount = joinCount;
        this.joinedPeople = joinedPeople;
        this.requestDate = requestDate;
        this.createdBy = createdBy;
    }

    public SicakSuProfile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SicakSuProfile createdBy) {
        this.createdBy = createdBy;
    }

    public SicakSuEvent() {
        this.id = "None";
        this.content = "None";
        this.headline = "None";
        this.limit = 0;
        this.joinCount = 0;
        this.joinedPeople = null;
        this.requestDate = LocalDateTime.from(Instant.EPOCH);
        this.createdBy = null;
    }

    public SicakSuEvent(String content, String headline, int limit, int joinCount, List<SicakSuProfile> joinedPeople, LocalDateTime requestDate) {
        this.content = content;
        this.headline = headline;
        this.limit = limit;
        this.joinCount = joinCount;
        this.joinedPeople = joinedPeople;
        this.requestDate = requestDate;
    }

    public SicakSuEvent(String id, String content, String headline, int limit, int joinCount, List<SicakSuProfile> joinedPeople, LocalDateTime requestDate) {
        this.id = id;
        this.content = content;
        this.headline = headline;
        this.limit = limit;
        this.joinCount = joinCount;
        this.joinedPeople = joinedPeople;
        this.requestDate = requestDate;
    }

    public SicakSuEvent(String id, String content, String headline, int limit, int joinCount, List<SicakSuProfile> joinedPeople) {
        this.id = id;
        this.content = content;
        this.headline = headline;
        this.limit = limit;
        this.joinCount = joinCount;
        this.joinedPeople = joinedPeople;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public List<SicakSuProfile> getJoinedPeople() {
        return joinedPeople;
    }

    public void setJoinedPeople(List<SicakSuProfile> joinedPeople) {
        this.joinedPeople = joinedPeople;
    }

    @Override
    public String toString() {
        return "SicakSuEvent{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", headline='" + headline + '\'' +
                ", limit=" + limit +
                ", joinCount=" + joinCount +
                ", joinedPeople=" + joinedPeople +
                ", requestDate=" + requestDate +
                '}';
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}
