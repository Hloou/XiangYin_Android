package net.xy360.activitys.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.activitys.UserActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserInfo;
import net.xy360.commonutils.userdata.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_nickname, et_name, et_tel, et_sch, et_time, et_addr, et_sign;
    private TextView tv_mdftel;
    private String nickname, name, tel, sch, time, addr, sign;
    private ManagementService managementService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

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
                            UserData.save(UserInfoActivity.this, userInfo);
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
        et_nickname.setText(userInfo.nickname);
        et_tel.setText(userInfo.telephone);
        et_time.setText(userInfo.signupTime.toString());
        //et_nickname, et_name, et_tel, et_sch, et_time, et_addr, et_sign
        //tv_name.setText(userInfo.nickname);
        //tv_description.setText(userInfo.description == null ? getString(R.string.my_default_description) : userInfo.description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userinfo_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.userinfo_save) {
            nickname = et_nickname.getText().toString();
            JSONObject JSON = new JSONObject();
            try {
                JSON.put("nickname", nickname);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            UserInfo userInfo = UserData.load(this, UserInfo.class);
            //if (userInfo == null) {
                final UserId userId = UserData.load(this, UserId.class);
                if (userId == null) {
                    //do something error
                }
                if (managementService == null)
                    managementService = BaseRequest.retrofit.create(ManagementService.class);
                Map<String, String> map = new HashMap<>();
                map.put("modifiedFields", JSON.toString());

            Log.d("TAG", String.valueOf(userId.userId));
            Log.d("TAG", userId.token);
            Log.d("TAG", JSON.toString());

            managementService.updateUserInfo(userId.userId, userId.token, JSON.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<UserInfo, UserInfo>() {
                            @Override
                            public UserInfo call(UserInfo userInfo) {
                                UserData.save(UserInfoActivity.this, userInfo);
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

            //} else
                //setUI(userInfo);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_info_mdftel) {
            String nickname = et_nickname.getText().toString();
            String name = et_name.getText().toString();
            String tel = et_tel.getText().toString();
            String sch = et_sch.getText().toString();
            String time = et_time.getText().toString();
            String addr = et_addr.getText().toString();
            String sign = et_sign.getText().toString();

        }
    }

    public void initView() {
        et_nickname = (EditText) findViewById(R.id.user_info_nickname);
        et_name = (EditText) findViewById(R.id.user_info_name);
        et_tel = (EditText) findViewById(R.id.user_info_tel);
        et_sch = (EditText) findViewById(R.id.user_info_school);
        et_time = (EditText) findViewById(R.id.user_info_time);
        et_addr = (EditText) findViewById(R.id.user_info_myaddr);
        et_sign = (EditText) findViewById(R.id.user_info_sign);

        tv_mdftel = (TextView) findViewById(R.id.user_info_mdftel);
    }

}
