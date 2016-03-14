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
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.AdvertisementService;
import net.xy360.commonutils.models.AdvertisementInfo;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/3/6.
 */
public class WelcomeSignActivity extends BaseActivity implements View.OnClickListener {

    private ImageView welcome_sign_tv_ad;
    private TextView welcome_tv_sign, tv_signed;
    private LinearLayout welcome_sign_line_close;
    private AdvertisementService advertisementService = null;
    private UserId userId = null;
    private AdvertisementInfo advertisementInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_sign);
        initView();
        initListener();

        if (userId == null)
            userId = UserData.load(this, UserId.class);

        if (advertisementService == null)
            advertisementService = BaseRequest.retrofit.create(AdvertisementService.class);

        requestData();

    }


    @Override
    public void initView() {

        welcome_sign_tv_ad = (ImageView) findViewById(R.id.welcome_sign_tv_ad);
        welcome_sign_line_close = (LinearLayout) findViewById(R.id.welcome_sign_line_close);
        welcome_tv_sign = (TextView) findViewById(R.id.welcome_tv_sign);
        tv_signed = (TextView)findViewById(R.id.tv_signed);
    }

    private void initListener() {
        welcome_tv_sign.setOnClickListener(this);
        welcome_sign_line_close.setOnClickListener(this);
        welcome_sign_tv_ad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.welcome_tv_sign:
                intent.setClass(WelcomeSignActivity.this, SignActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.welcome_sign_line_close:
                intent.setClass(WelcomeSignActivity.this, NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.welcome_sign_tv_ad:
                clickImage();
                break;
        }

    }

    private void requestData() {
        advertisementService.getHomeAd(userId.userId, userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AdvertisementInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(WelcomeSignActivity.this, e);
                    }

                    @Override
                    public void onNext(AdvertisementInfo advertisementInfo) {
                        WelcomeSignActivity.this.advertisementInfo = advertisementInfo;
                        ImageLoader.getInstance().displayImage(advertisementInfo.content, welcome_sign_tv_ad);
                        if (advertisementInfo.alreadySignedIn)
                            tv_signed.setVisibility(View.VISIBLE);
                        else
                            welcome_tv_sign.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void clickImage() {
        if (advertisementInfo == null)
            return;
        advertisementService.clickAd(userId.userId, advertisementInfo.advertisementId, userId.token, "click")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        String url = advertisementInfo.link;
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + advertisementInfo.link;
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }


}
