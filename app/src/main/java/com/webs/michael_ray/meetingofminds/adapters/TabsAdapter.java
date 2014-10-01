package com.webs.michael_ray.meetingofminds.adapters;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.webs.michael_ray.meetingofminds.CategoryFragment;
import com.webs.michael_ray.meetingofminds.FavoritesFragment;
import com.webs.michael_ray.meetingofminds.NearFragment;

public class TabsAdapter extends FragmentPagerAdapter {
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private FragmentManager fm;
    private Location location;
    private int userID;

    public TabsAdapter(FragmentManager fm, Location location, int userID) {
        super(fm);
        this.location = location;
        this.userID = userID;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle data = new Bundle();
        data.putInt("current_page", position + 1);
        switch(position){
            case 0:
                NearFragment frag1 = new NearFragment(location);
                frag1.setArguments(data);
                return frag1;
            case 1:
                CategoryFragment frag2 = new CategoryFragment();
                frag2.setArguments(data);
                return frag2;
            case 2:
                FavoritesFragment frag3 = new FavoritesFragment(userID);
                frag3.setArguments(data);
                return frag3;
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public Fragment getActiveFragment(ViewPager container, int position) {
        String name = makeFragmentName(container.getId(), position);
        return  fm.findFragmentByTag(name);
    }

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }

    @Override
    public int getCount() {
        return 3;
    }
}