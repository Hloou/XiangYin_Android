package net.xy360.activitys.user;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.xy360.R;
import net.xy360.activitys.BaseActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserInfo;
import net.xy360.commonutils.userdata.UserData;
import net.xy360.fragments.LoadingFragment;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiangbin on 2016/2/29.
 */
public class ModifyTelActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_phone, et_new_phone, et_check;
    private ManagementService managementService = null;
    private UserId userId = null;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_mdf_tel);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

        if (userId == null)
            userId = UserData.load(this, UserId.class);

        if (managementService == null)
            managementService = BaseRequest.retrofit.create(ManagementService.class);
    }
    public void initView() {
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_new_phone = (EditText)findViewById(R.id.et_new_phone);
        et_check = (EditText)findViewById(R.id.et_check);
        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.btn_get_check).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_submit) {
            submit();
        } else if (id == R.id.btn_get_check) {
            getCheck();
        }

    }

    private void submit() {
        final LoadingFragment loadingFragment = LoadingFragment.showLoading(getSupportFragmentManager());
        Subscription s = managementService.resetPhoneviaSMS(userId.userId, userId.token, et_check.getText().toString(), et_new_phone.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(ModifyTelActivity.this, getString(R.string.mdf_tel_success), Toast.LENGTH_SHORT).show();
                        loadingFragment.dismiss();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(ModifyTelActivity.this, e);
                        loadingFragment.dismiss();
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {

                    }
                });
        loadingFragment.setSubscription(s);
    }

    private void getCheck() {
        //no smscode now
    }
}
