package net.xy360.activitys.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.activitys.UserActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserInfo;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.fragments.LoadingFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_nickname, et_name, et_sch, et_time, et_addr, et_sign;
    private TextView tv_mdftel, user_info_mdftel, tv_phone, tv_gender;
    private ManagementService managementService = null;
    private UserId userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        if (userId == null)
            userId = UserData.load(this, UserId.class);

        if (managementService == null)
            managementService = BaseRequest.retrofit.create(ManagementService.class);

        UserInfo userInfo = UserData.load(this, UserInfo.class);

        getUserInfo();
    }

    private void setUI(UserInfo userInfo) {
        et_nickname.setText(userInfo.nickname);
        tv_phone.setText(userInfo.telephone);
        et_time.setText(userInfo.entranceTime);
        et_name.setText(userInfo.realName);
        et_sch.setText(userInfo.universityName);
        et_addr.setText(userInfo.dormitory);
        if (userInfo.gender == 1)
            tv_gender.setText(getString(R.string.user_info_male));
        else
            tv_gender.setText(getString(R.string.user_info_female));
        et_sign.setText(userInfo.description);
    }

    private void getUserInfo() {
        managementService.getUserInfo(userId.userId, userId.token)
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
                        Log.d("error", e.toString());
                        BaseRequest.ErrorResponse(UserInfoActivity.this, e);
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        setUI(userInfo);
                    }
                });

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
            JSONObject json = new JSONObject();
            try {
                json.put("nickname", et_nickname.getText().toString());
                json.put("realName", et_name.getText().toString());
                json.put("universityName", et_sch.getText().toString());
                json.put("dormitory", et_addr.getText().toString());
                json.put("description", et_sign.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("fff", json.toString());

            final LoadingFragment loadingFragment = LoadingFragment.showLoading(getSupportFragmentManager());
            Subscription s = managementService.updateUserInfo(userId.userId, userId.token, json.toString())
                        .map(new Func1<UserInfo, UserInfo>() {
                                @Override
                                public UserInfo call(UserInfo userInfo) {
                                    UserData.save(UserInfoActivity.this, userInfo);
                                    return userInfo;
                                }
                            })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<UserInfo>() {
                            @Override
                            public void onCompleted() {
                                loadingFragment.dismiss();
                                Toast.makeText(UserInfoActivity.this, getString(R.string.user_info_save_success), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("ffff", e.toString());
                                loadingFragment.dismiss();
                                BaseRequest.ErrorResponse(UserInfoActivity.this, e);
                            }

                            @Override
                            public void onNext(UserInfo userInfo) {
                                setUI(userInfo);
                            }
                        });
            loadingFragment.setSubscription(s);

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.user_info_mdftel){
            Intent intent = new Intent();
            intent.setClass(UserInfoActivity.this, ModifyTelActivity.class);
            startActivity(intent);
        }
    }

    public void initView() {
        user_info_mdftel = (TextView)findViewById(R.id.user_info_mdftel);
        et_nickname = (EditText) findViewById(R.id.user_info_nickname);
        et_name = (EditText) findViewById(R.id.user_info_name);
        tv_phone = (TextView) findViewById(R.id.user_info_tel);
        et_sch = (EditText) findViewById(R.id.user_info_school);
        et_time = (EditText) findViewById(R.id.user_info_time);
        et_addr = (EditText) findViewById(R.id.user_info_myaddr);
        et_sign = (EditText) findViewById(R.id.user_info_sign);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_mdftel = (TextView) findViewById(R.id.user_info_mdftel);

        user_info_mdftel.setOnClickListener(this);
    }

}
