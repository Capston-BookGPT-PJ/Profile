package com.example.meltingbooks;

import android.os.Bundle;


public class CalendarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // 하단 메뉴
        setupBottomNavigation();

        //fragment_container
        findViewById(R.id.add_reading_record).setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new AddReadingRecordFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }


    //bottom Navigation의 위치 설정
    @Override
    protected int getCurrentNavItemId() {
        return R.id.Calendar;
    }
}