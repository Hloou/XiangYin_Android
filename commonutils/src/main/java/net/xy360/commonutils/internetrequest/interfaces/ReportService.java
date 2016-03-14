package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.Report;

import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jolin on 2016/3/14.
 */
public interface ReportService {
    @POST("users/{userid}/reports")
    Observable<Report> sendReport(@Path("userid")String userid, @Query("token")String token,
                                  @Query("fields")String fields);
}
