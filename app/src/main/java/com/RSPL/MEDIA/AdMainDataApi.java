package com.RSPL.MEDIA;

import Config.ConfigItems;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rspl-rajeev on 7/3/18.
 */

public interface AdMainDataApi {

    @FormUrlEncoded


    @POST("Media_Test.jsp")
        //@POST("/Android/download1.php")
    Call<ConfigItems> adMainData(@Field("STORE_ID") String STORE_ID, @Field("OTP") String OTP, @Query("tagged") String tags);

}
