package net.xy360.commonutils.imageloader;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by jolin on 2016/3/8.
 */
public class MyImageLoader {

    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            //图片显示大小恰好
            .imageScaleType(ImageScaleType.EXACTLY)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public static void init(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
        //  .diskCacheSize(50 * 1024 * 1024) // 50 MiB
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .writeDebugLogs()
                    .build();

        ImageLoader.getInstance().init(config);
    }
}
