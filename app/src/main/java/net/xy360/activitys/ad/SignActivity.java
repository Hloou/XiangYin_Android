package net.xy360.activitys.ad;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.activitys.NavigationActivity;

/**
 * Created by Administrator on 2016/3/6.
 */
public class SignActivity extends BaseActivity implements View.OnClickListener {

    private TextView sign_tv_integral;
    private ImageView sign_img_close,btn_callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        sign_tv_integral = (TextView) findViewById(R.id.sign_tv_integral);
        sign_img_close = (ImageView)findViewById(R.id.sign_img_close);
        btn_callback = (ImageView)findViewById(R.id.btn_callback);

        //导航条背景
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.red_press));
        //设置导航条返回按钮
        btn_callback.setImageResource(R.mipmap.callback_white);

        //设置页面头布局
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            SignActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //定义一个LayoutParams
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int MarginTopDp = DpPxChange.dip2px(SignActivity.this, 20);
            layoutParams.setMargins(0, MarginTopDp, 0, 0);
            toolbar.setPadding(0, MarginTopDp, 0, 0);

        }
    }

    private void initListener() {
        sign_tv_integral.setOnClickListener(this);
        sign_img_close.setOnClickListener(this);
        btn_callback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.sign_tv_integral:
                intent.setClass(SignActivity.this, PrintMoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.sign_img_close:
                intent.setClass(SignActivity.this, NavigationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_callback:
                finish();
                break;
        }

    }



}
