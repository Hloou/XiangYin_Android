package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.Retailer;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jolin on 2016/3/4.
 */
public interface OrderService {
    @GET("retailers")
    Observable<List<Retailer>> getRetailers();

    @POST("users/{userId}/printing-orders")
    Observable<String> addPrintOrder(@Path("userId")String userId, @Query("token")String token, @Query("printingOrderFields")String printOrderFields,
                                     @Query("copyItemsFields")String copyItemsFields, @Query("printingItemsFields")String printingItemsFields);
}
