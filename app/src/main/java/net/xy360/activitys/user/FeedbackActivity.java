package net.xy360.activitys.user;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;

/**
 * Created by jiangbin on 2016/2/29.
 */
public class FeedbackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }


    public void initView() {
        //导航条背景
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.black));
    }
}
