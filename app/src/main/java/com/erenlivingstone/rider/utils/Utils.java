package com.erenlivingstone.rider.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eren-DSK on 03/12/2016.
 *
 * This class contains static utility methods the mobile app can use.
 */

public class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    private static final String PREFERENCES_LATITUDE = "lat";
    private static final String PREFERENCES_LONGITUDE = "lng";

    /**
     * Check if the app has access to fine location permission. On pre-M
     * devices this will always return true.
     */
    public static boolean checkFineLocationPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Store the location in the app preferences.
     */
    public static void storeLocation(Context context, LatLng location) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(PREFERENCES_LATITUDE, Double.doubleToRawLongBits(location.latitude));
        editor.putLong(PREFERENCES_LONGITUDE, Double.doubleToRawLongBits(location.longitude));
        editor.apply();
    }

    /**
     * Fetch the location from app preferences.
     */
    public static LatLng getLocation(Context context) {
        if (!checkFineLocationPermission(context)) {
            return null;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Long lat = prefs.getLong(PREFERENCES_LATITUDE, Long.MAX_VALUE);
        Long lng = prefs.getLong(PREFERENCES_LONGITUDE, Long.MAX_VALUE);
        if (lat != Long.MAX_VALUE && lng != Long.MAX_VALUE) {
            Double latDbl = Double.longBitsToDouble(lat);
            Double lngDbl = Double.longBitsToDouble(lng);
            return new LatLng(latDbl, lngDbl);
        }
        return null;
    }
}
