package com.erenlivingstone.rider.appscreens.home;

import android.util.Log;
import android.widget.Toast;

import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.networking.BikeShareTorontoAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eren-DSK on 08/12/2016.
 */

public class HomePresenter implements HomeContract.Presenter,
        Callback<Stations> {

    public static final String TAG = HomePresenter.class.getSimpleName();

    private final HomeContract.View mHomeView;

    public HomePresenter(HomeContract.View homeView) {
        mHomeView = homeView;
        mHomeView.setPresenter(this);
    }

    @Override
    public void start() {
        // TODO: complete this method if needed
    }

    @Override
    public void onSearchButtonPressed(SearchMode searchMode) {
        // Tell the View to communicate the selected SearchMode to the Activity
        mHomeView.onSearchModeSelected(searchMode);

        loadStations();
    }

    /**
     * This method tells the View to display a loading indicator while network activity is
     * occurring, and triggers an HTTP call to GET the latest Stations data from the API.
     */
    private void loadStations() {
        mHomeView.setLoadingIndicator(true);

        // Build and queue a GET request to get the latest data from the BikeShare API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BikeShareTorontoAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BikeShareTorontoAPI bikeShareTorontoAPI = retrofit.create(BikeShareTorontoAPI.class);
        Call<Stations> call = bikeShareTorontoAPI.loadStations();
        // Asynchronous call to get data, calls back to this class
        call.enqueue(this);
    }

    //region Retrofit Callback methods

    /**
     * Retrofit Callback method when successful, tells the View to stop the loading indicator and
     * to communicate that the Stations have loaded successfully to the Activity
     *
     * @param call the call associated with this callback
     * @param response the HTTP response
     */
    @Override
    public void onResponse(Call<Stations> call, Response<Stations> response) {
        mHomeView.setLoadingIndicator(false);

        mHomeView.onStationsLoaded(response.body());
    }

    /**
     * Retrofit Callback method when error occurred, tells the View to stop the loading indicator
     * and logs an Error
     *
     * @param call the call associated with this callback
     * @param t an object containing info on the error
     */
    @Override
    public void onFailure(Call<Stations> call, Throwable t) {
        Log.e(TAG, t.getLocalizedMessage(), t);
        mHomeView.setLoadingIndicator(false);
    }

    //endregion
}
