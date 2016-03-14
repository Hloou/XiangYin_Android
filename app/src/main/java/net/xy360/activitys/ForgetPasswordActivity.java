package net.xy360.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.xy360.R;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.fragments.LoadingFragment;
import net.xy360.utils.Sha256Calculater;
import net.xy360.wxapi.WXEntryActivity;

import java.util.concurrent.ScheduledExecutorService;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiangbin on 2016/2/29.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_phone, et_check, et_password, et_password_again;
    private ManagementService managementService = null;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        if (managementService == null)
            managementService = BaseRequest.retrofit.create(ManagementService.class);
    }

    public void initView() {
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_check = (EditText)findViewById(R.id.et_check);
        et_password = (EditText)findViewById(R.id.et_password);
        et_password_again = (EditText)findViewById(R.id.et_password_again);
        findViewById(R.id.btn_get_check).setOnClickListener(this);
        findViewById(R.id.btn_find).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_get_check) {
            getCheck();
        } else if (id == R.id.btn_find) {
            findPwd();
        }
    }

    private void getCheck() {
        managementService.getSmsCode(et_phone.getText().toString(), "resetPwd")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(ForgetPasswordActivity.this, getString(R.string.password_sended), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(ForgetPasswordActivity.this, e);
                    }

                    @Override
                    public void onNext(String s) {
                    }
                });
    }

    private void findPwd() {
        if (!et_password.getText().toString().equals(et_password_again.getText().toString()))
            Toast.makeText(this, getString(R.string.password_password_not_same), Toast.LENGTH_SHORT).show();
        String pwdSha = Sha256Calculater.calSha(et_password.getText().toString());
        final LoadingFragment loadingFragment = LoadingFragment.showLoading(getSupportFragmentManager());
        Subscription s = managementService.resetPWD(et_phone.getText().toString(), et_check.getText().toString(), pwdSha)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        loadingFragment.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this, getString(R.string.password_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgetPasswordActivity.this, WXEntryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingFragment.dismiss();
                        BaseRequest.ErrorResponse(ForgetPasswordActivity.this, e);
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
        loadingFragment.setSubscription(s);
    }

}
