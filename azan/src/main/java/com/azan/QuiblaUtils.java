package com.azan;

import static com.azan.Constants.KAABA_LATITUDE;
import static com.azan.Constants.KAABA_LONGITUDE;
import static com.azan.Constants.TOTAL_DEGREES;
import static com.azan.util.MathUtil.atan2Deg;
import static com.azan.util.MathUtil.cosDeg;
import static com.azan.util.MathUtil.sinDeg;
import static com.azan.util.MathUtil.tanDeg;

/**
 * Created by AhmedEltaher on 13/11/16.
 */

public class QuiblaUtils {
    /**
     * Return qibla direction in degrees from the north (clock-wise).
     *
     * @param latitude  latitude in degrees
     * @param longitude longitude in degrees
     * @return 0 means north, 90 means east, 270 means west, etc
     */
    public static double qibla(double latitude, double longitude) {
        double degrees = atan2Deg(sinDeg(KAABA_LONGITUDE - longitude),
            cosDeg(latitude) * tanDeg(KAABA_LATITUDE)
                - sinDeg(latitude) * cosDeg(KAABA_LONGITUDE - longitude));
        return degrees >= 0 ? degrees : degrees + TOTAL_DEGREES;
    }
}
