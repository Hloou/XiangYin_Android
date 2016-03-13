package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.StorageInfo;
import net.xy360.commonutils.models.UserId;

import java.util.List;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jolin on 2016/3/2.
 */
public interface FileService {
    @GET("users/{userid}/files")
    Observable<List<File>> getFiles(@Path("userid")String userid, @Query("token")String token,
                                    @Query("isDeleted")boolean isDeleted);

    @PUT("users/{userid}/files/{inspaceid}")
    Observable<String> renameFile(@Path("userid")String userid, @Path("inspaceid")int inspaceid,
                                  @Query("token")String token, @Query("newName")String newName);

    @GET("users/{userid}/storage")
    Observable<StorageInfo> getStorage(@Path("userid")String userid, @Query("token")String token);

    @DELETE("users/{userid}/files/{inspaceid}")
    Observable<String> deleteFile(@Path("userid")String userid, @Path("inspaceid")String inspaceid,
                                  @Query("token")String token);

    @POST("users/{userid}/files/{inspaceid}")
    Observable<String> restoreFile(@Path("userid")String userid, @Path("inspaceid")String inspaceid, @Query("token")String token);

    @DELETE("users/{userid}/trash/{inspaceid}")
    Observable<String> removeFile(@Path("userid")String userid, @Path("inspaceid")String inspaceid, @Query("token")String token);

}
