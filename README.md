# Employee Management System

This project is a Java + MySQL desktop app with a GUI and secure login.


## Structure

- `Main.java` — starts the app  
- `app/controller/` — handles logic (e.g., AdminController)  
- `app/dao/` — database queries (e.g., EmployeeDAO)  
- `app/db/` — DBConnection (reads from properties file)  
- `app/model/` — data classes like User, Employee  
- `app/view/` — GUI files (LoginView, Dashboards)  


## How to Run

1. Copy the DB config:
   ```bash
   cp dbconfig.properties.example dbconfig.properties
   ```

2. Fill in your MySQL credentials in `dbconfig.properties`
    - **Note:** Your real `dbconfig.properties` is ignored in Git — only edit your local copy.


3. Compile and run:
   ```bash
   javac -cp .:lib/mysql-connector-j-9.2.0.jar -d out Main.java $(find app -name "*.java")
   java -cp out:lib/mysql-connector-j-9.2.0.jar Main
   ```

   - **Replace the mysql-connector with your version.**