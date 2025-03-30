package app.model;

public class PayStatement {
    private int empid;
    private String name;
    private String payDate;
    private double earnings, fedTax, fedMed, fedSS, stateTax, retire401k, healthCare;

    public PayStatement(int empid, String name, String payDate,
                        double earnings, double fedTax, double fedMed, double fedSS,
                        double stateTax, double retire401k, double healthCare) {
        this.empid = empid;
        this.name = name;
        this.payDate = payDate;
        this.earnings = earnings;
        this.fedTax = fedTax;
        this.fedMed = fedMed;
        this.fedSS = fedSS;
        this.stateTax = stateTax;
        this.retire401k = retire401k;
        this.healthCare = healthCare;
    }

    public int getEmpid() { return empid; }
    public String getName() { return name; }
    public String getPayDate() { return payDate; }
    public double getEarnings() { return earnings; }
    public double getFedTax() { return fedTax; }
    public double getFedMed() { return fedMed; }
    public double getFedSS() { return fedSS; }
    public double getStateTax() { return stateTax; }
    public double getRetire401k() { return retire401k; }
    public double getHealthCare() { return healthCare; }

    public double getTotalDeductions() {
        return fedTax + fedMed + fedSS + stateTax + retire401k + healthCare;
    }

    public double getNetPay() {
        return earnings - getTotalDeductions();
    }

    
}
