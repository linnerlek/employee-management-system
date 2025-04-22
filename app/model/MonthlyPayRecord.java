package app.model;

public class MonthlyPayRecord {
    private String jobTitle;
    private double earnings;
    private String year;
    private String month;
    private String division;

    public MonthlyPayRecord(String jobTitle, double earnings, String year, String month, String division) {
        this.jobTitle = jobTitle;
        this.earnings = earnings;
        this.year = year;
        this.month = month;
        this.division = division;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public double getEarnings() {
        return earnings;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDivision() {
        return division;
    }
}
