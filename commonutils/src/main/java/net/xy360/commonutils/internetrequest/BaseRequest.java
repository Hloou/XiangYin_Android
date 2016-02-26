package net.xy360.commonutils.internetrequest;

import java.security.KeyStore;
import java.security.KeyStoreException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jolin on 2016/2/24.
 */
public class BaseRequest {
    public final static String endPoint = "http://120.27.146.10:9000";
    public final static String endPointSecure = "https://120.27.146.10:9000";
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endPoint)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();


}
