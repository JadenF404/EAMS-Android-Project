## Database

We will use Firebase as our database.

~~The current configuration in Spring's `application.properties` file is set to a locally hosted MySQL server using the following URL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_management
```

The database should be a MySQL server, and it can be accessed using MyBatis XML mappers located [here](backend/src/main/resources/mapper).~~

---

@MILLER:  
Please add your code for the Password Reset. The system currently throws all user-facing errors.  
- **`AttendeePasswordChange.class`** Line: 57  
- **`OrganizerPasswordChange.class`** Line: 60  

For Logins:  
The login functionality is already connected to the UI; you just need to link it to the database.  
- **`AttendeeLoginActivity.class`** Line: 46  
- **`OrganizerLoginActivity.class`** Line: 45  
