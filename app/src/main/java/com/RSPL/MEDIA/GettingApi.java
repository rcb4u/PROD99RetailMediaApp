package com.RSPL.MEDIA;

import Config.ConfigItems;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by shilpa on 18/7/16.
 */
public interface GettingApi {
    /* @GET("/Android/json.php")
 Call<ConfigItems> loadQuestions(@Query("tagged") String tags);*/
    @FormUrlEncoded
    @POST("FMCG_PROD_MEDIA_DOWNLOAD.jsp")
    // @POST("ProdMediaDownload.jsp")

    //@POST("/Android/download1.php")
    Call<ConfigItems> load(@Field("STORE_ID") String STORE_ID, @Field("OTP") String OTP, @Query("tagged") String tags);


}




