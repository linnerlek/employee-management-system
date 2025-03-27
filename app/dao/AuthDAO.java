package app.dao;

import java.sql.*;
import app.model.User;
import app.db.DBConnection;

public class AuthDAO {
    public static User login(String email, String password) {
        // TODO: Implement DB logic
        // JOIN employees + employee_job_titles to get job_title_id
        // Return new User(email, password, jobTitleId)

        // TEMP for testing:
        String mockJobTitleId = "901";
        return new User(email, password, mockJobTitleId);
    }
}
