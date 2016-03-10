package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.Order;
import net.xy360.commonutils.models.PaperBindingPrice;
import net.xy360.commonutils.models.Retailer;
import net.xy360.commonutils.models.RetailerPrice;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("users/{userId}/printing-orders")
    Observable<List<Order>> getPrintOrder(@Path("userId")String userId, @Query("token")String token,
                                    @Query("pageNumber")int i, @Query("status")String status);

    @PUT("users/{userId}/printing-orders/{printingId}")
    Observable<String> updatePrintOrder(@Path("userId")String userId, @Path("printingId")String printingId,
                                        @Query("token")String token, @Query("operation")String operation);

    @GET("retailers/{retailerId}/prices")
    Observable<RetailerPrice> getRetailerPrice(@Path("retailerId")String retailerId);
}
