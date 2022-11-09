package com.example.caldenderview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarView extends RecyclerView.ViewHolder {
    private GridView gridView;
    private int year = 0;
    private int month = 0;
    private CalendarDateAdapter calendarDateAdapter;
    private Context context;


    public CalendarView(@NonNull View itemView) {
        super(itemView);
        gridView = itemView.findViewById(R.id.wgvCalendar);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        context = itemView.getContext();
        initEvent();
    }

    private void initEvent() {
        gridView.getSelectedItem();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                for (int i = 0; i < adapterView.getCount(); i++) {
                    View v = adapterView.getChildAt(i);
                    if (i == position) {
                        view.setBackgroundColor(Color.parseColor("#F3F3F3"));
                    } else {
                        v.setBackground(null);
                    }
                }
            }
        });
    }

    public void initData(Calendar calendar) {
        ArrayList<DateBean> data = new ArrayList<>();
        //获取第一天是星期几然后计算出需要填充的空白数据
        for (int i = 0; i < getMonthOneDayWeek(calendar); i++) {
            //填充空白的
            data.add(new DateBean(0, 0, 0));
        }

        //填充数据
        for (int i = 0; i < getMonthMaxData(calendar); i++) {
            data.add(new DateBean(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, i + 1));
        }
        calendarDateAdapter = new CalendarDateAdapter(context, data);
        gridView.setAdapter(calendarDateAdapter);
        setGridViewHeight(gridView, data.size());

    }

    //获取第一天为星期几
    private int getMonthOneDayWeek(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    //获取当月有几天
    private int getMonthMaxData(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static void setGridViewHeight(GridView gridView, int size) {
        // 获取listview的adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int col = 7;
        int totalHeight = 0;
        int hang;
        if (size % col == 0) {
            hang = size / col;
        } else {
            hang = size / col + 1;
        }
        View listItem = listAdapter.getView(8, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight() * hang;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }


}
