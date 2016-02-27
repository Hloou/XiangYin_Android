package net.xy360.commonutils.userdata;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.UserId;

/**
 * Created by jolin on 2016/2/27.
 */
public class UserData {
    public final static String name = "userdata";

    public static void save(Context context, Object o) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(o.getClass().getName(), BaseRequest.gson.toJson(o));
        editor.commit();
    }

    public static <T extends Object>T load(Context context, Class<T> type) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        String s = sharedPreferences.getString(type.getName(), null);
        return BaseRequest.gson.fromJson(s, type);
    }


}
