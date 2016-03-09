package net.xy360.activitys.print;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.adapters.OrderPagerAdapter;

public class AllOrderActivity extends BaseActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    @Override
    public void initView() {
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(new OrderPagerAdapter(getSupportFragmentManager(), this));
        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
