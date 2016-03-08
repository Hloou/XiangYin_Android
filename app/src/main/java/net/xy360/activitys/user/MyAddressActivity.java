package net.xy360.activitys.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;

public class MyAddressActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_adderess_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        initView();
    }


    @Override
    public void initView() {
        btn_adderess_add = (Button)findViewById(R.id.btn_adderess_add);
        btn_adderess_add.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_adderess_add:
                intent.setClass(MyAddressActivity.this, NewAddressActivity.class);
                startActivity(intent);
                break;
        }
    }
}
