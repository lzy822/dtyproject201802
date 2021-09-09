package com.android.license;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PasswordUtil {
    //计算识别码
    public static String getPassword1(String deviceId){
        //String password;
        //password = "l" + encryption(deviceId) + "ZY";
        SimpleDateFormat df = new SimpleDateFormat(MyApplication.getContext().getResources().getText(R.string.Date_1).toString());
        Date nowDate = new Date(System.currentTimeMillis());
        return encryption(deviceId) + encryption1(df.format(nowDate));
    }
    //计算识别码
    public static String getPassword1ForAndroid10(String deviceId){
        //String password;
        //password = "l" + encryption(deviceId) + "ZY";
        SimpleDateFormat df = new SimpleDateFormat(MyApplication.getContext().getResources().getText(R.string.Date_1).toString());
        Date nowDate = new Date(System.currentTimeMillis());
        return encryptionForAndroid10(deviceId) + encryption1(df.format(nowDate));
    }

    public static String getDeltaDate(String password, int deltaTime){
        if (deltaTime == 7){
            password = password + "z";
        }else if (deltaTime == 180){
            password = password + "x";
        }else if (deltaTime == 366){
            password = password + "c";
        }else if (deltaTime == 30){
            password = password + "v";
        }else if (deltaTime == 90){
            password = password + "b";
        }else if (deltaTime == 3660){
            password = password + "n";
        }
        return password;
    }

    //编码
    public static String encryptionForAndroid10(String password){
        password = password.replace("0", "q");
        password = password.replace("1", "w");
        password = password.replace("2", "e");
        password = password.replace("3", "r");
        password = password.replace("4", "t");
        password = password.replace("5", "y");
        password = password.replace("6", "u");
        password = password.replace("7", "i");
        password = password.replace("8", "o");
        password = password.replace("9", "p");
        password = password.replace("a", "a");
        password = password.replace("b", "s");
        password = password.replace("c", "d");
        password = password.replace("d", "f");
        password = password.replace("e", "g");
        password = password.replace("f", "h");

        return password;
    }

    //编码
    public static String encryption(String password){
        password = password.replace("0", "q");
        password = password.replace("1", "w");
        password = password.replace("2", "e");
        password = password.replace("3", "r");
        password = password.replace("4", "t");
        password = password.replace("5", "y");
        password = password.replace("6", "u");
        password = password.replace("7", "i");
        password = password.replace("8", "o");
        password = password.replace("9", "p");
        password = password.replace("A", "a");
        password = password.replace("B", "s");
        password = password.replace("C", "d");
        password = password.replace("D", "f");
        password = password.replace("E", "g");
        password = password.replace("F", "h");

        return password;
    }

    //编码
    public static String encryption1(String password){
        password = password.replace("0", "q");
        password = password.replace("1", "w");
        password = password.replace("2", "e");
        password = password.replace("3", "r");
        password = password.replace("4", "t");
        password = password.replace("5", "y");
        password = password.replace("6", "u");
        password = password.replace("7", "i");
        password = password.replace("8", "o");
        password = password.replace("9", "p");
        return password;
    }

    public static String getDeltaDatex(String password, int deltaTime){
        if (deltaTime == 7){
            password = password + "x";
        }else if (deltaTime == 180){
            password = password + "X";
        }else if (deltaTime == 366){
            password = password + "y";
        }else if (deltaTime == 30){
            password = password + "g";
        }else if (deltaTime == 90){
            password = password + "G";
        }else if (deltaTime == 3660){
            password = password + "j";
        }
        return password;
    }

    //编码
    public static String encryptionForAndroid10x(String password){
        password = password.replace("0", "q");
        password = password.replace("1", "R");
        password = password.replace("2", "V");
        password = password.replace("3", "z");
        password = password.replace("4", "T");
        password = password.replace("5", "b");
        password = password.replace("6", "L");
        password = password.replace("7", "s");
        password = password.replace("8", "W");
        password = password.replace("9", "F");
        password = password.replace("a", "d");
        password = password.replace("b", "o");
        password = password.replace("c", "O");
        password = password.replace("d", "n");
        password = password.replace("e", "v");
        password = password.replace("f", "C");

        return password;
    }

    //编码
    public static String encryptionx(String password){
        password = password.replace("0", "q");
        password = password.replace("1", "R");
        password = password.replace("2", "V");
        password = password.replace("3", "z");
        password = password.replace("4", "T");
        password = password.replace("5", "b");
        password = password.replace("6", "L");
        password = password.replace("7", "s");
        password = password.replace("8", "W");
        password = password.replace("9", "F");
        password = password.replace("A", "d");
        password = password.replace("B", "o");
        password = password.replace("C", "O");
        password = password.replace("D", "n");
        password = password.replace("E", "v");
        password = password.replace("F", "C");

        return password;
    }

    //编码
    public static String encryption1x(String password){
        password = password.replace("0", "a");
        password = password.replace("1", "Z");
        password = password.replace("2", "o");
        password = password.replace("3", "M");
        password = password.replace("4", "e");
        password = password.replace("5", "w");
        password = password.replace("6", "z");
        password = password.replace("7", "R");
        password = password.replace("8", "D");
        password = password.replace("9", "q");
        return password;
    }

    //反转授权码
    public static String reverseStr(String str){
        return str.substring(6, 10) + str.substring(0, 6) + str.substring(10);
    }

    //恢复反转授权码
    private String reReverseStr(String str){
        return str.substring(4,10) + str.substring(0, 4) + str.substring(10);
    }

    //日期提取算法
    private String getDateFromStr(String password){
        password = password.replace("a", "0");
        password = password.replace("Z", "1");
        password = password.replace("o", "2");
        password = password.replace("M", "3");
        password = password.replace("e", "4");
        password = password.replace("w", "5");
        password = password.replace("z", "6");
        password = password.replace("R", "7");
        password = password.replace("D", "8");
        password = password.replace("q", "9");
        password = password.substring(0, 4) + "年" + password.substring(4, 6) + "月" + password.substring(6) + "日";
        return password;
    }
}
