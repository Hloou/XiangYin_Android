package net.xy360.activitys.yinpan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;

public class TrashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yin_pan_trash, menu);
        return true;
    }

    @Override
    public void initView() {

    }
}
