package com.android.license;

public class license_test {
    private String imei;
    private String password;
    private String startDate;
    private String endDate;
    private String registerDate;
    private String enterprise;
    private String name;
    private int systemnum;

    public license_test(String imei, String password, String registerDate) {
        this.imei = imei;
        this.password = password;
        this.registerDate = registerDate;
    }

    /*public license_test(String imei, String password, String startDate, String endDate, String registerDate) {
        this.imei = imei;
        this.password = password;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registerDate = registerDate;
    }*/

    public license_test(String imei, String password, String startDate, String endDate, String registerDate, String enterprise, String name, int systemnum) {
        this.imei = imei;
        this.password = password;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registerDate = registerDate;
        this.enterprise = enterprise;
        this.name = name;
        this.systemnum = systemnum;
    }

    public String getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSystemnum() {
        return systemnum;
    }

    public void setSystemnum(int systemnum) {
        this.systemnum = systemnum;
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
