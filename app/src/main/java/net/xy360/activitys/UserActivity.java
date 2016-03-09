package net.xy360.activitys;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.activitys.ad.DpPxChange;
import net.xy360.activitys.ad.PrintMoneyActivity;
import net.xy360.activitys.print.AllOrderActivity;
import net.xy360.activitys.user.FeedbackActivity;
import net.xy360.activitys.user.MyAddressActivity;
import net.xy360.activitys.user.MyMessageActivity;
import net.xy360.activitys.user.SettingsActivity;
import net.xy360.activitys.user.UserInfoActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserInfo;
import net.xy360.commonutils.userdata.UserData;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_name, tv_description;
    private LinearLayout user_line_integral,user_line_mymsg,user_line_info,user_line_address,user_line_feedback,user_line_setting;
    private ManagementService managementService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();

        UserInfo userInfo = UserData.load(this, UserInfo.class);
        if (userInfo == null) {
            final UserId userId = UserData.load(this, UserId.class);
            if (userId == null) {
                //do something error
            }
            if (managementService == null)
                managementService = BaseRequest.retrofit.create(ManagementService.class);
            Map<String, String> map = new HashMap<>();
            map.put("token", userId.token);
            managementService.getUserInfo(userId.userId, map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<UserInfo, UserInfo>() {
                        @Override
                        public UserInfo call(UserInfo userInfo) {
                            UserData.save(UserActivity.this, userInfo);
                            return userInfo;
                        }
                    })
                    .subscribe(new Subscriber<UserInfo>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(UserInfo userInfo) {
                            setUI(userInfo);
                        }
                    });

        } else
            setUI(userInfo);
    }

    public void initView() {
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_description = (TextView)findViewById(R.id.tv_description);

        user_line_integral= (LinearLayout)findViewById(R.id.user_line_integral);
        user_line_mymsg= (LinearLayout)findViewById(R.id.user_line_mymsg);
        user_line_info= (LinearLayout)findViewById(R.id.user_line_info);
        user_line_address= (LinearLayout)findViewById(R.id.user_line_address);
        user_line_feedback= (LinearLayout)findViewById(R.id.user_line_feedback);
        user_line_setting = (LinearLayout)findViewById(R.id.user_line_setting);

        findViewById(R.id.ll_my_order).setOnClickListener(this);

        user_line_integral.setOnClickListener(this);
        user_line_mymsg.setOnClickListener(this);
        user_line_info.setOnClickListener(this);
        user_line_address.setOnClickListener(this);
        user_line_feedback.setOnClickListener(this);
        user_line_setting.setOnClickListener(this);


        //导航条背景
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        //设置导航条标题颜色
        toolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.black));


        //设置页面头布局
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            UserActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }


    private void setUI(UserInfo userInfo) {
        tv_name.setText(userInfo.nickname);
        tv_description.setText(userInfo.description == null ? getString(R.string.my_default_description) : userInfo.description);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_my_order) {
            Intent intent = new Intent(this, AllOrderActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_line_setting) {
            Intent intent = new Intent(UserActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_line_feedback) {
            Intent intent = new Intent(UserActivity.this, FeedbackActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_line_mymsg) {
            Intent intent = new Intent(UserActivity.this, MyMessageActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_line_address) {
            Intent intent = new Intent(UserActivity.this, MyAddressActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_line_info) {
            Intent intent = new Intent(UserActivity.this, UserInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.user_line_integral) {
            Intent intent = new Intent(UserActivity.this, PrintMoneyActivity.class);
            startActivity(intent);
        }
    }

}
