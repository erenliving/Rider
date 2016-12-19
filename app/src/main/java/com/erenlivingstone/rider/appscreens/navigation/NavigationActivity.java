package com.erenlivingstone.rider.appscreens.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.appscreens.CardActivity;
import com.erenlivingstone.rider.constants.Constants;
import com.erenlivingstone.rider.data.model.Directions;
import com.erenlivingstone.rider.networking.GoogleMapsDirectionsAPI;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NavigationActivity extends FragmentActivity implements OnMapReadyCallback,
        Callback<Directions> {

    public static final String TAG = NavigationActivity.class.getSimpleName();

    private static final String KEY_CAMERA_POSITION = "CAMERA_POSITION";
    private static final String KEY_ROUTE_POLYLINE = "ROUTE_POLYLINE";

    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private List<LatLng> mRoute;

    private LatLng origin, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        origin = intent.getParcelableExtra(CardActivity.EXTRA_ORIGIN);
        destination = intent.getParcelableExtra(CardActivity.EXTRA_DESTINATION);

        if (savedInstanceState != null) {
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            mRoute = savedInstanceState.getParcelableArrayList(KEY_ROUTE_POLYLINE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            // Convert List<LatLng> into ArrayList<LatLng> so it can be saved as Parcelable
            outState.putParcelableArrayList(KEY_ROUTE_POLYLINE, new ArrayList<>(mRoute));
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        float[] riderBlueHSV = new float[3];
        ColorUtils.colorToHSL(ContextCompat.getColor(this, R.color.colorAccent), riderBlueHSV);
        float[] riderGreenHSV = new float[3];
        ColorUtils.colorToHSL(ContextCompat.getColor(this, R.color.colorPrimary), riderGreenHSV);
        addMarkerToMap(origin, getString(R.string.start), riderBlueHSV[0]);
        addMarkerToMap(destination, getString(R.string.finish), riderGreenHSV[0]);

        if (Utils.checkFineLocationPermission(this)) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        // If the previous state was saved, set the map's camera position to the saved state and
        // redraw the polyline. Otherwise call the Google Maps Direction API to get route
        // information.
        if (mCameraPosition != null && mRoute != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
            addRouteToMap(mRoute);
        } else {
            // Build and queue a GET request to get Directions
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GoogleMapsDirectionsAPI.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GoogleMapsDirectionsAPI googleMapsDirectionsAPI = retrofit.create(GoogleMapsDirectionsAPI.class);
            Call<Directions> call = googleMapsDirectionsAPI.getDirections(
                    Utils.getFormattedLocation(origin.latitude, origin.longitude),
                    Utils.getFormattedLocation(destination.latitude, destination.longitude),
                    getString(R.string.google_maps_directions_key));
            // Asynchronous call to get data, calls back to this class
            call.enqueue(this);
        }
    }

    /**
     * Creates and adds a styled Marker at the given location to the map
     *
     * @param latLng the location of the Marker
     * @param title the title to display in info popup
     * @param markerHue the Bitmap hue color to style the Marker, from 0 - 360
     */
    private void addMarkerToMap(LatLng latLng, String title, float markerHue) {
        MarkerOptions startMarkerOptions = new MarkerOptions();
        startMarkerOptions.position(latLng);
        startMarkerOptions.title(title);
        startMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(markerHue));
        mMap.addMarker(startMarkerOptions);
    }

    /**
     * Creates a styled polyline path based on the list of locations to add to the map
     *
     * @param latLngs the list of locations to draw the polyline
     */
    public void addRouteToMap(List<LatLng> latLngs) {
        PolylineOptions options = new PolylineOptions();
        options.width(10);
        options.color(ContextCompat.getColor(this, R.color.colorAccent));
        for (LatLng latLng : latLngs) {
            options.add(latLng);
        }
        mMap.addPolyline(options);
    }

    //region Retrofit Callback methods

    /**
     * Retrofit Callback method when successful
     *
     * @param call the call associated with this callback
     * @param response the HTTP response
     */
    @Override
    public void onResponse(Call<Directions> call, Response<Directions> response) {
        if (Constants.DIRECTIONS_STATUS_OK.equals(response.body().status)) {
            // Get the first Route's polyline points
            String encodedPoints = response.body().routes.get(0).overview_polyline.points;
            mRoute = new ArrayList<LatLng>();
            mRoute = PolyUtil.decode(encodedPoints);
            addRouteToMap(mRoute);
            Utils.fixZoomForLatLngs(mMap, mRoute);
        }
    }

    /**
     * Retrofit Callback method when error occurred, logs an Error
     *
     * @param call the call associated with this callback
     * @param t an object containing info on the error
     */
    @Override
    public void onFailure(Call<Directions> call, Throwable t) {
        Log.e(TAG, t.getLocalizedMessage(), t);
    }

    //endregion

}
