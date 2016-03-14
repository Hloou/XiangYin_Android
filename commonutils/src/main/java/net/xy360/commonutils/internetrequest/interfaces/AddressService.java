package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.Address;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jolin on 2016/3/14.
 */
public interface AddressService {
    @GET("users/{userid}/addresses")
    Observable<List<Address>> getAddress(@Path("userid")String userid, @Query("token")String token);

    @POST("users/{userid}/addresses")
    Observable<Address> addAddress(@Path("userid")String userid, @Query("token")String token, @Query("fields")String fields);
}
