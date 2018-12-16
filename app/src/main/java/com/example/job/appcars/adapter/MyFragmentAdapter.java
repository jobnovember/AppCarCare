package com.example.job.appcars.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.job.appcars.Booking;
import com.example.job.appcars.Login;
import com.example.job.appcars.Register;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0: return new Booking();
            case 1: return new Login();
            case 2: return new Register();
            default: return new Booking();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] tabs = {"ตารางคิว", "เข้าสู่ระบบ", "สมัครสมาชิก"};
        return tabs[position];
    }
}
