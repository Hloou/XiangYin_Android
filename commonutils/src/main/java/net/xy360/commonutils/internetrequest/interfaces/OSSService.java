package net.xy360.commonutils.internetrequest.interfaces;

import net.xy360.commonutils.models.File;
import net.xy360.commonutils.models.OSSCredential;
import net.xy360.commonutils.models.OSSFile;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by jolin on 2016/3/12.
 */
public interface OSSService {
    @GET("paths")
    Observable<OSSFile> getFilePathViaHash(@Query("hash")String hash);

    @GET("files/{fileid}/paths")
    Observable<OSSFile> getFilePathViaId(@Path("fileid")String fileid);

    @GET("users/{userid}/oss/credentials")
    Observable<OSSCredential> getOSSCredential(@Path("userid")String userid, @Query("token")String token);

    @POST("users/{userid}/files")
    Observable<File> uploadFile(@Path("userid")String userid, @Query("token")String token, @Query("hash")String hash,
                                @Query("fileName")String fileName, @Query("fileType")String fileType);
}
