package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.Copy;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by jolin on 2016/2/28.
 */
public interface CopiesService {
    @GET("copies")
    Observable<List<Copy>> getCopies(@Query("query") String query, @Query("pageNumber") int pageNumber);
}
