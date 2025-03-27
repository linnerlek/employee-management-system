package app.model;

public class User {
    private String email;
    private String password;
    private String role;

    public User(String email, String password, String jobTitleId) {
        this.email = email;
        this.password = password;
        this.role = jobTitleId.startsWith("9") ? "Admin" : "Employee";
    }

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(role);
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(String jobTitleId) {
        this.role = jobTitleId.startsWith("9") ? "Admin" : "Employee";
    }
}
