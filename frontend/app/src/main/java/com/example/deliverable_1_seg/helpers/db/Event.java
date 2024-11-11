package com.example.deliverable_1_seg.helpers.db;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String eventId;
    private String title;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    private String organizerId;
    private String address;

    public void setRequests(List<String> requests) {
        this.requests = requests;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    private List<String> people = new ArrayList<>();
    private List<String> requests = new ArrayList<>();


    public List<String> getPeople() {
        return people;
    }

    public void addPeople(String userID) {
        people.add(userID);
    }

    public boolean removePeople(String userID){
        return people.remove(userID);
    }

    public List<String> getRequests() {
        return requests;
    }

    public void addRequests(String userID) {
        requests.add(userID);
    }

    public boolean removeRequests(String userID){
        return requests.remove(userID);
    }



    private boolean automaticApproval;

    // Empty constructor for Firebase
    public Event() {}

    // Full constructor
    public Event(String eventId, String title, String date, String startTime, String endTime, String description, String organizerId, String address, boolean automaticApproval) {
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.organizerId = organizerId;
        this.address = address;
        this.automaticApproval = automaticApproval;

    }

    // Getters and setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAutomaticApproval() {
        return automaticApproval;
    }

    public void setAutomaticApproval(boolean automaticApproval) {
        this.automaticApproval = automaticApproval;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

