package net.xy360.activitys.ad;

import android.content.Intent;
import android.content.res.ColorStateList;
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
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.AdvertisementService;
import net.xy360.commonutils.models.SignInInfo;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.utils.DpPxChange;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/3/6.
 */
public class SignActivity extends BaseActivity implements View.OnClickListener {

    private ImageView sign_img_close;
    private List<TextView> textViewList;
    private TextView tv_count, tv_income;
    private AdvertisementService advertisementService = null;
    private UserId userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initView();
        initListener();

        userId = UserData.load(this, UserId.class);

        if (advertisementService == null)
            advertisementService = BaseRequest.retrofit.create(AdvertisementService.class);

        requestData();
    }

    @Override
    public void initView() {
        sign_img_close = (ImageView)findViewById(R.id.sign_img_close);
        textViewList = new ArrayList<>();
        textViewList.add((TextView)findViewById(R.id.tv_count1));
        textViewList.add((TextView) findViewById(R.id.tv_count2));
        textViewList.add((TextView) findViewById(R.id.tv_count3));
        textViewList.add((TextView) findViewById(R.id.tv_count4));
        textViewList.add((TextView) findViewById(R.id.tv_count5));
        tv_count = (TextView)findViewById(R.id.tv_count);
        tv_income = (TextView)findViewById(R.id.tv_income);

        //导航条背景
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.red_press));

    }

    private void initListener() {
        sign_img_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.sign_img_close:
                intent.setClass(SignActivity.this, NavigationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }

    }

    private void requestData() {
        advertisementService.signIn(userId.userId, userId.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SignInInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(SignActivity.this, e);
                    }

                    @Override
                    public void onNext(SignInInfo signInInfo) {
                        tv_count.setText("" + signInInfo.consecutiveSignInDays);
                        tv_income.setText(String.format("%.2f", signInInfo.temporaryIncomeInCent / 100.0));
                        for (int i = 0; i < signInInfo.consecutiveSignInDays; i++) {
                            TextView t =textViewList.get(i);
                            t.setBackgroundResource(R.drawable.btn_yellow_circular);
                            t.setTextColor(0xff000000);
                        }
                    }
                });
    }



}
