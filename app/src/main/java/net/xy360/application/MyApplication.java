package net.xy360.application;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import net.xy360.commonutils.imageloader.MyImageLoader;
import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.models.CopyCart;
import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.PrintingCart;
import net.xy360.commonutils.realm.RealmHelper;

/**
 * Created by jolin on 2016/3/6.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        MyImageLoader.init(this);
        
        RealmHelper.realm = RealmHelper.getInstance(this);

        //remove all data for testing
        RealmHelper.removeAllData();
    }
}
