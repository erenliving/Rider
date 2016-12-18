package com.erenlivingstone.rider.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Eren-DSK on 03/12/2016.
 *
 * This class contains static utility methods the mobile app can use.
 */

public class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    //region Location Utils methods

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

    //endregion

    //region String Utils methods

    /**
     * Transforms the input String into one ready for display in the UI
     *
     * @param timestamp String with format yyyy-MM-dd HH:mm:ss
     * @return String with format MMM d, yyyy HH:mm, or the input String if error occurs
     */
    public static String getFormattedTimestampForDisplay(String timestamp) {
        String formattedTimestamp = timestamp;

        try {
            SimpleDateFormat dateTimeFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance();
            dateTimeFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date = dateTimeFormat.parse(timestamp);
            dateTimeFormat.applyPattern("MMM d, yyyy HH:mm");
            formattedTimestamp = dateTimeFormat.format(date);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e); // ClassCastException, ParseException
        }

        return formattedTimestamp;
    }

    /**
     * Transforms the input double into a String ready for display in the UI, with automatic
     * formatting to show kilometer distances
     *
     * @param distance the distance in meters
     * @return the String representation of the distance in meters or kilometers
     */
    public static String getFormattedDistanceForDisplay(double distance) {
        if (distance >= 1000) {
            return String.format(Locale.getDefault(), "%.2fkm away", distance/1000);
        } else {
            return String.format(Locale.getDefault(), "%.0fm away", distance);
        }
    }

    public static String getFormattedLocation(double latitude, double longitude) {
        return Double.toString(latitude) + "," + Double.toString(longitude);
    }

    //endregion

    //region GoogleMap Utils methods

    public static void fixZoomForLatLngs(GoogleMap googleMap, List<LatLng> latLngs) {
        if (latLngs != null && latLngs.size() > 0) {
            LatLngBounds.Builder bc = new LatLngBounds.Builder();

            for (LatLng latLng : latLngs) {
                bc.include(latLng);
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 100), 4000,
                    null);
        }
    }

    //endregion
}
