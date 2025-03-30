package app.model;

public class User {
    private int empid;
    private String email;
    private String password;
    private String role;
    private String fname;

    public User(int empid, String email, String password, String fname, String jobTitleId) {
        this.empid = empid;
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.role = jobTitleId.startsWith("9") ? "Admin" : "Employee";
    }

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(role);
    }

    public int getEmpid() {
        return empid;
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

    public String getFname() {
        return fname;
    }

    public void setRole(String jobTitleId) {
        this.role = jobTitleId.startsWith("9") ? "Admin" : "Employee";
    }
}
