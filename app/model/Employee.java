package app.model;

public class Employee {
    private int empid;
    private String fname;
    private String lname;
    private String email;
    private String ssn;
    private String hireDate;
    private String salary;
    private String jobTitle;

    // Address
    private String street;
    private String zip;
    private String gender;
    private String race;
    private String dob;
    private String phone;
    private String cityName;
    private String stateName;

    // Division
    private String divisionName;

    // Payroll
    private String lastPaidDate;

    public Employee(int empid, String fname, String lname, String email, String ssn, String hireDate, String salary, String jobTitle,
                    String street, String zip, String gender, String race, String dob, String phone, String cityName, String stateName,
                    String divisionName, String lastPaidDate) {
        this.empid = empid;
        this.fname = defaultIfNull(fname);
        this.lname = defaultIfNull(lname);
        this.email = defaultIfNull(email);
        this.ssn = defaultIfNull(ssn);
        this.hireDate = defaultIfNull(hireDate);
        this.salary = defaultIfNull(salary);
        this.jobTitle = defaultIfNull(jobTitle);
        this.street = defaultIfNull(street);
        this.zip = defaultIfNull(zip);
        this.gender = defaultIfNull(gender);
        this.race = defaultIfNull(race);
        this.dob = defaultIfNull(dob);
        this.phone = defaultIfNull(phone);
        this.cityName = defaultIfNull(cityName);
        this.stateName = defaultIfNull(stateName);
        this.divisionName = defaultIfNull(divisionName);
        this.lastPaidDate = defaultIfNull(lastPaidDate);
    }

    private String defaultIfNull(String val) {
        return (val == null || val.isEmpty()) ? "Missing" : val;
    }

    public int getEmpid() { return empid; }
    public String getFname() { return fname; }
    public String getLname() { return lname; }
    public String getEmail() { return email; }
    public String getSsn() { return ssn; }
    public String getHireDate() { return hireDate; }
    public String getSalary() { return salary; }
    public String getJobTitle() { return jobTitle; }
    public String getStreet() { return street; }
    public String getZip() { return zip; }
    public String getGender() { return gender; }
    public String getRace() { return race; }
    public String getDob() { return dob; }
    public String getPhone() { return phone; }
    public String getCityName() { return cityName; }
    public String getStateName() { return stateName; }
    public String getDivisionName() { return divisionName; }
    public String getLastPaidDate() { return lastPaidDate; }
}
