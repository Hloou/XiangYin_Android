package net.xy360.commonutils;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.internetrequest.interfaces.ManagementService;
import net.xy360.commonutils.models.University;

import java.util.List;

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

    @LargeTest
    public void test() {
        ManagementService managementService = BaseRequest.retrofit.create(ManagementService.class);
        managementService.getUniversitys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<University>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("retrofit", "done");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("retrofit", e.getMessage());
                    }

                    @Override
                    public void onNext(List<University> universities) {
                        Log.d("retrofit", ""+universities.size());
                    }
                });
        Log.d("retrofit test", "");
    }
}