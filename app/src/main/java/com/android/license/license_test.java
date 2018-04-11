package com.android.license;

public class license_test {
    private String imei;
    private String password;
    private String startDate;
    private String endDate;
    private String registerDate;

    public license_test(String imei, String password, String registerDate) {
        this.imei = imei;
        this.password = password;
        this.registerDate = registerDate;
    }

    public license_test(String imei, String password, String startDate, String endDate, String registerDate) {
        this.imei = imei;
        this.password = password;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registerDate = registerDate;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
}
