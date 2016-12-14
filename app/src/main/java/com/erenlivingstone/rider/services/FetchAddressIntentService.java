package com.erenlivingstone.rider.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.appscreens.bikes.BikesFragment;
import com.erenlivingstone.rider.appscreens.bikes.BikesPresenter;
import com.erenlivingstone.rider.constants.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchAddressIntentService extends IntentService {

    public static final String TAG = FetchAddressIntentService.class.getSimpleName();

    private static final String ACTION_FETCH_ADDRESS = "com.erenlivingstone.rider.services.ACTION_FETCH_ADDRESS";

    private static final String EXTRA_LOCATION_DATA = "com.erenlivingstone.rider.services.EXTRA_LOCATION_DATA";

    private ResultReceiver mReceiver;

    public FetchAddressIntentService() {
        super(TAG);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionFetchAddress(Context context, BikesPresenter
            .AddressResultReceiver receiver, Location location) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.setAction(ACTION_FETCH_ADDRESS);
        intent.putExtra(Constants.RECEIVER, receiver);
        intent.putExtra(EXTRA_LOCATION_DATA, location);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (ACTION_FETCH_ADDRESS.equals(action)) {
            mReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            // Check if receiver was properly registered.
            if (mReceiver == null) {
                Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
                return;
            }

            // Get the location passed to this service through an extra.
            Location location = intent.getParcelableExtra(EXTRA_LOCATION_DATA);
            // Make sure that the location data was really sent over through an extra. If it wasn't,
            // send an error error message and return.
            if (location == null) {
                String errorMessage = getString(R.string.no_location_data_provided);
                Log.wtf(TAG, errorMessage);
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
                return;
            }

            fetchAddressInternal(location);
        }
    }

    /**
     * Called when an address lookup is requested
     */
    private void fetchAddressInternal(Location location) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();

        // It's OK to use blockingConnect() here as we are running in an
        // IntentService that executes work on a separate (background) thread.
        ConnectionResult connectionResult = googleApiClient.blockingConnect(
                Constants.GOOGLE_API_CLIENT_TIMEOUT_S, TimeUnit.SECONDS);

        if (connectionResult.isSuccess() && googleApiClient.isConnected()) {
            String errorMessage = "";

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        // Get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                errorMessage = getString(R.string.service_not_available);
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                errorMessage = getString(R.string.invalid_lat_long_used);
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() +
                        ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }

            // Handle case where no address was found.
            if (addresses == null || addresses.size()  == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = getString(R.string.no_address_found);
                    Log.e(TAG, errorMessage);
                }
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using getAddressLine,
                // join them, and send them to the thread.
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                Log.i(TAG, getString(R.string.address_found));
                deliverResultToReceiver(Constants.SUCCESS_RESULT,
                        TextUtils.join(System.getProperty("line.separator"),
                                addressFragments));
            }
        } else {
            Log.e(TAG, String.format(Constants.GOOGLE_API_CLIENT_ERROR_MSG,
                    connectionResult.getErrorCode()));
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}
