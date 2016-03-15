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
import net.xy360.commonutils.userdata.UserData;
import net.xy360.fragments.LoadingFragment;
import net.xy360.utils.Sha256Calculater;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiangbin on 2016/2/29.
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_origin, et_password, et_password_again;
    private ManagementService managementService = null;
    private UserId userId = null;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_mdf_password);
        initView();

        if (userId == null)
            userId = UserData.load(this, UserId.class);
        if (managementService == null)
            managementService = BaseRequest.retrofit.create(ManagementService.class);
    }
    public void initView() {
        et_origin = (EditText)findViewById(R.id.et_origin);
        et_password = (EditText)findViewById(R.id.et_password);
        et_password_again = (EditText)findViewById(R.id.et_password_again);
        findViewById(R.id.btn_change_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_change_password) {
            change_password();
        }
    }

    private void change_password() {
        if (!et_password.getText().toString().equals(et_password_again.getText().toString())) {
            Toast.makeText(this, getString(R.string.pw_mdf_password_not_same), Toast.LENGTH_SHORT).show();
            return;
        }

        String oldPassword = Sha256Calculater.calSha(et_origin.getText().toString());
        String newPassword = Sha256Calculater.calSha(et_password.getText().toString());

        final LoadingFragment loadingFragment = LoadingFragment.showLoading(getSupportFragmentManager());
        Subscription s = managementService.resetPWDviaPWD(userId.userId, userId.token, oldPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(ModifyPasswordActivity.this, getString(R.string.pw_mdf_success), Toast.LENGTH_SHORT).show();
                        loadingFragment.dismiss();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(ModifyPasswordActivity.this, e);
                        loadingFragment.dismiss();
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
        loadingFragment.setSubscription(s);
    }
}
