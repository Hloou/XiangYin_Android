package net.xy360.application;

import android.app.Application;
import android.util.Log;

import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.realm.RealmHelper;

/**
 * Created by jolin on 2016/3/6.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        RealmHelper.realm = RealmHelper.getInstance(this);
        RealmHelper.realm.beginTransaction();
        RealmHelper.realm.clear(Cart.class);
        RealmHelper.realm.commitTransaction();
        //Copy copy = new Copy();
        //copy.setCopyId("123");
        //Log.d("ffff", BaseRequest.gson.toJson(copy));
    }
}
