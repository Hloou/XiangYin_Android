package net.xy360.activitys;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabWidget;

import net.xy360.R;
import net.xy360.activitys.wenku.WenKuActivity;
import net.xy360.activitys.yinpan.YinPanActivity;
import net.xy360.views.CustomTabViewExt;

public class NavigationActivity extends TabActivity {

    private TabHost mHost;
    private TabWidget mWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mHost = getTabHost();
        mWidget = mHost.getTabWidget();

        Intent indexIntent = new Intent(this, IndexActivity.class);
        CustomTabViewExt indexTab = new CustomTabViewExt(this, getString(R.string.tab_index), R.drawable.tab_ic_index);
        TabHost.TabSpec indexSpec = mHost.newTabSpec("tab1").setIndicator(indexTab).setContent(indexIntent);

        Intent yinPanIntent = new Intent(this, YinPanActivity.class);
        CustomTabViewExt yinPanTab = new CustomTabViewExt(this, getString(R.string.tab_yinpan), R.drawable.tab_ic_yinpan);
        TabHost.TabSpec yinPanSpec = mHost.newTabSpec("tab2").setIndicator(yinPanTab).setContent(yinPanIntent);

        Intent wenKuIntent = new Intent(this, WenKuActivity.class);
        CustomTabViewExt wenKuTab = new CustomTabViewExt(this, getString(R.string.tab_wenku), R.drawable.tab_ic_wenku);
        TabHost.TabSpec wenKuSpec = mHost.newTabSpec("tab3").setIndicator(wenKuTab).setContent(wenKuIntent);

        Intent userIntent = new Intent(this, UserActivity.class);
        CustomTabViewExt userTab = new CustomTabViewExt(this, getString(R.string.tab_user), R.drawable.tab_ic_user);
        TabHost.TabSpec userSpec = mHost.newTabSpec("tab4").setIndicator(userTab).setContent(userIntent);

        mHost.addTab(indexSpec);
        mHost.addTab(yinPanSpec);
        mHost.addTab(wenKuSpec);
        mHost.addTab(userSpec);

    }
}
