package net.xy360.activitys.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

    public void initView() {

    }

}
