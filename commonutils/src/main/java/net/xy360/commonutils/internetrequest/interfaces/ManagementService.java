package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.University;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserInfo;
import net.xy360.commonutils.models.UserLogin;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by jolin on 2016/2/24.
 */
public interface ManagementService {

    @POST("users/sessions")
    Observable<UserId> login(@QueryMap Map<String, String> map);

    @GET("users/{id}/private")
    Observable<UserInfo> getUserInfo(@Path("id") int id, @QueryMap Map<String, String> map);

    @GET("universities")
    Observable<List<University>> getUniversitys();
}
