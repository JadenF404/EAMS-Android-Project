# Event Attendance Management System (EAMS)

EAMS is a mobile application designed to handle event registration and attendance tracking for university events. Built for Android devices, it provides tailored features for **Attendees**, **Organizers**, and **Administrators**

---

## Project Overview

The Event Attendance Management System (EAMS) allows:
- **Attendees** to search for events, register, and manage their participation.
- **Organizers** to create and manage events, approve attendee registrations, and maintain event details.
- **Administrators** to oversee user registrations and ensure system integrity.

The app was developed using:
- **Java** for core application logic.
- **Android Studio** as the primary development environment.
- **Firebase Realtime Database** for cloud-based data storage.

---

## Features

### Attendee
- **Event Search**: Find events using keywords in titles or descriptions.
- **Registration Management**:
  - Request registration for events.
  - View statuses of event registrations (Approved, Rejected, Pending).
  - Cancel registrations under valid conditions.
- **Conflict Prevention**: Ensures no overlap in registered events.

### Organizer
- **Event Creation**:
  - Add events with validation for time, date, and other details.
  - Choose between manual or automatic approval of attendees.
- **Registration Management**:
  - Approve or reject individual attendee registrations.
  - Bulk approval options available.
- **Event Control**:
  - View past and upcoming events.
  - Restrict deletion of events with approved attendees.

### Administrator
- **User Registration**:
  - Approve or reject registration requests for both attendees and organizers.
  - Manage rejected requests with the option to reprocess.
- **System Oversight**:
  - Prevent unauthorized access through role-based permissions.

---

## Technical Implementation

- **Database Integration**:
  - Firebase Realtime Database handles user login, user data, events, and registration statuses.
- **Real-Time Updates**:
  - Immediate feedback and updates for registrations and event changes.
- **Notifications**:
  - Event reminders and registration status updates sent via Firebase.
- **Validation**:
  - Input validation ensures user-friendly error handling.
- **Scalability**:
  - Designed to support additional features and user roles in the future.

---

## How to Use

1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/EAMS.git
2. Open the project in Android Studio.
3. Add the Firebase configuration file (google-services.json).
4. Build and run the application on an emulator or Android device.
