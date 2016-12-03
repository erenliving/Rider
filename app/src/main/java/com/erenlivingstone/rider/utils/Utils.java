package com.erenlivingstone.rider.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Eren-DSK on 03/12/2016.
 *
 * This class contains static utility methods the mobile app can use.
 */

public class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    /**
     * Check if the app has access to fine location permission. On pre-M
     * devices this will always return true.
     */
    public static boolean checkFineLocationPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

}
