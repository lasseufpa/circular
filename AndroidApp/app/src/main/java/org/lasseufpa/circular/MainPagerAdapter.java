package org.lasseufpa.circular;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by alberto on 14/08/2017.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Fragment.instantiate(context, CircularMapFragment.class.getName());
                break;
            case 1:
                fragment = Fragment.instantiate(context, CircularListFragment.class.getName());
                break;
            case 2:
                fragment = Fragment.instantiate(context, FeedbackFragment.class.getName());
                break;
            case 3:
                fragment = Fragment.instantiate(context, FeedbackFragment.class.getName());
                break;

        }


        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
