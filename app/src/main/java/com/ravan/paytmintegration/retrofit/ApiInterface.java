package com.ravan.paytmintegration.retrofit;

import com.ravan.paytmintegration.checksum_constant.Checksum;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    String BASE_URL = "http://192.168.0.105:9898/paytm_integration/";

    @FormUrlEncoded
    @POST("generateChecksum.php")
    Call<Checksum> getChecksum(
            @Field("MID") String mId,
            @Field("ORDER_ID") String orderId,
            @Field("CUST_ID") String custId,
            @Field("CHANNEL_ID") String channelId,
            @Field("TXN_AMOUNT") String txnAmount,
            @Field("WEBSITE") String website,
            @Field("CALLBACK_URL") String callbackUrl,
            @Field("INDUSTRY_TYPE_ID") String industryTypeId
    );
}
