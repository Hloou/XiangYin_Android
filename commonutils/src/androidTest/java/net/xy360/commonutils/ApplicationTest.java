package net.xy360.commonutils;

import android.app.Application;
import android.content.SharedPreferences;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.CopiesService;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.models.Order;
import net.xy360.commonutils.models.University;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserInfo;
import net.xy360.commonutils.models.UserLogin;
import net.xy360.commonutils.userdata.UserData;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() {
        Order order = new Order();
        Log.d("ffff", BaseRequest.gson.fromJson("\"OK\"", String.class));
    }


    public void test2() {
        ManagementService managementService = BaseRequest.retrofit.create(ManagementService.class);
        UserLogin userLogin = new UserLogin("18818200005", "f71c91dd2314aac58c83a5a227331877c0eeff16f8514a006fff7819b0b13c5b");
        managementService.login(userLogin.toMap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserId>() {
                    @Override
                    public void onCompleted() {
                        Log.e("sha256", "done");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("sha256 error", e.getMessage());
                        //et_phone.setText(e.getMessage());
                        if (e instanceof HttpException) {
                            HttpException response = (HttpException) e;
                            if (response.response().errorBody() != null)
                                try {
                                    Log.e("test2", response.response().errorBody().string());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            else {
                                Gson gson = new Gson();
                                Log.e("test2",gson.toJson(response.response().errorBody()));
                            }

                        }
                    }

                    @Override
                    public void onNext(UserId userId) {
                        Log.e("sha256 good", userId.token);
                    }
                });

    }

    @LargeTest
    public void test3() {
        CopiesService copiesService = BaseRequest.retrofit.create(CopiesService.class);
        copiesService.getCopies("", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Copy>>() {
                    @Override
                    public void onCompleted() {
                        Log.e("test3", "done");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("test3", "fail");
                    }

                    @Override
                    public void onNext(List<Copy> copies) {
                        Gson gson = new Gson();
                        Log.e("test3", gson.toJson(copies));
                    }
                });

    }
}