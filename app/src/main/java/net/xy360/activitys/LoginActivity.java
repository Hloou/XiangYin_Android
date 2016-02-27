package net.xy360.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import net.xy360.R;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserLogin;
import net.xy360.commonutils.userdata.UserData;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_phone, et_password;
    private Button btn_login;
    private ManagementService managementService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_phone = (EditText)findViewById(R.id.et_phone);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        et_phone.setText("18818200005");
        et_password.setText("18818200005");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /*if(UserData.getUserId(this) != null) {
            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            login();
        }
    }

    public void login() {
        if (managementService == null)
            managementService = BaseRequest.retrofit.create(ManagementService.class);
        String phone = et_phone.getText().toString();
        String password = et_password.getText().toString();
        String passwordsha = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            byte[] hash = md.digest(password.getBytes());
            passwordsha = String.format("%064x", new BigInteger(1, hash));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        UserLogin userLogin = new UserLogin(phone, passwordsha);

        managementService.login(userLogin.toMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<UserId, UserId>() {
                    @Override
                    public UserId call(UserId userId) {
                        //UserData.saveUserId(LoginActivity.this, userId);
                        UserData.save(LoginActivity.this, userId);
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
                        Log.d("sha256 error", e.getMessage());
                        //et_phone.setText(e.getMessage());
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            if (response.response().errorBody() != null)
                                try {
                                    et_phone.setText(response.response().errorBody().string());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            else {
                                Gson gson = new Gson();
                                et_phone.setText(gson.toJson(response.response().errorBody()));
                            }

                        }
                    }

                    @Override
                    public void onNext(UserId userId) {
                        Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
    }

}
