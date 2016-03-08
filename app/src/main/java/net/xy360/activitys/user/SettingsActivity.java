package net.xy360.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tencent.mm.sdk.openapi.SendAuth;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;

/**
 * Created by jiangbin on 2016/3/1.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener{

    private ImageView about;
    private LinearLayout settings_line_mdf_pw;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

    }

    public void initView() {
        about = (ImageView) findViewById(R.id.settings_about);
        settings_line_mdf_pw = (LinearLayout)findViewById(R.id.settings_line_mdf_pw);

        about.setOnClickListener(this);
        settings_line_mdf_pw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.settings_line_mdf_pw) {
            Intent intent = new Intent(SettingsActivity.this, ModifyPasswordActivity.class);
            startActivity(intent);
        }
        if (id == R.id.settings_about) {
            Intent intent = new Intent(SettingsActivity.this, ModifyPasswordActivity.class);
            startActivity(intent);
        }
    }
}
