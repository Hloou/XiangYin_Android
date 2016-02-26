package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.internetrequest.BaseRequest;
import net.xy360.commonutils.models.University;
import net.xy360.commonutils.models.UserId;
import net.xy360.commonutils.models.UserLogin;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by jolin on 2016/2/24.
 */
public interface ManagementService {
    @Headers("Content-Type: application/json")
    @POST("/users/sessions")
    Observable<UserId> login(@Body UserLogin userLogin);


    @GET("/universities")
    Observable<List<University>> getUniversitys();
}
