package com.azan.astrologicalCalc

object Utils {
    val INVALID_TRIGGER = -.999

    val PI = 3.1415926535898

    val DEG_TO_10_BASE = 1 / 15.0

    val CENTER_OF_SUN_ANGLE = -0.833370

    val ALTITUDE_REFRACTION = 0.0347

    val REF_LIMIT = 9999999.0

    val KAABA_LAT = 21.423333

    val KAABA_LONG = 39.823333

    val DEF_NEAREST_LATITUDE = 48.5

    val DEF_IMSAAK_ANGLE = 1.5

    val DEF_IMSAAK_INTERVAL = 10.0

    val DEFAULT_ROUND_SEC = 30.0

    val AGGRESSIVE_ROUND_SEC = 1.0

    /* UTILITIES */
    fun DEG_TO_RAD(A: Double): Double {
        return A * (PI / 180.0)
    }

    fun RAD_TO_DEG(A: Double): Double {
        return A / (PI / 180.0)
    }
}
