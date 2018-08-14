package com.RSPL.MEDIA;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rspl-rajeev on 29/3/18.
 */

public interface MediaApkVersion {
    @FormUrlEncoded

    @POST("OperationJSP/MEDIA_VERSION.jsp")
    Call<MediaApkVersionModel> loadMediaVersion(@Field("STORE_ID") String STORE_ID,
                                                @Field("STORE_NM") String STORE_NM,
                                                @Field("STORE_ADDRESS") String STORE_ADDRESS,
                                                @Field("APK_VERSION") String APK_VERSION,
                                                @Field("APK_UPGRADE") String APK_UPGRADE,
                                                @Query("tagged") String tags);

}