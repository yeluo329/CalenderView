package com.example.caldenderview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private CalendarAdapter calendarAdapter;
    private TextView dateTv;
    private ImageView arrowLeftImg, arrowRightImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initData();
    }

    private void init() {
        viewPager2 = findViewById(R.id.vpContainer);
        //viewPager2的适配器
        calendarAdapter = new CalendarAdapter();
        viewPager2.setAdapter(calendarAdapter);
        dateTv = findViewById(R.id.date_tv);
        arrowLeftImg = findViewById(R.id.arrow_left);
        arrowRightImg = findViewById(R.id.arrow_right);
    }

    private void initData() {
        List<Calendar> data = new ArrayList<>();

        for (int i = 11; i >= 0; i--) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -i);
            data.add(calendar);
        }

        calendarAdapter.refreshData(data);
        viewPager2.setCurrentItem(11, false);
        dateTv.setText(data.get(data.size() - 1).get(Calendar.YEAR) + "-" + (data.get(data.size() - 1).get(Calendar.MONTH) + 1));

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int year = data.get(position).get(Calendar.YEAR);
                int month = data.get(position).get(Calendar.MONTH) + 1;
                dateTv.setText(year + "-" + month);
                RecyclerView recyclerView = (RecyclerView) viewPager2.getChildAt(0);
                View view = recyclerView.getLayoutManager().findViewByPosition(position);
                if (view != null)
                    updatePagerHeightForChild(view, viewPager2);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            public void updatePagerHeightForChild(View view, ViewPager2 pager) {
                view.post(() -> {
                    int weightMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
                    int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    view.measure(weightMeasureSpec, heightMeasureSpec);
                    if (pager.getLayoutParams().height != view.getMeasuredHeight()) {
                        ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
                        layoutParams.height = view.getMeasuredHeight();
                        pager.setLayoutParams(layoutParams);
                    }
                });

            }
        });


        arrowRightImg.setOnClickListener(V -> {
            arrowRightImg.post(() -> {
                if (viewPager2.getCurrentItem() != 11)
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1, false);
            });
        });
        arrowLeftImg.setOnClickListener(V -> {
            arrowLeftImg.post(() -> {
                if (viewPager2.getCurrentItem() != 0)
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1, false);
            });
        });


    }
}