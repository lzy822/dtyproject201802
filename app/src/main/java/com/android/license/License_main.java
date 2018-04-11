package com.android.license;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class License_main extends AppCompatActivity {
    EditText editText;
    Button button1;
    TextView textView;
    Toolbar tb;
    String password;
    RadioGroup radioGroup;
    int deltaTime;
    private static final String TAG = "License_main";

    //判断日期
    private boolean verifyDate(String endDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date nowDate = new Date(System.currentTimeMillis());
        Date endTimeDate = null;
        try {
            if (!endDate.isEmpty()){
                endTimeDate = df.parse(endDate);
            }
        }catch (ParseException e){
            Toast.makeText(License_main.this, "发生错误, 请联系我们!", Toast.LENGTH_LONG).show();
        }
        if (nowDate.getTime() > endTimeDate.getTime()){
            return false;
        }else return true;
    }

    //计算识别码
    private String getPassword(String deviceId){
        String password;
        password = "l" + encryption(deviceId.substring(9)) + "ZY";
        Log.w(TAG, "getPassword: " +  password);
        if (deltaTime == 7){
            password = password + "x";
        }else if (deltaTime == 180){
            password = password + "X";
        }else if (deltaTime == 366){
            password = password + "y";
        }
        return password;
    }

    //编码
    private String encryption(String password){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_main);
        deltaTime = 7;
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.week:
                        deltaTime = 7;
                        break;
                    case R.id.halfyear:
                        deltaTime = 180;
                        break;
                    case R.id.wholeyear:
                        deltaTime = 366;
                        break;
                }
            }
        });
        LitePal.getDatabase();
        password = "";
        editText = (EditText) findViewById(R.id.inputic_edit);
        button1 = (Button) findViewById(R.id.ok_button);
        textView = (TextView) findViewById(R.id.ic_text);
        tb = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(tb);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                if (str.length() >= 14 & str.length() <= 15){
                    SimpleDateFormat df1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    SimpleDateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日");
                    Date date = new Date(System.currentTimeMillis());
                    String time = df1.format(date);
                    String startTime = df2.format(date);
                    Log.w(TAG, "startDate: " + startTime );
                    List<licenses> lsts = DataSupport.findAll(licenses.class);
                    int size = lsts.size();
                    boolean isExist = false;
                    if (str.length() == 15){
                        try {
                            long test = Long.valueOf(str);
                            for (int i = 0; i < size; i++){
                                if (str.contentEquals(lsts.get(i).getImei()) & verifyDate(lsts.get(i).getEndDate())){
                                    isExist = true;
                                    password = lsts.get(i).getPassword();
                                    break;
                                }
                            }
                            if (!isExist){
                                password = getPassword(str);
                                textView.setText("授权码为: " + password + "(长按复制)");
                                licenses license = new licenses();
                                license.setImei(str);
                                license.setPassword(password);
                                license.setRegisterDate(time);
                                license.setStartDate(startTime);
                                if (deltaTime == 7){
                                    license.setEndDate(getDateStr(startTime, 7));
                                }else if (deltaTime == 180){
                                    license.setEndDate(getDateStr(startTime, 180));
                                }else if (deltaTime == 366){
                                    license.setEndDate(getDateStr(startTime, 366));
                                }
                                license.save();
                            }else {
                                textView.setText("授权码为: " + password + "(长按复制)");
                            }
                        }catch (NumberFormatException e){
                            Toast.makeText(License_main.this, "请输入正确的设备码", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        for (int i = 0; i < size; i++){
                            if (str.contentEquals(lsts.get(i).getImei()) & verifyDate(lsts.get(i).getEndDate())){
                                isExist = true;
                                password = lsts.get(i).getPassword();
                                break;
                            }
                        }
                        if (!isExist){
                            password = getPassword(str);
                            textView.setText("授权码为: " + password + "(长按复制)");
                            licenses license = new licenses();
                            license.setImei(str);
                            license.setPassword(password);
                            license.setRegisterDate(time);
                            license.setStartDate(startTime);
                            if (deltaTime == 7){
                                license.setEndDate(getDateStr(startTime, 7));
                            }else if (deltaTime == 180){
                                license.setEndDate(getDateStr(startTime, 180));
                            }else if (deltaTime == 366){
                                license.setEndDate(getDateStr(startTime, 366));
                            }
                            license.save();
                        }else {
                            textView.setText("授权码为: " + password + "(长按复制)");
                        }
                    }
                }else Toast.makeText(License_main.this, "请输入正确的设备码", Toast.LENGTH_LONG).show();
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String str1 = textView.getText().toString();
                if (!str1.contentEquals("授权码为: ")){
                    ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    manager.setText(password);
                    Toast.makeText(License_main.this, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(License_main.this, "请先获取授权码", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maintoolbar, menu);
        menu.findItem(R.id.back).setVisible(false);
        menu.findItem(R.id.delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case  R.id.query:
                Intent intent = new Intent(License_main.this, License_show.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
