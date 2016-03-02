package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.File;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jolin on 2016/3/2.
 */
public interface FileService {
    @GET("users/{userid}/files")
    Observable<List<File>> getFiles(@Path("userid")int userid, @Query("token")String token);
}
