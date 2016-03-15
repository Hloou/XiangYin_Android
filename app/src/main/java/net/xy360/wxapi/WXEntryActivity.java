package net.xy360.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import android.widget.ImageView;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.activitys.ForgetPasswordActivity;
import net.xy360.activitys.NavigationActivity;
import net.xy360.activitys.SignUpActivity;
import net.xy360.activitys.ad.WelcomeSignActivity;
import net.xy360.activitys.user.MyAddressActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserLogin;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.contants.wechat;
import net.xy360.fragments.LoadingFragment;
import net.xy360.utils.Sha256Calculater;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class WXEntryActivity extends BaseActivity implements View.OnClickListener, IWXAPIEventHandler{

    private EditText et_phone, et_password;
    private Button btn_login;
    private TextView forget, register;
    private ManagementService managementService = null;

    private IWXAPI api;
    private ImageView wechatLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        et_phone.setText("18818200005");
        et_password.setText("18818200005");

        if (managementService == null)
            managementService = BaseRequest.retrofit.create(ManagementService.class);

        api = WXAPIFactory.createWXAPI(this, wechat.APP_ID, false);
        api.handleIntent(getIntent(), this);

    }


    public void initView() {
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        wechatLogin = (ImageView) findViewById(R.id.wechat_login);
        forget = (TextView) findViewById(R.id.login_forget);
        register = (TextView) findViewById(R.id.login_register);

        wechatLogin.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        forget.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            login();
        }
        if (id == R.id.wechat_login) {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);
        }
        if (id == R.id.login_forget) {
            Intent intent = new Intent(WXEntryActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        }
        if (id == R.id.login_register) {
            Intent intent = new Intent(WXEntryActivity.this, SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void login() {

        final LoadingFragment loadingFragment = LoadingFragment.showLoading(getSupportFragmentManager());

        String phone = et_phone.getText().toString();
        String password = et_password.getText().toString();
        String passwordsha = Sha256Calculater.calSha(password);

        UserLogin userLogin = new UserLogin(phone, passwordsha);
        Subscription s = managementService.login(userLogin.toMap())
                .map(new Func1<UserId, UserId>() {
                    @Override
                    public UserId call(UserId userId) {
                        //UserData.saveUserId(WXEntryActivity.this, userId);
                        UserData.save(WXEntryActivity.this, userId);
                        return userId;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserId>() {
                    @Override
                    public void onCompleted() {
                        //Log.d("sha256", "done");
                        loadingFragment.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.d("sha256 error", e.getMessage());
                        BaseRequest.ErrorResponse(WXEntryActivity.this, e);
                        loadingFragment.dismiss();
                    }

                    @Override
                    public void onNext(UserId userId) {
                        //Log.d("ffff", "yeah");
                        Intent intent = new Intent(WXEntryActivity.this, WelcomeSignActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
        loadingFragment.setSubscription(s);
    }

    /**
     * wechat method begin
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    //  wechat send message to app
    @Override
    public void onReq(BaseReq req) {
        // TODO

    }

    // app send message to wechat
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.d("Tag", "ERR_OK");
                result = R.string.errcode_success;
                // get token
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                String token = sendResp.token;
                Log.d("Tag", "token\t" + token);

                /*managementService.loginWeChat(token)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Func1<UserId, UserId>() {
                            @Override
                            public UserId call(UserId userId) {
                                //UserData.saveUserId(WXEntryActivity.this, userId);
                                UserData.save(WXEntryActivity.this, userId);
                                return userId;
                            }
                        })
                        .subscribe(new Subscriber<UserId>() {
                            @Override
                            public void onCompleted() {
                                Log.d("sha256", "done");
                            }

                            @Override
                            public void onError(Throwable e) {
                                BaseRequest.ErrorResponse(WXEntryActivity.this, e);
                            }

                            @Override
                            public void onNext(UserId userId) {
                                Intent intent = new Intent(WXEntryActivity.this, NavigationActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });*/
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                Log.d("Tag", "errcode_cancel");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                Log.d("Tag", "ERR_AUTH_DENIED");
                break;
            default:
                result = R.string.errcode_unknown;
                Log.d("Tag", "errcode_unknown");
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
    /**
     * wechat method end
     */

}
