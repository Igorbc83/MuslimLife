package com.muslimlife.app.view.message;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import com.muslimlife.app.databinding.FragmentMessageBinding;

public class PagerAdapter extends FragmentStatePagerAdapter {


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();



    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title, FragmentMessageBinding binding, int messageCount, int notificationCount) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

        if(messageCount<1)
            binding.switcher.answerNotif.setVisibility(View.GONE);
        else{
            binding.switcher.answerNotif.setText(String.valueOf(messageCount));
            binding.switcher.answerNotif.setVisibility(View.VISIBLE);
        }

        if(notificationCount<1)
            binding.switcher.notificationNotif.setVisibility(View.GONE);
        else{
            binding.switcher.notificationNotif.setText(String.valueOf(notificationCount));
            binding.switcher.notificationNotif.setVisibility(View.VISIBLE);
        }


    }
}