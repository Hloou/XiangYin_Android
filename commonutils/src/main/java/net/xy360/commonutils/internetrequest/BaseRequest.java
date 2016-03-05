package net.xy360.commonutils.internetrequest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.xy360.commonutils.R;
import net.xy360.commonutils.models.*;
import net.xy360.commonutils.models.Error;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jolin on 2016/2/24.
 */
public class BaseRequest {
    public final static String endPoint = "http://120.27.146.10:9000/";
    public final static String endPointSecure = "https://120.27.146.10:9000/";

    public static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();


    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(endPoint)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    // true for solve the error, false for not solve
    public static boolean ErrorResponse(Context context, Throwable e) {
        String s = null;
        if (e instanceof HttpException) {
            HttpException response = (HttpException) e;
            if (response.response().errorBody() != null) {
                try {
                    s = response.response().errorBody().string();
                } catch (IOException e1) {
                    //e1.printStackTrace();
                }
                net.xy360.commonutils.models.Error error = gson.fromJson(s, Error.class);
                if (error.code >= 100 && error.code < 200) {
                    Toast.makeText(context, context.getString(R.string.error_server_fail), Toast.LENGTH_SHORT).show();
                    return true;
                }
                switch (error.code) {
                    case 210:

                    case 220:

                    case 230:
                        Toast.makeText(context, context.getString(R.string.error_password_error), Toast.LENGTH_SHORT).show();
                        return true;
                    case 240:
                    case 250:
                    case 260:
                        Toast.makeText(context, context.getString(R.string.error_telephone_not_found_error), Toast.LENGTH_SHORT).show();
                        return true;
                    default:

                }
            }
        }
        Toast.makeText(context, context.getString(R.string.error_connection_fail), Toast.LENGTH_SHORT).show();
        //Toast.makeText(context, response.response().errorBody().string(), Toast.LENGTH_SHORT).show();
        return false;
    }

}
