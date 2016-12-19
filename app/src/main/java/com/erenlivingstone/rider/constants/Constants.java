package com.erenlivingstone.rider.constants;

/**
 * Created by Eren-DSK on 03/12/2016.
 */

public class Constants {

    private Constants() {}

    public static final int GOOGLE_API_CLIENT_TIMEOUT_S = 10; // 10 seconds
    public static final String GOOGLE_API_CLIENT_ERROR_MSG =
            "Failed to connect to GoogleApiClient (error code = %d)";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com.erenlivingstone.rider.services";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";

    public static final String DIRECTIONS_STATUS_OK = "OK";

    public static final int EMOJI_BICYCLE_UNICODE = 0x1F6B2;
    public static final int EMOJI_CHEQUERED_FLAG_UNICODE = 0x1F3C1;

}
