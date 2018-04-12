package com.android.license;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class License_show_single extends AppCompatActivity {
    List<license_test> license_tests;
    List<licenses> lists;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    license_testAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_show_single);
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("deviceId");
        license_tests = new ArrayList<license_test>();
        lists = new ArrayList<licenses>();
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        lists = DataSupport.where("imei = ?", deviceId).find(licenses.class);
        int size = lists.size();
        int isOk = 0;
        for (int i = 0; i < size; i++){
            if (verifyDate(lists.get(i).getEndDate())) {
                isOk++;
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate());
                license_tests.add(licenseTest);
            }
        }
        getSupportActionBar().setTitle("共" + Integer.toString(size) + "条");
        for (int i = 0; i < size; i++){
            //license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getRegisterDate());
            if (!verifyDate(lists.get(i).getEndDate())) {
                license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getStartDate(), lists.get(i).getEndDate(), lists.get(i).getRegisterDate());
                license_tests.add(licenseTest);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new license_testAdapter(license_tests);
        recyclerView.setAdapter(adapter);
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
                DataSupport.deleteAll(licenses.class);
                break;
        }
        return true;
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
}
