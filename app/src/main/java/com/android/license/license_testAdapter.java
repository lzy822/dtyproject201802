package com.android.license;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class license_testAdapter extends RecyclerView.Adapter<license_testAdapter.ViewHolder>{
    private Context mContext;

    private List<license_test> mlicenseList;

    private OnRecyclerItemLongListener mOnItemLong;

    private OnRecyclerItemClickListener mOnItemClick;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //private OnRecyclerItemLongListener mOnItemLong = null;
        CardView cardView;
        TextView licenseTxt;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            licenseTxt = (TextView) view.findViewById(R.id.license_txt);



        }
    }
    public license_testAdapter(List<license_test> mlicenseList) {
        this.mlicenseList = mlicenseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.license_test_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnItemClick != null){
                    int position = holder.getAdapterPosition();
                    license_test license = mlicenseList.get(position);
                    mOnItemClick.onItemClick(v, position);
                    notifyItemRangeChanged(0, mlicenseList.size());
                }

                /*Intent intent = new Intent(mContext, License_show_single.class);
                intent.putExtra("deviceId", license.getImei());
                mContext.startActivity(intent);*/
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLong != null){
                    int position = holder.getAdapterPosition();
                    license_test license = mlicenseList.get(position);
                    mOnItemLong.onItemLongClick(v, position);
                    notifyItemRangeChanged(0, mlicenseList.size());
                    //holder.cardView.setCardBackgroundColor(Color.GRAY);
                }
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        //notifyItemChanged(position);
        if (payloads.isEmpty()){
            onBindViewHolder(holder, position);
        }else {
            notifyItemChanged(position);
            //Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_SHORT).show();
            Log.w(TAG, "find here" + payloads.toString() );
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        license_test license = mlicenseList.get(position);
        String SystemStr = "";
        switch (license.getSystemnum()){
            case 0:
                SystemStr="安卓";
                break;
            case 1:
                SystemStr="Windows";
                break;
        }
        String str = "企业名: " + license.getEnterprise() + "\n" + "用户名: " + license.getName() + "\n" +  "系统号: " + SystemStr + "\n" +  "设备码: " + license.getImei() + "\n" + "授权码: " + license.getPassword() + "\n" + "授权时间: " + license.getRegisterDate()
                + "\n" + "开始时间: " + license.getStartDate() + "\n" + "结束时间: " + license.getEndDate();
        if (verifyDate(license.getEndDate())){
            holder.cardView.setCardBackgroundColor(Color.GREEN);
        }else holder.cardView.setCardBackgroundColor(Color.RED);
        holder.licenseTxt.setText(str);


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
            Toast.makeText(mContext, "发生错误, 请联系我们!", Toast.LENGTH_LONG).show();
        }
        if (nowDate.getTime() > endTimeDate.getTime()){
            return false;
        }else return true;
    }

    @Override
    public int getItemCount() {
        return mlicenseList.size();
    }



    public interface OnRecyclerItemLongListener{
        void onItemLongClick(View view, int position);
    }
    public void setOnItemLongClickListener(OnRecyclerItemLongListener listener){
        this.mOnItemLong =  listener;
    }

    public interface OnRecyclerItemClickListener{
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnRecyclerItemClickListener listener){
        this.mOnItemClick =  listener;
    }

}
