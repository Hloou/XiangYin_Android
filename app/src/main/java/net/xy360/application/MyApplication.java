package net.xy360.application;

import android.app.Application;
<<<<<<< HEAD

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Administrator on 2016/3/6.
 */
public class MyApplication extends Application {
=======
import android.util.Log;

import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.Cart;
import net.xy360.commonutils.models.Copy;
import net.xy360.commonutils.realm.RealmHelper;

/**
 * Created by jolin on 2016/3/6.
 */
public class MyApplication extends Application{
>>>>>>> cl3

    @Override
    public void onCreate() {
        super.onCreate();
        //图片加载配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //图片显示大小恰好
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();


        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        //  config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();

        ImageLoader.getInstance().init(config.build());
        
        RealmHelper.realm = RealmHelper.getInstance(this);
        RealmHelper.realm.beginTransaction();
        RealmHelper.realm.clear(Cart.class);
        RealmHelper.realm.commitTransaction();
        //Copy copy = new Copy();
        //copy.setCopyId("123");
        //Log.d("ffff", BaseRequest.gson.toJson(copy));
    }
}
