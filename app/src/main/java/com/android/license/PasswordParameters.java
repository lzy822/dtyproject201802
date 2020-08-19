package com.android.license;

import java.util.List;

public class PasswordParameters {
    public String str;
    public int size;
    public List<licenses> lsts;
    public String time;
    public String startTime;

    public PasswordParameters(String str, int size, List<licenses> lsts, String time, String startTime) {
        this.str = str;
        this.size = size;
        this.lsts = lsts;
        this.time = time;
        this.startTime = startTime;
    }
}
