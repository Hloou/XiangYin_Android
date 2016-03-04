package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.Label;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jolin on 2016/3/2.
 */
public interface LabelService {

    @GET("users/{userid}/labels")
    Observable<List<Label>> getLabels(@Path("userid") String userid, @Query("token")String token);

    @GET("users/{userid}/labels/{labelid}/files")
    Observable<List<File>> getFilesViaLabels(@Path("userid")String userid, @Path("labelid")int labelid, @Query("token")String token);
}
