package net.xy360.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.SendAuth;

import net.xy360.R;
import net.xy360.activitys.ad.SignActivity;
import net.xy360.activitys.user.SettingsActivity;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.UserInfo;
import net.xy360.fragments.LoadingFragment;
import net.xy360.utils.Sha256Calculater;
import net.xy360.wxapi.WXEntryActivity;

import org.w3c.dom.Text;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiangbin on 2016/2/29.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener{

    private TextView user_agreement, own_account;
    private Button signup;
    private ManagementService managementService = null;
    private EditText et_phone, et_password, et_password_again, et_check;
    private CheckBox cb_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();

        if (managementService == null)
            managementService = BaseRequest.retrofit.create(ManagementService.class);
    }

    @Override
    public void initView() {
        user_agreement = (TextView) findViewById(R.id.signup_user_agreement);
        own_account = (TextView) findViewById(R.id.signup_account);
        signup = (Button) findViewById(R.id.signup_signup);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_password = (EditText)findViewById(R.id.et_password);
        et_password_again = (EditText)findViewById(R.id.et_password_again);
        et_check = (EditText)findViewById(R.id.et_check);
        cb_selected = (CheckBox)findViewById(R.id.cb_selected);
        findViewById(R.id.btn_get_check).setOnClickListener(this);
        user_agreement.setOnClickListener(this);
        own_account.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.signup_user_agreement) {

        } else if (id == R.id.signup_account) {
            Intent intent = new Intent(SignUpActivity.this, WXEntryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Log.d("tag", "zhanghu");
            startActivity(intent);
        } else if (id == R.id.signup_signup) {
            goSignUp();
        } else if (id == R.id.btn_get_check) {
            getCheck();
        }
    }

    public void getCheck() {
        managementService.getSmsCode(et_phone.getText().toString(), "signup")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(SignUpActivity.this, getString(R.string.signup_sended), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        BaseRequest.ErrorResponse(SignUpActivity.this, e);
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
    }

    public void goSignUp() {
        if (!et_password.getText().toString().equals(et_password_again.getText().toString())) {
            Toast.makeText(this, getString(R.string.signup_password_not_same), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cb_selected.isChecked()) {
            Toast.makeText(this, getString(R.string.signup_agree), Toast.LENGTH_SHORT).show();
            return;
        }
        String shapwd = Sha256Calculater.calSha(et_password.getText().toString());
        final LoadingFragment loadingFragment = LoadingFragment.showLoading(getSupportFragmentManager());
        Subscription s =managementService.signup(et_phone.getText().toString(), shapwd, et_check.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        loadingFragment.dismiss();
                        Toast.makeText(SignUpActivity.this, getString(R.string.signup_signup_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, WXEntryActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingFragment.dismiss();
                        BaseRequest.ErrorResponse(SignUpActivity.this, e);
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {

                    }
                });
        loadingFragment.setSubscription(s);
    }

}
