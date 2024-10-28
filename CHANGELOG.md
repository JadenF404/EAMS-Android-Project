


## [1.11] - 2024-10-27 (27th October 2024) 

@ Miller 
  - Remade Main login page UI & Attendee Login Page UI

@Aditya
  - deleted random unused files & folder, such as `databaselog.md`  and  ` backend`
  - Everything related to firebase is in the `FirebaseHelper` class
  - Fixed Issue with updated UI where app would crash 
  - Refractored the codebase of java classes. File structure as follows: 


└── deliverable_1_seg
    ├── FirebaseHelper.java
    ├── MainActivity.java
  
    ├── helpers
    │   ├── db
    │   │   ├── RegistrationRequest.java
    │   │   └── RequestsAdapter.java
    
    │   ├── password
    │   │   ├── AttendeePasswordChange.java
    │   │   └── OrganizerPasswordChange.java
    
    │   └── welcomepages
    │       ├── AdministratorWelcomePage.java
    │       ├── AttendeeWelcomePage.java
    │       └── OrganizerWelcomePage.java
  
    └── user_actions
        ├── AdminRequestsActivity.java
        ├── AdministratorLoginActivity.java
        ├── AttendeeLoginActivity.java
        └── OrganizerLoginActivity.java



## [1.10] - 2024-10-25 (25th October 2024) 

## Added 

@ Jaden 
- Firebase auth is set up and working. It saves each user with a unique ID and all the other user data is still stored in the real-time database
- I added a new attribute status that is set to 'pending' when creating an account. There is no way to change it as of yet
- When you login as admin, there is now a page called 'admin_requests_inbox' that lists all the pending requests using a RecyclerView that displays items of class 'RegistrationRequest' using the 'RequestAdapter' class


# Still to implement 
- The approve/reject buttons do not do anything yet
- Logic on the attendee/organizer login forms to allow you only to login when your status changes to accepted
- Message informing rejected requests that they need to contact administrator
- The UI could be better if you wish
  


---

## [1.03] - 2024-16-11 (16th October 2024) 

## Added 
- Welcome pages added for after a user logs in
- Error checking for certain inputs when signing up

# Fixed 
- Input type incorrect for certain fields in organizer


---


## [1.02] - 2024-10-11 (15th October 2024) 

## Added 
 - Login Page for both User & Organizer.
 - Added Password reset UI & related error checking (not same password)
 - Added Not An Attendee & Not An Organizer buttons (takes you back to the main page) 

# Fixed 
- Color Scheme of entire application.


---


## [1.01] - 2024-10-11 (11th October 2024) 

SCRAPPED (using firebase) 

### Added
  - spring db mapper (@miller)
  - add spring beans for each type of user
  - map data into mysql db using mybatis xml
  - implement service layers to route jdbc data to database
  - implement controllers for more secure connection
  - set up hikari pools ready to connect to database
  - set up administrator access to manage data

### To do
  - (@miller)
  - important: generate tokens first by verifying passwords matches email before returning attendee data. handle this on the backend
  - add user read/write permissions for each admin/organizer for more security
  - cors only allow connections from our frontend
  - need to add manageable event data types

---

## [1.00] - 2024-10-07 (7th October 2024) 

### Added
  - Added Attendee's login (@aditya)
  - Added Main Landing page

### Changed
  - `AttendeeLoginActivity.java`, to allow for button presses when you click on attendee in `activity_main.xml` (@aditya)
    
### Fixed


### Removed


### To be added
  - Firebase implementation (@mark)
  - UI refinement (@miller) 
