package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.AdvertisementInfo;
import net.xy360.commonutils.models.SignInInfo;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jolin on 2016/3/14.
 */
public interface AdvertisementService {

    @GET("users/{userid}/home-advertisement")
    Observable<AdvertisementInfo> getHomeAd(@Path("userid")String userid, @Query("token")String token);

    @POST("users/{userid}/advertisements/{advertisementid}/actions")
    Observable<String> clickAd(@Path("userid")String userid, @Path("advertisementid")String advertisementid,
                               @Query("token")String token, @Query("action")String action);

    @POST("users/{userid}/attendances")
    Observable<SignInInfo> signIn(@Path("userid")String userid, @Query("token")String token);
}
