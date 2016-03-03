package net.xy360.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tencent.mm.sdk.openapi.SendAuth;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;

/**
 * Created by jiangbin on 2016/3/1.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mdf_pw;
    private ImageView about;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

    }

    public void initView() {
        mdf_pw = (ImageView) findViewById(R.id.settings_mdf_pw);
        about = (ImageView) findViewById(R.id.settings_about);

        mdf_pw.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.settings_mdf_pw) {
            Intent intent = new Intent(SettingsActivity.this, ModifyPasswordActivity.class);
            startActivity(intent);
        }
        if (id == R.id.settings_about) {
            Intent intent = new Intent(SettingsActivity.this, ModifyPasswordActivity.class);
            startActivity(intent);
        }
    }
}
