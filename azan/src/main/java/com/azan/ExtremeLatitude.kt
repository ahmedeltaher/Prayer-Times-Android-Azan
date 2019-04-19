package com.azan

/**
 *
 * At certain locations and times of year, some prayer times do not occur
 * or otherwise are impossible to precisely calculate using conventional
 * means. These methods generally apply to locations with High latitudes
 * (near or above 49 degrees) or locations of Extreme proportion (near or
 * above 66 degrees).
 *
 * Method Category Information:
 *
 *  *  Nearest Latitude (Aqrab Al-Bilaad): Calculate a prayer time
 * using a safe latitude value. The recommended latitude by
 * many schools of Fiqh is 48.5 degrees, but you can customize
 * this by setting the "Method.setNearestLat()" method.
 *
 *  *  Nearest Good Day (Aqrab Al-Ayyam): The library determines
 * the closest previous or next day that the Fajr and Ishaa
 * times occur and are both valid.
 *
 *  *  An [amount] of Night and Day: Unlike the above mentioned
 * methods, the multiple methods in this category have no proof
 * in traditional Shari'a (Fiqh) resources. These methods were
 * introduced by modern day Muslim scholars and scientists for
 * practical reasons only.
 *
 *  *  Minutes from Shurooq/Maghrib: Use an interval time to
 * calculate Fajr and Ishaa. This will set the values of Fajr
 * and Ishaa to the same as the computed Shurooq and Maghrib
 * respectively, then add or subtract the amount of minutes
 * found in the "Method.getFajrInv" and "Method.getIshaaInv"
 * methods.
 *
 *
 */
class ExtremeLatitude {
    companion object {
        /**
         * none. If unable to calculate, leave only the invalid prayer
         * time as 99:99.
         */
        val NONE_EX = ExtremeLatitude()

        /**
         * Nearest Latitude: Apply to all prayer times always.
         */
        val LAT_ALL = ExtremeLatitude()

        /**
         * Nearest Latitude: Apply to Fajr and Ishaa times always.
         */
        val LAT_ALWAYS = ExtremeLatitude()

        /**
         * Nearest Latitude: Apply to Fajr and Ishaa times but only if
         * the library has detected that the current
         * Fajr or Ishaa time is invalid.
         */
        val LAT_INVALID = ExtremeLatitude()

        /**
         * Nearest Good Day: Apply to all prayer times always.
         */
        val GOOD_ALL = ExtremeLatitude()

        /**
         * Nearest Good Day: Apply to Fajr and Ishaa times but only if
         * the library has detected that the current
         * Fajr or Ishaa time is invalid. This is the
         * default method. (Default)
         */
        val GOOD_INVALID = ExtremeLatitude()

        /**
         * 1/7th of Night: Apply to Fajr and Ishaa times always.
         */
        val SEVEN_NIGHT_ALWAYS = ExtremeLatitude()

        /**
         * 1/7th of Night: Apply to Fajr and Ishaa times but only if
         * the library has detected that the current
         * Fajr or Ishaa time is invalid.
         *
         */
        val SEVEN_NIGHT_INVALID = ExtremeLatitude()

        /**
         * 1/7th of Day: Apply to Fajr and Ishaa times always.
         */
        val SEVEN_DAY_ALWAYS = ExtremeLatitude()

        /**
         * 1/7th of Day: Apply to Fajr and Ishaa times but only if the
         * library has detected that the current Fajr
         * or Ishaa time is invalid.
         */
        val SEVEN_DAY_INVALID = ExtremeLatitude()

        /**
         * Half of the Night: Apply to Fajr and Ishaa times always.
         */
        val HALF_ALWAYS = ExtremeLatitude()

        /**
         * Half of the Night: Apply to Fajr and Ishaa times but only
         * if the library has detected that the
         * current Fajr or Ishaa time is
         * invalid.
         */
        val HALF_INVALID = ExtremeLatitude()

        /**
         * Minutes from Shorooq/Maghrib: Apply to Fajr and Ishaa times always.
         */
        val MIN_ALWAYS = ExtremeLatitude()

        /**
         * Minutes from Shorooq/Maghrib: Apply to Fajr and Ishaa times but only if
         * the library has detected that the
         * current Fajr or Ishaa time is invalid.
         *
         */
        val MIN_INVALID = ExtremeLatitude()

        /**
         * Nearest Good Day: Different good days for Fajr and Ishaa (Not
         * implemented)
         */
        val GOOD_DIF = ExtremeLatitude()

    }
}
