package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.Retailer;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.HTTP;
import rx.Observable;

/**
 * Created by jolin on 2016/3/4.
 */
public interface OrderService {
    @GET("retailers")
    Observable<List<Retailer>> getRetailers();
}
