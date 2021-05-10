package com.android.license;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class License_main extends AppCompatActivity {
    EditText editText;
    Button button1;
    TextView textView;
    Toolbar tb;
    String password;
    RadioGroup radioGroup;
    int deltaTime;
    private static final String TAG = "License_main";

    //请求授权
    private void requestAuthority(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(License_main.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(License_main.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(License_main.this, permissions, 118);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_main);
        requestAuthority();
        deltaTime = 7;
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.week:
                        deltaTime = 7;
                        break;
                    case R.id.onemonth:
                        deltaTime = 30;
                        break;
                    case R.id.threemonth:
                        deltaTime = 90;
                        break;
                    case R.id.halfyear:
                        deltaTime = 180;
                        break;
                    case R.id.wholeyear:
                        deltaTime = 366;
                        break;
                    case R.id.forever:
                        deltaTime = 3660;
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
                EditText EnterpriseEditText = (EditText) findViewById(R.id.inputenterprise_edit);
                EditText NameEditText = (EditText) findViewById(R.id.inputname_edit);
                String EnterpriseStr = EnterpriseEditText.getText().toString().trim();
                String NameStr = NameEditText.getText().toString().trim();
                if (NameStr.length()>0 && EnterpriseStr.length()>0) {
                    String str = editText.getText().toString();
                    if (str.length() >= 14 & str.length() <= 16) {
                        SimpleDateFormat df1 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                        SimpleDateFormat df2 = new SimpleDateFormat("yyyy年MM月dd日");
                        Date date = new Date(System.currentTimeMillis());
                        String time = df1.format(date);
                        String startTime = df2.format(date);
                        Log.w(TAG, "startDate: " + startTime);
                        List<licenses> lsts = LitePal.findAll(licenses.class);
                        int size = lsts.size();
                        PasswordParameters pp = new PasswordParameters(str, size, lsts, time, startTime);
                        if (str.length() == 15) {
                            ManageAndroid10Password(pp);
                        } else {
                            ManageOldAndroidVersionPassword(pp);
                        }
                    } else {
                        ManageOtherSystemPassword(str);
                    }
                }
                else{
                    Toast.makeText(License_main.this, "请完善输入信息！企业和用户名不可为空！", Toast.LENGTH_LONG);
                }
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

        final Button EnterpriseSearchButton = (Button) findViewById(R.id.enterprisesearch_button);
        EnterpriseSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEnterpriseDownPullMenu();
            }
        });

        final Button NameSearchButton = (Button) findViewById(R.id.namesearch_button);
        NameSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowNameDownPullMenu();
            }
        });
    }

    private void ShowNameDownPullMenu(){
        final EditText inputname_edit = (EditText) findViewById(R.id.inputname_edit);
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);

        HashMap<String, String> hashMap = new HashMap<>();
        List<licenses> licensesList = LitePal.findAll(licenses.class);
        for (int i = 0; i < licensesList.size(); i++) {
            String name = licensesList.get(i).getName();
            if (!hashMap.containsKey(name))
                hashMap.put(name, "");
        }
        final String[] items = new String[hashMap.size()];
        Iterator iter = hashMap.entrySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            items[i++] = (String) key;
        }

        // ListView适配器
        listPopupWindow.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items));

        // 选择item的监听事件
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String name = items[position];

                inputname_edit.setText(name);
            }
        });

        // 对话框的宽高
        listPopupWindow.setWidth(600);
        listPopupWindow.setHeight(600);

        // ListPopupWindow的锚,弹出框的位置是相对当前View的位置
        listPopupWindow.setAnchorView(inputname_edit);

        // ListPopupWindow 距锚view的距离
        listPopupWindow.setHorizontalOffset(0);
        listPopupWindow.setVerticalOffset(5);

        listPopupWindow.setModal(false);

        listPopupWindow.show();
    }

    private void ShowEnterpriseDownPullMenu(){
        final EditText inputenterprise_edit = (EditText) findViewById(R.id.inputenterprise_edit);

        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);

        HashMap<String, String> hashMap = new HashMap<>();
        List<licenses> licensesList = LitePal.findAll(licenses.class);
        for (int i = 0; i < licensesList.size(); i++) {
            String enterprise = licensesList.get(i).getEnterprise();
            if (!hashMap.containsKey(enterprise))
                hashMap.put(enterprise, "");
        }
        final String[] items = new String[hashMap.size()];
        Iterator iter = hashMap.entrySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            items[i++] = (String) key;
        }

        // ListView适配器
        listPopupWindow.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items));

        // 选择item的监听事件
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String enterprise = items[position];

                inputenterprise_edit.setText(enterprise);
            }
        });

        // 对话框的宽高
        listPopupWindow.setWidth(600);
        listPopupWindow.setHeight(600);

        // ListPopupWindow的锚,弹出框的位置是相对当前View的位置
        listPopupWindow.setAnchorView(inputenterprise_edit);

        // ListPopupWindow 距锚view的距离
        listPopupWindow.setHorizontalOffset(0);
        listPopupWindow.setVerticalOffset(5);

        listPopupWindow.setModal(false);

        listPopupWindow.show();
    }

    private void ManageOtherSystemPassword(String str) {
        password = PasswordUtil.encryption(str);
        textView.setText("授权码为: " + password + "(长按复制)");
        Toast.makeText(License_main.this, "该设备码与时限无关", Toast.LENGTH_LONG).show();
    }

    private String GetOldVersionPassword(String str){
        String password = "";
        try {
            password = PasswordUtil.getDeltaDate(PasswordUtil.reverseStr(PasswordUtil.getPassword1(str.substring(str.length() - 6))), deltaTime);
        }catch (Exception e){
            Toast.makeText(License_main.this, "获取老版本验证码出错。可能是对方机器获取了假的验证码，请获取对方设备的硬件型号和软件版本号", Toast.LENGTH_LONG).show();
        }
        return password;
    }

    private String GetAndroid10Password(String str) {
        String password = "";
        try {
            password = PasswordUtil.getDeltaDate(PasswordUtil.reverseStr(PasswordUtil.getPassword1ForAndroid10(str.substring(str.length() - 6))), deltaTime);
        }catch (Exception e){
            Toast.makeText(License_main.this, "获取安卓10验证码出错。可能是对方机器获取了假的验证码，请获取对方设备的硬件型号和软件版本号", Toast.LENGTH_LONG).show();
        }
        return password;
    }

    private void ManageOldAndroidVersionPassword(PasswordParameters pp) {
        boolean isExist = hasExistPassword(pp);

        if (!isExist){
            password = GetOldVersionPassword(pp.str);

            SaveAndroidPasswordInfo(pp);
        }

        textView.setText("授权码为: " + password + "(长按复制)");
    }

    private void ManageAndroid10Password(PasswordParameters pp) {
        boolean isExist = false;
        try {
            //long test = Long.valueOf(str);
            isExist = hasExistPassword(pp);

            Log.w(TAG, "onClicktest: " + 2);
            if (!isExist){
                password = GetAndroid10Password(pp.str);

                SaveAndroidPasswordInfo(pp);
            }

            textView.setText("授权码为: " + password + "(长按复制)");

        }catch (NumberFormatException e){
            Toast.makeText(License_main.this, "请输入正确的设备码" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private Boolean hasExistPassword(PasswordParameters pp){
        boolean isExist = false;

        try {
            for (int i = 0; i < pp.size; i++){
                if (pp.str.contentEquals(pp.lsts.get(i).getImei()) && (DataUtil.verifyDate(pp.lsts.get(i).getEndDate()) == DataUtil.VerifyDateEnum.CONFIRM)){
                    isExist = true;
                    password = pp.lsts.get(i).getPassword();
                    break;
                }
            }
        }
        catch (Exception e){
            Toast.makeText(License_main.this, "校验验证码出错，请联系我或重装Licence" + e.toString(), Toast.LENGTH_LONG).show();
        }

        return isExist;
    }

    private void SaveAndroidPasswordInfo(PasswordParameters pp){
        EditText EnterpriseEditText = (EditText) findViewById(R.id.inputenterprise_edit);
        EditText NameEditText = (EditText) findViewById(R.id.inputname_edit);
        String EnterpriseStr = EnterpriseEditText.getText().toString().trim();
        String NameStr = NameEditText.getText().toString().trim();

        licenses license = new licenses();
        license.setImei(pp.str);
        license.setPassword(password);
        license.setRegisterDate(pp.time);
        license.setStartDate(pp.startTime);
        license.setEndDate(DataUtil.datePlus(pp.startTime, deltaTime));
        license.setSystemNum(0);
        license.setEnterprise(EnterpriseStr);
        license.setName(NameStr);
            /*if (deltaTime == 7){
                license.setEndDate(datePlus(pp.startTime, 7));
            }else if (deltaTime == 180){
                license.setEndDate(datePlus(pp.startTime, 180));
            }else if (deltaTime == 366){
                license.setEndDate(datePlus(pp.startTime, 366));
            }else if (deltaTime == 30){
                license.setEndDate(datePlus(pp.startTime, 30));
            }else if (deltaTime == 90){
                license.setEndDate(datePlus(pp.startTime, 90));
            }else if (deltaTime == 3660){
                license.setEndDate(datePlus(pp.startTime, 3660));
            }*/
        license.save();
    }

    private void SaveOtherSystemPasswordInfo(PasswordParameters pp){
        licenses license = new licenses();
        license.setImei(pp.str);
        license.setPassword(password);
        license.setRegisterDate(pp.time);
        license.setStartDate(pp.startTime);
        license.setEndDate(DataUtil.datePlus(pp.startTime, deltaTime));
        license.setSystemNum(1);
            /*if (deltaTime == 7){
                license.setEndDate(datePlus(pp.startTime, 7));
            }else if (deltaTime == 180){
                license.setEndDate(datePlus(pp.startTime, 180));
            }else if (deltaTime == 366){
                license.setEndDate(datePlus(pp.startTime, 366));
            }else if (deltaTime == 30){
                license.setEndDate(datePlus(pp.startTime, 30));
            }else if (deltaTime == 90){
                license.setEndDate(datePlus(pp.startTime, 90));
            }else if (deltaTime == 3660){
                license.setEndDate(datePlus(pp.startTime, 3660));
            }*/
        license.save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maintoolbar, menu);
        menu.findItem(R.id.back).setVisible(false);
        menu.findItem(R.id.delete).setVisible(false);
        menu.findItem(R.id.output).setVisible(false);
        menu.findItem(R.id.sort).setVisible(false);
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
