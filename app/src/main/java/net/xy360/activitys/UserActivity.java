package net.xy360.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.xy360.R;
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
    private ImageView settings, feedback, message, address, info;
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
        settings = (ImageView)findViewById(R.id.user_setting);
        feedback = (ImageView)findViewById(R.id.user_feedback);
        message = (ImageView)findViewById(R.id.user_mymsg);
        address = (ImageView)findViewById(R.id.user_address);
        info = (ImageView)findViewById(R.id.user_info);

        settings.setOnClickListener(this);
        feedback.setOnClickListener(this);
        message.setOnClickListener(this);
        address.setOnClickListener(this);
        info.setOnClickListener(this);
    }


    private void setUI(UserInfo userInfo) {
        tv_name.setText(userInfo.nickname);
        tv_description.setText(userInfo.description == null ? getString(R.string.my_default_description) : userInfo.description);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_setting) {
            Intent intent = new Intent(UserActivity.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (id == R.id.user_feedback) {
            Intent intent = new Intent(UserActivity.this, FeedbackActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (id == R.id.user_mymsg) {
            Intent intent = new Intent(UserActivity.this, MyMessageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (id == R.id.user_address) {
            Intent intent = new Intent(UserActivity.this, MyAddressActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if (id == R.id.user_info) {
            Intent intent = new Intent(UserActivity.this, UserInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
