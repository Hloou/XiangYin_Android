package net.xy360.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.xy360.R;
import net.xy360.fragments.AllOrderFragment;

/**
 * Created by jolin on 2016/3/9.
 */
public class OrderPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 1;
    private String tabTitles[];
    private Context context;

    public OrderPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[] {context.getString(R.string.all_order_all),
                                  context.getString(R.string.all_order_wait_paid),
                                  context.getString(R.string.all_order_wait_receive),
                                  context.getString(R.string.all_order_completed)};
    }

    @Override
    public Fragment getItem(int position) {
        return AllOrderFragment.newInstance();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
