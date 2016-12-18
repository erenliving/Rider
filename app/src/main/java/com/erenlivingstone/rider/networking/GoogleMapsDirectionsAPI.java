package com.erenlivingstone.rider.networking;

import com.erenlivingstone.rider.data.model.Directions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by eren on 2016-12-16.
 *
 * A Retrofit 2 client implementation to use for getting Directions between two locations
 */

public interface GoogleMapsDirectionsAPI {
    String BASE_URL = "https://maps.googleapis.com/maps/api/directions/";

    @GET("json?mode=walking")
    Call<Directions> getDirections(@Query("origin") String origin, @Query("destination") String
            destination, @Query("key") String key);
}
