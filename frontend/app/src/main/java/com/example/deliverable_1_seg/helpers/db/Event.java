package com.example.deliverable_1_seg.helpers.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Event {

    private String eventId;
    private String title;
    private String date;
    private String startTime;
    private String endTime;
    private String description;
    private String organizerId;
    private String address;
    private boolean automaticApproval;
    private Map<String, Boolean> people = new HashMap<>();
    private Map<String, Boolean> requests = new HashMap<>();

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

    // Full constructor with people and requests
    public Event(String eventId, String title, String date, String startTime, String endTime, String description, String organizerId, String address, boolean automaticApproval, Map<String, Boolean> people, Map<String, Boolean> requests) {
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.organizerId = organizerId;
        this.address = address;
        this.automaticApproval = automaticApproval;
        this.people = people;
        this.requests = requests;

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



    public Map<String, Boolean> getPeople() {
        return people;
    }

    public void setPeople(Map<String, Boolean> people) {
        this.people = people;
    }

    public void addPerson(String userId) {
        if (people == null) {
            people = new HashMap<>();
        }
        people.put(userId, true);
    }

    public boolean removePerson(String userId) {
        if (people != null) {
            return people.remove(userId) != null;
        }
        return false;
    }
    public Map<String, Boolean> getRequests() {
        return requests;
    }

    public void setRequests(Map<String, Boolean> requests) {
        this.requests = requests;
    }

    public void addRequest(String userId) {
        if (requests == null) {
            requests = new HashMap<>();
        }
        requests.put(userId, true);
    }

    public boolean removeRequest(String userId) {
        if (requests != null) {
            return requests.remove(userId) != null;
        }
        return false;
    }

    public long getLongDate() {
        // Define the format pattern for the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        try {
            String dateTimeString = date + " " + endTime;

            Date endDate = dateFormat.parse(dateTimeString);

            return endDate != null ? endDate.getTime() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Return 0 if parsing fails
        }
    }

}

