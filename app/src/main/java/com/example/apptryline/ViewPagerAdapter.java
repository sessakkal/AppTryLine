package com.example.apptryline;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Class<?>> activityClassList = new ArrayList<>();
    private final List<String> activityTitleList = new ArrayList<>();
    private final List<String> activityTagList = new ArrayList<>();
    private final FragmentManager fragmentManager;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentManager = fm;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        try {
            Class<?> activityClass = activityClassList.get(position);
            return (Fragment) activityClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getCount() {
        return activityClassList.size();
    }

    public void addActivity(Class<?> activityClass, String title) {
        activityClassList.add(activityClass);
        activityTitleList.add(title);
        activityTagList.add(null); // Tag will be null initially
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return activityTitleList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // Refresh all fragments when data set changes
        return POSITION_NONE;
    }

    public void refreshActivity(int position) {
        if (position < activityTagList.size() && activityTagList.get(position) != null) {
            Fragment fragment = fragmentManager.findFragmentByTag(activityTagList.get(position));
            if (fragment != null) {
                fragmentManager.beginTransaction().detach(fragment).attach(fragment).commit();
            }
        }
    }
}
