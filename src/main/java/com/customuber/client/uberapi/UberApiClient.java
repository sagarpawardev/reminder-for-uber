package com.customuber.client.uberapi;

import com.customuber.client.uberapi.model.TimeEstimateList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UberApiClient {

    @GET("api/estimate/time")
    Call<TimeEstimateList> getEstimates(@Query("start_latitude") double lat, @Query("start_longitude") double lng);

}
