package com.erenlivingstone.rider.networking;

import com.erenlivingstone.rider.data.model.Stations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Eren on 05/12/2016.
 *
 * A Retrofit 2 client implementation to use for fetching
 * BikeShareToronto's JSON feed of stations
 */

public interface BikeShareTorontoAPI {
    String BASE_URL = "https://feeds.bikesharetoronto.com/";

    @GET("stations/stations.json")
    Call<Stations> loadStations();
}
