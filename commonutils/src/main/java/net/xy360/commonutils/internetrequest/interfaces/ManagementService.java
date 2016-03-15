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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by jolin on 2016/2/24.
 */
public interface ManagementService {

    @POST("users/sessions")
    Observable<UserId> login(@QueryMap Map<String, String> map);

    @POST("users/sessions/wechat")
    Observable<UserId> loginWeChat(@Query("code") String code);

    @GET("users/{id}/private")
    Observable<UserInfo> getUserInfo(@Path("id") String id, @Query("token") String token);

    @PUT("users/{id}")
    Observable<UserInfo> updateUserInfo(@Path("id") String id, @Query("token") String token, @Query("modifiedFields") String json);

    @GET("universities")
    Observable<List<University>> getUniversitys();

    @POST("users/sms-codes")
    Observable<String> getSmsCode(@Query("telephone")String telephone, @Query("usage")String usage);

    @PUT("user-telephones/{telephone}/password")
    Observable<String> resetPWD(@Path("telephone")String telephone, @Query("smsCode")String smsCode, @Query("newPassword")String newPassword);

    @POST("users")
    Observable<UserInfo> signup(@Query("telephone")String telephone, @Query("password")String password, @Query("smsCode")String code);

    @PUT("users/{userid}/password")
    Observable<String> resetPWDviaPWD(@Path("userid")String userid, @Query("token")String token,
                                      @Query("oldPassword")String oldPassword, @Query("newPassword")String newPassword);

    @PUT("users/{userid}/telephone")
    Observable<UserInfo> resetPhoneviaSMS(@Path("userid")String userid, @Query("token")String token,
                                          @Query("smsCode")String smsCode, @Query("newTelephone")String newTelephone);
}
