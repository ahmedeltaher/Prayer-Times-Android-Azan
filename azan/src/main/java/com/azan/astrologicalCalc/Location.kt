package com.azan.astrologicalCalc

class Location {

    /**
     *
     * @param degreeLong Longitude in decimal degree.
     */
    var degreeLong: Double = 0.toDouble()

    /**
     *
     * @param degreeLat Latitude in decimal degree.
     */
    var degreeLat: Double = 0.toDouble()

    /**
     *
     * @param gmtDiff  GMT difference at **regular time**.
     */
    var gmtDiff: Double = 0.toDouble()

    /**
     * Daylight savings time switch (0 if not used). Set
     * this to 1 should add 1 hour to all the calculated
     * prayer times
     * @param dst
     */
    var dst: Int = 0

    /**
     *
     * @param seaLevel Height above Sea level in meters
     */
    var seaLevel: Double = 0.toDouble()

    /**
     * @param pressure Atmospheric pressure in millibars (the
     * astronomical standard value is 1010 (`Location.DEFAULT_PRESSURE`))
     */
    var pressure: Double = 0.toDouble()

    /**
     *
     * @param temperature Temperature in Celsius degree (the
     * astronomical standard value is 10)
     */
    var temperature: Double = 0.toDouble()

    /**
     * default constructor of location object. Latitude, Longitude,
     * GMT difference and day saving time flag are required. Other
     * settings (sea level, pressure, temperature) are given standard
     * astronomical values and can be set later using setters.
     *
     *
     * @param degreeLat latitude in degrees
     * @param degreeLong longitude in degrees
     * @param gmtDiff difference with GMT
     * @param dst day saving time (1 to add one hour, 2 to add two, 0
     * if none, etc..)
     */
    constructor(degreeLat: Double, degreeLong: Double, gmtDiff: Double, dst: Int) {
        this.degreeLong = degreeLong
        this.degreeLat = degreeLat
        this.gmtDiff = gmtDiff
        this.dst = dst

        this.seaLevel = DEFAULT_SEA_LEVEL
        this.pressure = DEFAULT_PRESSURE
        this.temperature = DEFAULT_TEMPERATURE
    }

    private constructor() {

    }

    fun copy(): Location {
        val loc = Location()

        // copy all fields
        loc.degreeLat = degreeLat
        loc.degreeLong = degreeLong
        loc.gmtDiff = gmtDiff
        loc.dst = dst
        loc.seaLevel = seaLevel
        loc.pressure = pressure
        loc.temperature = temperature

        return loc
    }

    companion object {

        internal val DEFAULT_SEA_LEVEL = 0.0

        internal val DEFAULT_PRESSURE = 1010.0

        internal val DEFAULT_TEMPERATURE = 10.0
    }
}
