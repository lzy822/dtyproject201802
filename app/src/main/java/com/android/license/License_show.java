package com.android.license;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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
        license_tests = new ArrayList<license_test>();
        lists = new ArrayList<licenses>();
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        lists = DataSupport.findAll(licenses.class);
        int size = lists.size();
        getSupportActionBar().setTitle("已有" + Integer.toString(size) + "条授权信息");
        for (int i = 0; i < size; i++){
            license_test licenseTest = new license_test(lists.get(i).getImei(), lists.get(i).getPassword(), lists.get(i).getRegisterDate());
            license_tests.add(licenseTest);
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new license_testAdapter(license_tests);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maintoolbar, menu);
        menu.findItem(R.id.query).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case  R.id.back:
                finish();
                break;
        }
        return true;
    }
}
