## [1.01] - 2024-10-11 (11th October 2024) 

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
