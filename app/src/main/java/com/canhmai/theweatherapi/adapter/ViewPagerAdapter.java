package com.canhmai.theweatherapi.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.canhmai.theweatherapi.api.response.curentweather.CurrenWeatherResponse;


import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final CurrenWeatherResponse responseData;
    private List<Fragment> listFragmentName;

    public List<Fragment> getListFragmentName() {
        return listFragmentName;
    }


    public void setListFragmentName(List<Fragment> listFragmentName) {
        this.listFragmentName = listFragmentName;
    }

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> listFragmentName, CurrenWeatherResponse responseData) {
        super(fragmentActivity);
        this.listFragmentName = listFragmentName;
        this.responseData = responseData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//        switch (position) {
//            case 0:
//                WeatherFragment weatherFragment = new WeatherFragment();
//                weatherFragment.setData(responseData);
//
//                return weatherFragment;
//            case 1:
//
//        }
//        return new WeatherFragment();
        return null;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {

        return 2;
    }
}
