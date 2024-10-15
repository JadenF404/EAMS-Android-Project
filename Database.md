## Database

Will use firebase

<s>Database url can be set up in Spring's `application.properties` file. The current url is set to a localhosted mySQL server using:
`spring.datasource.url=jdbc:mysql://localhost:3306/user_management`
The database should be a mysql server and is accessed using MyBatis XML mappers [here](backend/src/main/resources/mapper).</s>



@MILLER: 
Add your code for the Password Reset;  {It already thows all User facing errors}
`AttendeePasswordChange.class` Line: 57
`OrganizerPasswordChange.class` Line: 60

For Logins: {it already speaks to my UI; you just need to connect that to the DB} 
`AttendeeLoginActivity.class` Line :  46
`OrganizerLoginActivity.class` Line : 45
