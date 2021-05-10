package com.android.license;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataUtil {
    public enum VerifyDateEnum{
        CONFIRM, NOCONFIRM, ERROR
    }

    public static VerifyDateEnum verifyDate(String endDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date nowDate = new Date(System.currentTimeMillis());
        Date endTimeDate = null;
        try {
            if (!endDate.isEmpty())
                endTimeDate = df.parse(endDate);
        }catch (ParseException e){
            return VerifyDateEnum.ERROR;
        }
        if (nowDate.getTime() > endTimeDate.getTime())
            return VerifyDateEnum.NOCONFIRM;
        else
            return VerifyDateEnum.CONFIRM;
    }

    public static String datePlus(String day, int days) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date base = null;
        try {
            base = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(base);
        cal.add(Calendar.DATE, days);
        String dateOK = df.format(cal.getTime());

        return dateOK;
    }

    //Day:日期字符串例如 2015-3-10  Num:需要减少的天数例如 7
    public static String getDateStr(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() + (long)(Num * 24 * 60 * 60 * 1000));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }
}
