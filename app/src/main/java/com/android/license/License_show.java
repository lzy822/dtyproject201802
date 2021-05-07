package com.android.license;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class License_show extends AppCompatActivity {
    List<license_test> license_tests;
    List<licenses> lists;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    license_testAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_show);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        RefreshRecycler();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maintoolbar, menu);
        menu.findItem(R.id.query).setVisible(false);
        menu.findItem(R.id.delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case  R.id.back:
                finish();
                break;
            case  R.id.delete:
                LitePal.deleteAll(licenses.class);
                break;
            case  R.id.sort:
                if (isSpecificEnterpriseStatue){
                    showFilteredChoiceDialog();
                }else{
                    showStandardChoiceDialog();
                }
                break;
            case  R.id.output:
                OutputEvent();
                break;
        }
        return true;
    }

    private StringBuffer MakeFileHead(StringBuffer stringBuffer){
        StringBuffer stringBuffer1 = new StringBuffer();
        stringBuffer1.append("企业名").append(",");
        stringBuffer1.append("用户名").append(",");
        stringBuffer1.append("系统名").append(",");
        stringBuffer1.append("设备码").append(",");
        stringBuffer1.append("授权码").append(",");
        stringBuffer1.append("授权时间").append(",");
        stringBuffer1.append("开始时间").append(",");
        stringBuffer1.append("结束时间").append("\n");
        String newStr = stringBuffer1.toString();
        try {
            //newStr = URLEncoder.encode(newStr, "utf-8");
        }
        catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
        }
        return stringBuffer.append(newStr);
    }

    private StringBuffer MakeLicenseRowInfo(StringBuffer stringBuffer, license_test license_test){
        StringBuffer stringBuffer1 = new StringBuffer();
        stringBuffer1.append(license_test.getEnterprise()).append(",");
        stringBuffer1.append(license_test.getName()).append(",");
        stringBuffer1.append(license_test.getSystemnum()).append(",");
        stringBuffer1.append(license_test.getImei()).append(",");
        stringBuffer1.append(license_test.getPassword()).append(",");
        stringBuffer1.append(license_test.getRegisterDate()).append(",");
        stringBuffer1.append(license_test.getStartDate()).append(",");
        stringBuffer1.append(license_test.getEndDate()).append("\n");
        String newStr = stringBuffer1.toString();
        try {
            //newStr = URLEncoder.encode(newStr, "utf-8");
        }
        catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
        }
        return stringBuffer.append(newStr);
    }

    private void OutputEvent(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer = MakeFileHead(stringBuffer);
        for (int i = 0; i < license_tests.size(); i++) {
            license_test license_test = license_tests.get(i);
            stringBuffer = MakeLicenseRowInfo(stringBuffer, license_test);
        }
        File file = new File(Environment.getExternalStorageDirectory() + "/" + License_show.this.getResources().getText(R.string.save_folder_name1) + "/Output");
        if (!file.exists()){
            file.mkdirs();
        }
        final String outputPath = Long.toString(System.currentTimeMillis());
        File file1 = new File(Environment.getExternalStorageDirectory() + "/" + License_show.this.getResources().getText(R.string.save_folder_name1) + "/Output" + "/",  outputPath + ".txt");
        try {
            FileOutputStream of = new FileOutputStream(file1);
            of.write(stringBuffer.toString().getBytes());
            of.close();
        }catch (IOException e){
            Toast.makeText(License_show.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void RefreshRecycler(){
        license_tests = new ArrayList<license_test>();
        lists = new ArrayList<licenses>();
        lists = LitePal.findAll(licenses.class);
        int size = lists.size();
        int isOk = 0;
        for (int i = 0; i < size; i++){
            if (verifyDate(lists.get(i).getEndDate())) {
                isOk++;
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        getSupportActionBar().setTitle("共" + Integer.toString(size) + "条, " + Integer.toString(isOk) + "条有效");
        for (int i = 0; i < size; i++){
            //license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getRegisterDate());
            if (!verifyDate(lists.get(i).getEndDate())) {
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new license_testAdapter(license_tests);
        adapter.setOnItemClickListener(new license_testAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改企业名").setView(editText);
                editText.setText(licenseTest.getEnterprise().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecycler();
                            }
                        }).show();
            }
        });
        adapter.setOnItemLongClickListener(new license_testAdapter.OnRecyclerItemLongListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改姓名").setView(editText);
                editText.setText(licenseTest.getName().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecycler();
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void RefreshRecycler(final String enterprise){
        license_tests = new ArrayList<license_test>();
        lists = new ArrayList<licenses>();
        lists = LitePal.findAll(licenses.class);
        int size = lists.size();
        int isOk = 0;
        for (int i = 0; i < size; i++){
            if (verifyDate(lists.get(i).getEndDate()) && lists.get(i).getEnterprise().equals(enterprise)) {
                isOk++;
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        for (int i = 0; i < size; i++){
            //license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getRegisterDate());
            if (!verifyDate(lists.get(i).getEndDate()) && lists.get(i).getEnterprise().equals(enterprise)) {
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        getSupportActionBar().setTitle("共" + Integer.toString(license_tests.size()) + "条, " + Integer.toString(isOk) + "条有效");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new license_testAdapter(license_tests);
        adapter.setOnItemClickListener(new license_testAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改企业名").setView(editText);
                editText.setText(licenseTest.getEnterprise().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecycler(enterprise);
                            }
                        }).show();
            }
        });
        adapter.setOnItemLongClickListener(new license_testAdapter.OnRecyclerItemLongListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改姓名").setView(editText);
                editText.setText(licenseTest.getName().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecycler(enterprise);
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private boolean verifyDate(String endDate){
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date nowDate = new Date(System.currentTimeMillis());
        Date endTimeDate = null;
        try {
            if (!endDate.isEmpty()){
                endTimeDate = df.parse(endDate);
            }
        }catch (ParseException e){
            Toast.makeText(this, "发生错误, 请联系我们!", Toast.LENGTH_LONG).show();
        }
        if (nowDate.getTime() > endTimeDate.getTime()){
            return false;
        }else return true;
    }

    String CurrentEnterprise = "";
    boolean isSpecificEnterpriseStatue = false;
    int yourChoice;
    private void showStandardChoiceDialog(){
        final String[] items = { "开始时间排序","到期时间排序","只看某单位" };
        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(License_show.this);
        singleChoiceDialog.setTitle("筛选排序");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            /*Toast.makeText(License_show.this,
                                    "你选择了" + items[yourChoice],
                                    Toast.LENGTH_SHORT).show();*/
                            switch (yourChoice){
                                case 0:
                                    RefreshRecyclerForStartDate();
                                    break;
                                case 1:
                                    RefreshRecyclerForEndDate();
                                    break;
                                case 2:
                                    ShowEnterpriseChoiceDialog();
                                    break;
                            }
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void ShowEnterpriseChoiceDialog(){
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
        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(License_show.this);
        singleChoiceDialog.setTitle("筛选排序");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            isSpecificEnterpriseStatue = true;
                            CurrentEnterprise = items[yourChoice];
                            RefreshRecycler(items[yourChoice]);
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void showFilteredChoiceDialog(){
        final String[] items = { "开始时间排序","到期时间排序","查看所有"};
        yourChoice = 0;
        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(License_show.this);
        singleChoiceDialog.setTitle("筛选排序");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (yourChoice != -1) {
                            /*Toast.makeText(License_show.this,
                                    "你选择了" + items[yourChoice],
                                    Toast.LENGTH_SHORT).show();*/
                            switch (yourChoice){
                                case 0:
                                    RefreshRecyclerForStartDate(CurrentEnterprise);
                                    break;
                                case 1:
                                    RefreshRecyclerForEndDate(CurrentEnterprise);
                                    break;
                                case 2:
                                    CurrentEnterprise = "";
                                    isSpecificEnterpriseStatue = false;
                                    RefreshRecycler();
                                    break;
                            }
                        }
                    }
                });
        singleChoiceDialog.show();
    }

    private void RefreshRecyclerForStartDate(final String enterpriseStr){
        final List<licenses> licensesList = LitePal.where("enterprise = ?", enterpriseStr).find(licenses.class);

        int len = licensesList.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (Long.valueOf(ParseDate(licensesList.get(j).getStartDate())) > Long.valueOf(ParseDate(licensesList.get(j+1).getStartDate()))) {        // 相邻元素两两对比
                    licenses temp = licensesList.get(j+1);        // 元素交换
                    licensesList.set(j+1, licensesList.get(j));
                    licensesList.set(j, temp);
                }
            }
        }

        license_tests = new ArrayList<license_test>();
        lists = new ArrayList<licenses>();
        lists = licensesList;
        int size = lists.size();
        int isOk = 0;
        for (int i = 0; i < size; i++){
            if (verifyDate(lists.get(i).getEndDate())) {
                isOk++;
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        for (int i = 0; i < size; i++){
            //license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getRegisterDate());
            if (!verifyDate(lists.get(i).getEndDate())) {
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        getSupportActionBar().setTitle("共" + Integer.toString(license_tests.size()) + "条, " + Integer.toString(isOk) + "条有效");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new license_testAdapter(license_tests);
        adapter.setOnItemClickListener(new license_testAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改企业名").setView(editText);
                editText.setText(licenseTest.getEnterprise().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecyclerForStartDate(enterpriseStr);
                            }
                        }).show();
            }
        });
        adapter.setOnItemLongClickListener(new license_testAdapter.OnRecyclerItemLongListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改姓名").setView(editText);
                editText.setText(licenseTest.getName().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecyclerForStartDate(enterpriseStr);
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void RefreshRecyclerForEndDate(final String enterpriseStr){
        List<licenses> licensesList = LitePal.where("enterprise = ?", enterpriseStr).find(licenses.class);

        int len = licensesList.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (Long.valueOf(ParseDate(licensesList.get(j).getEndDate())) > Long.valueOf(ParseDate(licensesList.get(j+1).getEndDate()))) {        // 相邻元素两两对比
                    licenses temp = licensesList.get(j+1);        // 元素交换
                    licensesList.set(j+1, licensesList.get(j));
                    licensesList.set(j, temp);
                }
            }
        }

        license_tests = new ArrayList<license_test>();
        lists = new ArrayList<licenses>();
        lists = licensesList;
        int size = lists.size();
        int isOk = 0;
        for (int i = 0; i < size; i++){
            if (verifyDate(lists.get(i).getEndDate())) {
                isOk++;
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        for (int i = 0; i < size; i++){
            //license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getRegisterDate());
            if (!verifyDate(lists.get(i).getEndDate())) {
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        getSupportActionBar().setTitle("共" + Integer.toString(license_tests.size()) + "条, " + Integer.toString(isOk) + "条有效");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new license_testAdapter(license_tests);
        adapter.setOnItemClickListener(new license_testAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改企业名").setView(editText);
                editText.setText(licenseTest.getEnterprise().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecyclerForEndDate(enterpriseStr);
                            }
                        }).show();
            }
        });
        adapter.setOnItemLongClickListener(new license_testAdapter.OnRecyclerItemLongListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改姓名").setView(editText);
                editText.setText(licenseTest.getName().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecyclerForEndDate(enterpriseStr);
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void RefreshRecyclerForStartDate(){
        List<licenses> licensesList = LitePal.findAll(licenses.class);

        int len = licensesList.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (Long.valueOf(ParseDate(licensesList.get(j).getStartDate())) > Long.valueOf(ParseDate(licensesList.get(j+1).getStartDate()))) {        // 相邻元素两两对比
                    licenses temp = licensesList.get(j+1);        // 元素交换
                    licensesList.set(j+1, licensesList.get(j));
                    licensesList.set(j, temp);
                }
            }
        }

        license_tests = new ArrayList<license_test>();
        lists = new ArrayList<licenses>();
        lists = licensesList;
        int size = lists.size();
        int isOk = 0;
        for (int i = 0; i < size; i++){
            if (verifyDate(lists.get(i).getEndDate())) {
                isOk++;
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        getSupportActionBar().setTitle("共" + Integer.toString(size) + "条, " + Integer.toString(isOk) + "条有效");
        for (int i = 0; i < size; i++){
            //license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getRegisterDate());
            if (!verifyDate(lists.get(i).getEndDate())) {
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new license_testAdapter(license_tests);
        adapter.setOnItemClickListener(new license_testAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改企业名").setView(editText);
                editText.setText(licenseTest.getEnterprise().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecyclerForStartDate();
                            }
                        }).show();
            }
        });
        adapter.setOnItemLongClickListener(new license_testAdapter.OnRecyclerItemLongListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改姓名").setView(editText);
                editText.setText(licenseTest.getName().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecyclerForStartDate();
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void RefreshRecyclerForEndDate(){
        List<licenses> licensesList = LitePal.findAll(licenses.class);

        int len = licensesList.size();
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len - 1 - i; j++) {
                if (Long.valueOf(ParseDate(licensesList.get(j).getEndDate())) > Long.valueOf(ParseDate(licensesList.get(j+1).getEndDate()))) {        // 相邻元素两两对比
                    licenses temp = licensesList.get(j+1);        // 元素交换
                    licensesList.set(j+1, licensesList.get(j));
                    licensesList.set(j, temp);
                }
            }
        }

        license_tests = new ArrayList<license_test>();
        lists = new ArrayList<licenses>();
        lists = licensesList;
        int size = lists.size();
        int isOk = 0;
        for (int i = 0; i < size; i++){
            if (verifyDate(lists.get(i).getEndDate())) {
                isOk++;
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        getSupportActionBar().setTitle("共" + Integer.toString(size) + "条, " + Integer.toString(isOk) + "条有效");
        for (int i = 0; i < size; i++){
            //license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getRegisterDate());
            if (!verifyDate(lists.get(i).getEndDate())) {
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate(), lists.get(i).getEnterprise()
                        , lists.get(i).getName(), lists.get(i).getSystemNum());
                license_tests.add(licenseTest);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new license_testAdapter(license_tests);
        adapter.setOnItemClickListener(new license_testAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改企业名").setView(editText);
                editText.setText(licenseTest.getEnterprise().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecyclerForEndDate();
                            }
                        }).show();
            }
        });
        adapter.setOnItemLongClickListener(new license_testAdapter.OnRecyclerItemLongListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final license_test licenseTest = license_tests.get(position);
                final EditText editText = new EditText(License_show.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(License_show.this);
                inputDialog.setTitle("修改姓名").setView(editText);
                editText.setText(licenseTest.getName().trim());
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                licenses licenses = new licenses();
                                licenses.setEnterprise(editText.getText().toString());
                                licenses.updateAll("imei = ?", licenseTest.getImei());
                                RefreshRecyclerForEndDate();
                            }
                        }).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /*private long ParseDate(String Date){
        Date = Date.replace('年', Character.MIN_VALUE);
        Date = Date.replace('月', Character.MIN_VALUE);
        Date = Date.replace('日', Character.MIN_VALUE);
        return Long.valueOf(Date);
    }*/

    public static String ParseDate(String s){
        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher m=pat.matcher(s);
        return m.replaceAll("");
    }
}
