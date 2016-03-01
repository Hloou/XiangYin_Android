package net.xy360.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import net.xy360.R;

/**
 * Created by jolin on 2016/3/1.
 */
public class WenKuPagerAdapter extends PagerAdapter {
    public Object instantiateItem(ViewGroup collection, int position) {
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.primaryContentView;
                break;
            case 1:
                resId = R.id.secondContentView;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

}
