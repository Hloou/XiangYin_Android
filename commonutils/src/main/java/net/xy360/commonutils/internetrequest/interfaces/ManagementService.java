package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.University;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by jolin on 2016/2/24.
 */
public interface ManagementService {
    @GET("/universities")
    Observable<List<University>> getUniversitys();
}
