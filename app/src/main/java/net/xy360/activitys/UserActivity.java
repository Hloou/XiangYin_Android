package net.xy360.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import net.xy360.R;
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

public class UserActivity extends BaseActivity {

    private TextView tv_name, tv_description;
    private ManagementService managementService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_description = (TextView)findViewById(R.id.tv_description);
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

    private void setUI(UserInfo userInfo) {
        tv_name.setText(userInfo.nickname);
        tv_description.setText(userInfo.description == null ? getString(R.string.my_default_description) : userInfo.description);
    }

}
