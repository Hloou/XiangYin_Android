package net.xy360.activitys.ad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.activitys.NavigationActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/3/6.
 */
public class WelcomeSignActivity extends BaseActivity implements View.OnClickListener {

    String imgUrl = "http://pic.qiantucdn.com/58pic/12/12/75/21h58PICbDa.jpg";
    private ImageView welcome_sign_tv_ad;
    private TextView welcome_tv_sign;
    private LinearLayout welcome_sign_line_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_sign);
        initView();
        initListener();

    }


    @Override
    public void initView() {

        welcome_sign_tv_ad = (ImageView) findViewById(R.id.welcome_sign_tv_ad);
        welcome_sign_line_close = (LinearLayout) findViewById(R.id.welcome_sign_line_close);
        welcome_tv_sign = (TextView) findViewById(R.id.welcome_tv_sign);
        ImageLoader.getInstance().displayImage(imgUrl, welcome_sign_tv_ad);
    }

    private void initListener() {
        welcome_tv_sign.setOnClickListener(this);
        welcome_sign_line_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.welcome_tv_sign:
                intent.setClass(WelcomeSignActivity.this, SignActivity.class);
                startActivity(intent);
                break;
            case R.id.welcome_sign_line_close:
                intent.setClass(WelcomeSignActivity.this, NavigationActivity.class);
                startActivity(intent);
                break;
        }

    }


}
