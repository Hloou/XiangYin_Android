package net.xy360.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.xy360.R;
import net.xy360.fragments.AllOrderFragment;
import net.xy360.fragments.CompletedOrderFragment;
import net.xy360.fragments.UnPaidOrderFragment;
import net.xy360.fragments.UnReceiveOrderFragment;

/**
 * Created by jolin on 2016/3/9.
 */
public class OrderPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
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
        if (position == 0)
            return AllOrderFragment.newInstance();
        else if (position == 1)
            return UnPaidOrderFragment.newInstance();
        else if (position == 2)
            return UnReceiveOrderFragment.newInstance();
        else
            return CompletedOrderFragment.newInstance();
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
