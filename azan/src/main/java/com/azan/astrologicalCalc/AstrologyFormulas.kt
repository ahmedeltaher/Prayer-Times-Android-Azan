package com.azan.astrologicalCalc

/**
 * Astro functions
 *
 * Most of the astronomical values and formulas used in this file are
 * based on the Jean Meeus subset of the VSOP87 planetary theory.
 *
 */
object AstrologyFormulas {
    internal val L0 = arrayOf(doubleArrayOf(175347046.0, 0.0, 0.0), doubleArrayOf(3341656.0, 4.6692568, 6283.07585), doubleArrayOf(34894.0, 4.6261, 12566.1517), doubleArrayOf(3497.0, 2.7441, 5753.3849), doubleArrayOf(3418.0, 2.8289, 3.5231), doubleArrayOf(3136.0, 3.6277, 77713.7715), doubleArrayOf(2676.0, 4.4181, 7860.4194), doubleArrayOf(2343.0, 6.1352, 3930.2097), doubleArrayOf(1324.0, 0.7425, 11506.7698), doubleArrayOf(1273.0, 2.0371, 529.691), doubleArrayOf(1199.0, 1.1096, 1577.3435), doubleArrayOf(990.0, 5.233, 5884.927), doubleArrayOf(902.0, 2.045, 26.298), doubleArrayOf(857.0, 3.508, 398.149), doubleArrayOf(780.0, 1.179, 5223.694), doubleArrayOf(753.0, 2.533, 5507.553), doubleArrayOf(505.0, 4.583, 18849.228), doubleArrayOf(492.0, 4.205, 775.523), doubleArrayOf(357.0, 2.92, 0.067), doubleArrayOf(317.0, 5.849, 11790.629), doubleArrayOf(284.0, 1.899, 796.298), doubleArrayOf(271.0, 0.315, 10977.079), doubleArrayOf(243.0, 0.345, 5486.778), doubleArrayOf(206.0, 4.806, 2544.314), doubleArrayOf(205.0, 1.869, 5573.143), doubleArrayOf(202.0, 2.4458, 6069.777), doubleArrayOf(156.0, 0.833, 213.299), doubleArrayOf(132.0, 3.411, 2942.463), doubleArrayOf(126.0, 1.083, 20.775), doubleArrayOf(115.0, 0.645, 0.98), doubleArrayOf(103.0, 0.636, 4694.003), doubleArrayOf(102.0, 0.976, 15720.839), doubleArrayOf(102.0, 4.267, 7.114), doubleArrayOf(99.0, 6.21, 2146.17), doubleArrayOf(98.0, 0.68, 155.42), doubleArrayOf(86.0, 5.98, 161000.69), doubleArrayOf(85.0, 1.3, 6275.96), doubleArrayOf(85.0, 3.67, 71430.7), doubleArrayOf(80.0, 1.81, 17260.15), doubleArrayOf(79.0, 3.04, 12036.46), doubleArrayOf(71.0, 1.76, 5088.63), doubleArrayOf(74.0, 3.5, 3154.69), doubleArrayOf(74.0, 4.68, 801.82), doubleArrayOf(70.0, 0.83, 9437.76), doubleArrayOf(62.0, 3.98, 8827.39), doubleArrayOf(61.0, 1.82, 7084.9), doubleArrayOf(57.0, 2.78, 6286.6), doubleArrayOf(56.0, 4.39, 14143.5), doubleArrayOf(56.0, 3.47, 6279.55), doubleArrayOf(52.0, 0.19, 12139.55), doubleArrayOf(52.0, 1.33, 1748.02), doubleArrayOf(51.0, 0.28, 5856.48), doubleArrayOf(49.0, 0.49, 1194.45), doubleArrayOf(41.0, 5.37, 8429.24), doubleArrayOf(41.0, 2.4, 19651.05), doubleArrayOf(39.0, 6.17, 10447.39), doubleArrayOf(37.0, 6.04, 10213.29), doubleArrayOf(37.0, 2.57, 1059.38), doubleArrayOf(36.0, 1.71, 2352.87), doubleArrayOf(36.0, 1.78, 6812.77), doubleArrayOf(33.0, 0.59, 17789.85), doubleArrayOf(30.0, 0.44, 83996.85), doubleArrayOf(30.0, 2.74, 1349.87), doubleArrayOf(25.0, 3.16, 4690.48))

    internal val L1 = arrayOf(doubleArrayOf(628331966747.0, 0.0, 0.0), doubleArrayOf(206059.0, 2.678235, 6283.07585), doubleArrayOf(4303.0, 2.6351, 12566.1517), doubleArrayOf(425.0, 1.59, 3.523), doubleArrayOf(119.0, 5.796, 26.298), doubleArrayOf(109.0, 2.966, 1577.344), doubleArrayOf(93.0, 2.59, 18849.23), doubleArrayOf(72.0, 1.14, 529.69), doubleArrayOf(68.0, 1.87, 398.15), doubleArrayOf(67.0, 4.41, 5507.55), doubleArrayOf(59.0, 2.89, 5223.69), doubleArrayOf(56.0, 2.17, 155.42), doubleArrayOf(45.0, 0.4, 796.3), doubleArrayOf(36.0, 0.47, 775.52), doubleArrayOf(29.0, 2.65, 7.11), doubleArrayOf(21.0, 5.34, 0.98), doubleArrayOf(19.0, 1.85, 5486.78), doubleArrayOf(19.0, 4.97, 213.3), doubleArrayOf(17.0, 2.99, 6275.96), doubleArrayOf(16.0, 0.03, 2544.31), doubleArrayOf(16.0, 1.43, 2146.17), doubleArrayOf(15.0, 1.21, 10977.08), doubleArrayOf(12.0, 2.83, 1748.02), doubleArrayOf(12.0, 3.26, 5088.63), doubleArrayOf(12.0, 5.27, 1194.45), doubleArrayOf(12.0, 2.08, 4694.0), doubleArrayOf(11.0, 0.77, 553.57), doubleArrayOf(10.0, 1.3, 3286.6), doubleArrayOf(10.0, 4.24, 1349.87), doubleArrayOf(9.0, 2.7, 242.73), doubleArrayOf(9.0, 5.64, 951.72), doubleArrayOf(8.0, 5.3, 2352.87), doubleArrayOf(6.0, 2.65, 9437.76), doubleArrayOf(6.0, 4.67, 4690.48))

    internal val L2 = arrayOf(doubleArrayOf(52919.0, 0.0, 0.0), doubleArrayOf(8720.0, 1.0721, 6283.0758), doubleArrayOf(309.0, 0.867, 12566.152), doubleArrayOf(27.0, 0.05, 3.52), doubleArrayOf(16.0, 5.19, 26.3), doubleArrayOf(16.0, 3.68, 155.42), doubleArrayOf(10.0, 0.76, 18849.23), doubleArrayOf(9.0, 2.06, 77713.77), doubleArrayOf(7.0, 0.83, 775.52), doubleArrayOf(5.0, 4.66, 1577.34), doubleArrayOf(4.0, 1.03, 7.11), doubleArrayOf(4.0, 3.44, 5573.14), doubleArrayOf(3.0, 5.14, 796.3), doubleArrayOf(3.0, 6.05, 5507.55), doubleArrayOf(3.0, 1.19, 242.73), doubleArrayOf(3.0, 6.12, 529.69), doubleArrayOf(3.0, 0.31, 398.15), doubleArrayOf(3.0, 2.28, 553.57), doubleArrayOf(2.0, 4.38, 5223.69), doubleArrayOf(2.0, 3.75, 0.98))

    internal val L3 = arrayOf(doubleArrayOf(289.0, 5.844, 6283.076), doubleArrayOf(35.0, 0.0, 0.0), doubleArrayOf(17.0, 5.49, 12566.15), doubleArrayOf(3.0, 5.2, 155.42), doubleArrayOf(1.0, 4.72, 3.52), doubleArrayOf(1.0, 5.3, 18849.23), doubleArrayOf(1.0, 5.97, 242.73))

    internal val L4 = arrayOf(doubleArrayOf(114.0, 3.142, 0.0), doubleArrayOf(8.0, 4.13, 6283.08), doubleArrayOf(1.0, 3.84, 12566.15))

    internal val L5 = arrayOf(doubleArrayOf(1.0, 3.14, 0.0))

    internal val B0 = arrayOf(

            doubleArrayOf(280.0, 3.199, 84334.662), doubleArrayOf(102.0, 5.422, 5507.553), doubleArrayOf(80.0, 3.88, 5223.69), doubleArrayOf(44.0, 3.7, 2352.87), doubleArrayOf(32.0, 4.0, 1577.34))

    internal val B1 = arrayOf(

            doubleArrayOf(9.0, 3.9, 5507.55), doubleArrayOf(6.0, 1.73, 5223.69))

    internal val R0 = arrayOf(doubleArrayOf(100013989.0, 0.0, 0.0), doubleArrayOf(1670700.0, 3.0984635, 6283.07585), doubleArrayOf(13956.0, 3.05525, 12566.1517), doubleArrayOf(3084.0, 5.1985, 77713.7715), doubleArrayOf(1628.0, 1.1739, 5753.3849), doubleArrayOf(1576.0, 2.8469, 7860.4194), doubleArrayOf(925.0, 5.453, 11506.77), doubleArrayOf(542.0, 4.564, 3930.21), doubleArrayOf(472.0, 3.661, 5884.927), doubleArrayOf(346.0, 0.964, 5507.553), doubleArrayOf(329.0, 5.9, 5223.694), doubleArrayOf(307.0, 0.299, 5573.143), doubleArrayOf(243.0, 4.273, 11790.629), doubleArrayOf(212.0, 5.847, 1577.344), doubleArrayOf(186.0, 5.022, 10977.079), doubleArrayOf(175.0, 3.012, 18849.228), doubleArrayOf(110.0, 5.055, 5486.778), doubleArrayOf(98.0, 0.89, 6069.78), doubleArrayOf(86.0, 5.69, 15720.84), doubleArrayOf(86.0, 1.27, 161000.69), doubleArrayOf(85.0, 0.27, 17260.15), doubleArrayOf(63.0, 0.92, 529.69), doubleArrayOf(57.0, 2.01, 83996.85), doubleArrayOf(56.0, 5.24, 71430.7), doubleArrayOf(49.0, 3.25, 2544.31), doubleArrayOf(47.0, 2.58, 775.52), doubleArrayOf(45.0, 5.54, 9437.76), doubleArrayOf(43.0, 6.01, 6275.96), doubleArrayOf(39.0, 5.36, 4694.0), doubleArrayOf(38.0, 2.39, 8827.39), doubleArrayOf(37.0, 0.83, 19651.05), doubleArrayOf(37.0, 4.9, 12139.55), doubleArrayOf(36.0, 1.67, 12036.46), doubleArrayOf(35.0, 1.84, 2942.46), doubleArrayOf(33.0, 0.24, 7084.9), doubleArrayOf(32.0, 0.18, 5088.63), doubleArrayOf(32.0, 1.78, 398.15), doubleArrayOf(28.0, 1.21, 6286.6), doubleArrayOf(28.0, 1.9, 6279.55), doubleArrayOf(26.0, 4.59, 10447.39))

    internal val R1 = arrayOf(

            doubleArrayOf(103019.0, 1.10749, 6283.07585), doubleArrayOf(1721.0, 1.0644, 12566.1517), doubleArrayOf(702.0, 3.142, 0.0), doubleArrayOf(32.0, 1.02, 18849.23), doubleArrayOf(31.0, 2.84, 5507.55), doubleArrayOf(25.0, 1.32, 5223.69), doubleArrayOf(18.0, 1.42, 1577.34), doubleArrayOf(10.0, 5.91, 10977.08), doubleArrayOf(9.0, 1.42, 6275.96), doubleArrayOf(9.0, 0.27, 5486.78))

    internal val R2 = arrayOf(

            doubleArrayOf(4359.0, 5.7846, 6283.0758), doubleArrayOf(124.0, 5.579, 12566.152), doubleArrayOf(12.0, 3.14, 0.0), doubleArrayOf(9.0, 3.63, 77713.77), doubleArrayOf(6.0, 1.87, 5573.14), doubleArrayOf(3.0, 5.47, 18849.0))

    internal val R3 = arrayOf(doubleArrayOf(145.0, 4.273, 6283.076), doubleArrayOf(7.0, 3.92, 12566.15))

    internal val R4 = doubleArrayOf(4.0, 2.56, 6283.08)

    internal val PE = arrayOf(doubleArrayOf(-171996.0, -174.2, 92025.0, 8.9), doubleArrayOf(-13187.0, -1.6, 5736.0, -3.1), doubleArrayOf(-2274.0, -0.2, 977.0, -0.5), doubleArrayOf(2062.0, 0.2, -895.0, 0.5), doubleArrayOf(1426.0, -3.4, 54.0, -0.1), doubleArrayOf(712.0, 0.1, -7.0, 0.0), doubleArrayOf(-517.0, 1.2, 224.0, -0.6), doubleArrayOf(-386.0, -0.4, 200.0, 0.0), doubleArrayOf(-301.0, 0.0, 129.0, -0.1), doubleArrayOf(217.0, -0.5, -95.0, 0.3), doubleArrayOf(-158.0, 0.0, 0.0, 0.0), doubleArrayOf(129.0, 0.1, -70.0, 0.0), doubleArrayOf(123.0, 0.0, -53.0, 0.0), doubleArrayOf(63.0, 0.0, 0.0, 0.0), doubleArrayOf(63.0, 0.1, -33.0, 0.0), doubleArrayOf(-59.0, 0.0, 26.0, 0.0), doubleArrayOf(-58.0, -0.1, 32.0, 0.0), doubleArrayOf(-51.0, 0.0, 27.0, 0.0), doubleArrayOf(48.0, 0.0, 0.0, 0.0), doubleArrayOf(46.0, 0.0, -24.0, 0.0), doubleArrayOf(-38.0, 0.0, 16.0, 0.0), doubleArrayOf(-31.0, 0.0, 13.0, 0.0), doubleArrayOf(29.0, 0.0, 0.0, 0.0), doubleArrayOf(29.0, 0.0, -12.0, 0.0), doubleArrayOf(26.0, 0.0, 0.0, 0.0), doubleArrayOf(-22.0, 0.0, 0.0, 0.0), doubleArrayOf(21.0, 0.0, -10.0, 0.0), doubleArrayOf(17.0, -0.1, 0.0, 0.0), doubleArrayOf(16.0, 0.0, -8.0, 0.0), doubleArrayOf(-16.0, 0.1, 7.0, 0.0), doubleArrayOf(-15.0, 0.0, 9.0, 0.0), doubleArrayOf(-13.0, 0.0, 7.0, 0.0), doubleArrayOf(-12.0, 0.0, 6.0, 0.0), doubleArrayOf(11.0, 0.0, 0.0, 0.0), doubleArrayOf(-10.0, 0.0, 5.0, 0.0), doubleArrayOf(-8.0, 0.0, 3.0, 0.0), doubleArrayOf(7.0, 0.0, -3.0, 0.0), doubleArrayOf(-7.0, 0.0, 0.0, 0.0), doubleArrayOf(-7.0, 0.0, 3.0, 0.0), doubleArrayOf(-7.0, 0.0, 3.0, 0.0), doubleArrayOf(6.0, 0.0, 0.0, 0.0), doubleArrayOf(6.0, 0.0, -3.0, 0.0), doubleArrayOf(6.0, 0.0, -3.0, 0.0), doubleArrayOf(-6.0, 0.0, 3.0, 0.0), doubleArrayOf(-6.0, 0.0, 3.0, 0.0), doubleArrayOf(5.0, 0.0, 0.0, 0.0), doubleArrayOf(-5.0, 0.0, 3.0, 0.0), doubleArrayOf(-5.0, 0.0, 3.0, 0.0), doubleArrayOf(-5.0, 0.0, 3.0, 0.0), doubleArrayOf(4.0, 0.0, 0.0, 0.0), doubleArrayOf(4.0, 0.0, 0.0, 0.0), doubleArrayOf(4.0, 0.0, 0.0, 0.0), doubleArrayOf(-4.0, 0.0, 0.0, 0.0), doubleArrayOf(-4.0, 0.0, 0.0, 0.0), doubleArrayOf(-4.0, 0.0, 0.0, 0.0), doubleArrayOf(3.0, 0.0, 0.0, 0.0), doubleArrayOf(-3.0, 0.0, 0.0, 0.0), doubleArrayOf(-3.0, 0.0, 0.0, 0.0), doubleArrayOf(-3.0, 0.0, 0.0, 0.0), doubleArrayOf(-3.0, 0.0, 0.0, 0.0), doubleArrayOf(-3.0, 0.0, 0.0, 0.0), doubleArrayOf(-3.0, 0.0, 0.0, 0.0), doubleArrayOf(-3.0, 0.0, 0.0, 0.0))

    internal val SINCOEFF = arrayOf(intArrayOf(0, 0, 0, 0, 1), intArrayOf(-2, 0, 0, 2, 2), intArrayOf(0, 0, 0, 2, 2), intArrayOf(0, 0, 0, 0, 2), intArrayOf(0, 1, 0, 0, 0), intArrayOf(0, 0, 1, 0, 0), intArrayOf(-2, 1, 0, 2, 2), intArrayOf(0, 0, 0, 2, 1), intArrayOf(0, 0, 1, 2, 2), intArrayOf(-2, -1, 0, 2, 2), intArrayOf(-2, 0, 1, 0, 0), intArrayOf(-2, 0, 0, 2, 1), intArrayOf(0, 0, -1, 2, 2), intArrayOf(2, 0, 0, 0, 0), intArrayOf(0, 0, 1, 0, 1), intArrayOf(2, 0, -1, 2, 2), intArrayOf(0, 0, -1, 0, 1), intArrayOf(0, 0, 1, 2, 1), intArrayOf(-2, 0, 2, 0, 0), intArrayOf(0, 0, -2, 2, 1), intArrayOf(2, 0, 0, 2, 2), intArrayOf(0, 0, 2, 2, 2), intArrayOf(0, 0, 2, 0, 0), intArrayOf(-2, 0, 1, 2, 2), intArrayOf(0, 0, 0, 2, 0), intArrayOf(-2, 0, 0, 2, 0), intArrayOf(0, 0, -1, 2, 1), intArrayOf(0, 2, 0, 0, 0), intArrayOf(2, 0, -1, 0, 1), intArrayOf(-2, 2, 0, 2, 2), intArrayOf(0, 1, 0, 0, 1), intArrayOf(-2, 0, 1, 0, 1), intArrayOf(0, -1, 0, 0, 1), intArrayOf(0, 0, 2, -2, 0), intArrayOf(2, 0, -1, 2, 1), intArrayOf(2, 0, 1, 2, 2), intArrayOf(0, 1, 0, 2, 2), intArrayOf(-2, 1, 1, 0, 0), intArrayOf(0, -1, 0, 2, 2), intArrayOf(2, 0, 0, 2, 1), intArrayOf(2, 0, 1, 0, 0), intArrayOf(-2, 0, 2, 2, 2), intArrayOf(-2, 0, 1, 2, 1), intArrayOf(2, 0, -2, 0, 1), intArrayOf(2, 0, 0, 0, 1), intArrayOf(0, -1, 1, 0, 0), intArrayOf(-2, -1, 0, 2, 1), intArrayOf(-2, 0, 0, 0, 1), intArrayOf(0, 0, 2, 2, 1), intArrayOf(-2, 0, 2, 0, 1), intArrayOf(-2, 1, 0, 2, 1), intArrayOf(0, 0, 1, -2, 0), intArrayOf(-1, 0, 1, 0, 0), intArrayOf(-2, 1, 0, 0, 0), intArrayOf(1, 0, 0, 0, 0), intArrayOf(0, 0, 1, 2, 0), intArrayOf(0, 0, -2, 2, 2), intArrayOf(-1, -1, 1, 0, 0), intArrayOf(0, 1, 1, 0, 0), intArrayOf(0, -1, 1, 2, 2), intArrayOf(2, -1, -1, 2, 2), intArrayOf(0, 0, 3, 2, 2), intArrayOf(2, -1, 0, 2, 2))

    fun getRefraction(loc: Location, sunAlt: Double): Double {
        val part1: Double
        val part2: Double

        part1 = loc.pressure / 1010.0 * (283 / (273 + loc.temperature))
        part2 = 1.02 / (Utils.RAD_TO_DEG(Math.tan(Utils.DEG_TO_RAD(sunAlt + 10.3 / (sunAlt + 5.11)))) + 0.0019279)
        return part1 * part2 / 60.0
    }

    fun getJulianDay(date: SimpleDate, gmt: Double): Double {

        var jdB = 0.0
        var jdY: Double
        var jdM: Double
        val JD: Double

        jdY = date.year.toDouble()
        jdM = date.month.toDouble()

        if (date.month <= 2) {
            jdY--
            jdM += 12.0
        }

        if (date.year < 1)
            jdY++

        if (date.year > 1582 || date.year == 1582 && (date.month > 10 || date
                        .month == 10 && date.day >= 4))
            jdB = 2 - Math.floor(jdY / 100.0) + Math.floor(jdY / 100.0 / 4.0)

        JD = (Math.floor(365.25 * (jdY + 4716.0))
                + Math.floor(30.6001 * (jdM + 1)) + (date.day + -gmt / 24.0)
                + jdB) - 1524.5

        return JD
    }

    fun getAstroValuesByDay(julianDay: Double,
                            loc: Location, astro: Astro, topAstro: Astro) {
        val ad = AstrologyDay()

        if (astro.jd == julianDay - 1) {
            astro.ra[0] = astro.ra[1]

            astro.ra[1] = astro.ra[2]
            astro.dec[0] = astro.dec[1]
            astro.dec[1] = astro.dec[2]
            astro.sid[0] = astro.sid[1]
            astro.sid[1] = astro.sid[2]
            astro.dra[0] = astro.dra[1]
            astro.dra[1] = astro.dra[2]
            astro.rsum[0] = astro.rsum[1]
            astro.rsum[1] = astro.rsum[2]
            computeAstroDay(julianDay + 1, ad)
            astro.ra[2] = ad.ra
            astro.dec[2] = ad.dec
            astro.sid[2] = ad.sidtime
            astro.dra[2] = ad.dra
            astro.rsum[2] = ad.rsum
        } else if (astro.jd == julianDay + 1) {

            astro.ra[2] = astro.ra[1]
            astro.ra[1] = astro.ra[0]
            astro.dec[2] = astro.dec[1]
            astro.dec[1] = astro.dec[0]
            astro.sid[2] = astro.sid[1]
            astro.sid[1] = astro.sid[0]
            astro.dra[2] = astro.dra[1]
            astro.dra[1] = astro.dra[0]
            astro.rsum[2] = astro.rsum[1]
            astro.rsum[1] = astro.rsum[0]
            computeAstroDay(julianDay - 1, ad)
            astro.ra[0] = ad.ra
            astro.dec[0] = ad.dec
            astro.sid[0] = ad.sidtime
            astro.dra[0] = ad.dra
            astro.rsum[0] = ad.rsum
        } else if (astro.jd != julianDay) {
            computeAstroDay(julianDay - 1, ad)
            astro.ra[0] = ad.ra
            astro.dec[0] = ad.dec
            astro.sid[0] = ad.sidtime
            astro.dra[0] = ad.dra
            astro.rsum[0] = ad.rsum
            computeAstroDay(julianDay, ad)
            astro.ra[1] = ad.ra
            astro.dec[1] = ad.dec
            astro.sid[1] = ad.sidtime
            astro.dra[1] = ad.dra
            astro.rsum[1] = ad.rsum
            computeAstroDay(julianDay + 1, ad)
            astro.ra[2] = ad.ra
            astro.dec[2] = ad.dec
            astro.sid[2] = ad.sidtime
            astro.dra[2] = ad.dra
            astro.rsum[2] = ad.rsum
        }

        astro.jd = julianDay
        computeTopAstro(loc, astro, topAstro)

    }

    fun computeAstroDay(JD: Double, astroday: AstrologyDay) {

        var i = 0
        val R: Double
        val Gg: Double
        val G: Double

        val tL: Double
        val L: Double
        val tB: Double
        val B: Double

        val X0: Double
        val X1: Double
        val X2: Double
        val X3: Double
        val X4: Double

        val U: Double
        val E0: Double
        val E: Double
        val lamda: Double
        val V0: Double
        val V: Double

        val RAn: Double
        val RAd: Double
        val RA: Double
        val DEC: Double

        var B0sum = 0.0
        var B1sum = 0.0
        var R0sum = 0.0
        var R1sum = 0.0
        var R2sum = 0.0
        var R3sum = 0.0
        var R4sum = 0.0
        var L0sum = 0.0
        var L1sum = 0.0
        var L2sum = 0.0
        var L3sum = 0.0
        var L4sum = 0.0
        var L5sum = 0.0

        var xsum = 0.0
        var psi = 0.0
        var epsilon = 0.0
        val deltaPsi: Double
        val deltaEps: Double

        val JC = (JD - 2451545) / 36525.0
        val JM = JC / 10.0
        val JM2 = Math.pow(JM, 2.0)
        val JM3 = Math.pow(JM, 3.0)
        val JM4 = Math.pow(JM, 4.0)
        val JM5 = Math.pow(JM, 5.0)

        while (i < 64) {
            L0sum += L0[i][0] * Math.cos(L0[i][1] + L0[i][2] * JM)
            i++
        }
        i = 0
        while (i < 34) {
            L1sum += L1[i][0] * Math.cos(L1[i][1] + L1[i][2] * JM)
            i++
        }
        i = 0
        while (i < 20) {
            L2sum += L2[i][0] * Math.cos(L2[i][1] + L2[i][2] * JM)
            i++
        }
        i = 0
        while (i < 7) {
            L3sum += L3[i][0] * Math.cos(L3[i][1] + L3[i][2] * JM)
            i++
        }
        i = 0
        while (i < 3) {
            L4sum += L4[i][0] * Math.cos(L4[i][1] + L4[i][2] * JM)
            i++
        }
        L5sum = L5[0][0] * Math.cos(L5[0][1] + L5[0][2] * JM)

        tL = (L0sum + L1sum * JM + L2sum * JM2 + L3sum * JM3
                + L4sum * JM4 + L5sum * JM5) / Math.pow(10.0, 8.0)

        L = limitAngle(Utils.RAD_TO_DEG(tL))

        i = 0
        while (i < 5) {
            B0sum += B0[i][0] * Math.cos(B0[i][1] + B0[i][2] * JM)
            i++
        }
        i = 0
        while (i < 2) {
            B1sum += B1[i][0] * Math.cos(B1[i][1] + B1[i][2] * JM)
            i++
        }

        tB = (B0sum + B1sum * JM) / Math.pow(10.0, 8.0)
        B = Utils.RAD_TO_DEG(tB)

        i = 0
        while (i < 40) {
            R0sum += R0[i][0] * Math.cos(R0[i][1] + R0[i][2] * JM)
            i++
        }
        i = 0
        while (i < 10) {
            R1sum += R1[i][0] * Math.cos(R1[i][1] + R1[i][2] * JM)
            i++
        }
        i = 0
        while (i < 6) {
            R2sum += R2[i][0] * Math.cos(R2[i][1] + R2[i][2] * JM)
            i++
        }
        i = 0
        while (i < 2) {
            R3sum += R3[i][0] * Math.cos(R3[i][1] + R3[i][2] * JM)
            i++
        }

        R4sum = R4[0] * Math.cos(R4[1] + R4[2] * JM)

        R = (R0sum + R1sum * JM + R2sum * JM2 + R3sum * JM3 + R4sum * JM4) / Math.pow(10.0, 8.0)

        G = limitAngle(L + 180)
        Gg = -B

        X0 = 297.85036 + 445267.111480 * JC - 0.0019142 * Math.pow(JC, 2.0) + Math.pow(JC, 3.0) / 189474.0
        X1 = (357.52772 + 35999.050340 * JC - 0.0001603 * Math.pow(JC, 2.0)
                - Math.pow(JC, 3.0) / 300000.0)
        X2 = (134.96298 + 477198.867398 * JC + 0.0086972 * Math.pow(JC, 2.0)
                + Math.pow(JC, 3.0) / 56250.0)
        X3 = 93.27191 + 483202.017538 * JC - 0.0036825 * Math.pow(JC, 2.0) + Math.pow(JC, 3.0) / 327270.0
        X4 = (125.04452 - 1934.136261 * JC + 0.0020708 * Math.pow(JC, 2.0)
                + Math.pow(JC, 3.0) / 450000.0)

        i = 0
        while (i < 63) {
            xsum += X0 * SINCOEFF[i][0]
            xsum += X1 * SINCOEFF[i][1]
            xsum += X2 * SINCOEFF[i][2]
            xsum += X3 * SINCOEFF[i][3]
            xsum += X4 * SINCOEFF[i][4]
            psi += (PE[i][0] + JC * PE[i][1]) * Math.sin(Utils.DEG_TO_RAD(xsum))
            epsilon += (PE[i][2] + JC * PE[i][3]) * Math.cos(Utils.DEG_TO_RAD(xsum))
            xsum = 0.0
            i++
        }

        deltaPsi = psi / 36000000.0
        deltaEps = epsilon / 36000000.0

        U = JM / 10.0
        E0 = 84381.448 - 4680.93 * U - 1.55 * Math.pow(U, 2.0) + 1999.25 * Math.pow(U, 3.0) - 51.38 * Math.pow(U, 4.0) - 249.67 * Math.pow(U, 5.0) - 39.05 * Math.pow(U, 6.0) + 7.12 * Math.pow(U, 7.0) + 27.87 * Math.pow(U, 8.0) + 5.79 * Math.pow(U, 9.0) + 2.45 * Math.pow(U, 10.0)
        E = E0 / 3600.0 + deltaEps
        lamda = G + deltaPsi + -20.4898 / (3600.0 * R)

        V0 = 280.46061837 + 360.98564736629 * (JD - 2451545) + 0.000387933 * Math.pow(JC, 2.0) - Math.pow(JC, 3.0) / 38710000.0
        V = limitAngle(V0) + deltaPsi * Math.cos(Utils.DEG_TO_RAD(E))

        RAn = Math.sin(Utils.DEG_TO_RAD(lamda)) * Math.cos(Utils.DEG_TO_RAD(E)) - Math.tan(Utils.DEG_TO_RAD(Gg)) * Math.sin(Utils.DEG_TO_RAD(E))
        RAd = Math.cos(Utils.DEG_TO_RAD(lamda))
        RA = limitAngle(Utils.RAD_TO_DEG(Math.atan2(RAn, RAd)))

        DEC = Math.asin(Math.sin(Utils.DEG_TO_RAD(Gg)) * Math.cos(Utils.DEG_TO_RAD(E)) + (Math.cos(Utils.DEG_TO_RAD(Gg))
                * Math.sin(Utils.DEG_TO_RAD(E))
                * Math.sin(Utils.DEG_TO_RAD(lamda))))

        astroday.ra = RA
        astroday.dec = DEC
        astroday.sidtime = V
        astroday.dra = 0.toDouble()
        astroday.rsum = R

    }

    fun computeTopAstro(loc: Location, astro: Astro,
                        topAstro: Astro) {
        var i: Int
        var lHour: Double
        var SP: Double
        var tU: Double
        var tCos: Double
        var tSin: Double
        var tRA0: Double
        var tRA: Double
        var tDEC: Double

        i = 0
        while (i < 3) {
            lHour = limitAngle(astro.sid[i] + loc.degreeLong - astro.ra[i])

            SP = 8.794 / (3600 * astro.rsum[i])

            tU = Math.atan(0.99664719 * Math.tan(Utils.DEG_TO_RAD(loc
                    .degreeLat)))

            tCos = Math.cos(tU) + loc.seaLevel / 6378140.0 * Math.cos(Utils.DEG_TO_RAD(loc.degreeLat))

            tSin = 0.99664719 * Math.sin(tU) + loc.seaLevel / 6378140.0 * Math.sin(Utils.DEG_TO_RAD(loc.degreeLat))

            tRA0 = -tCos * Math.sin(Utils.DEG_TO_RAD(SP)) * Math.sin(Utils
                    .DEG_TO_RAD(lHour)) / (Math.cos(astro.dec[i]) - (tCos
                    * Math.sin(Utils.DEG_TO_RAD(SP))
                    * Math.cos(Utils.DEG_TO_RAD(lHour))))

            tRA = astro.ra[i] + Utils.RAD_TO_DEG(tRA0)

            tDEC = Utils.RAD_TO_DEG(Math.atan2(
                    (Math.sin(astro.dec[i]) - tSin * Math.sin(Utils.DEG_TO_RAD(SP))) * Math.cos(tRA0), Math.cos(astro.dec[i]) - (tCos * Math.sin(Utils.DEG_TO_RAD(SP))
                    * Math.cos(Utils.DEG_TO_RAD(lHour)))))

            topAstro.ra[i] = tRA
            topAstro.dec[i] = tDEC
            topAstro.sid[i] = astro.sid[i]
            topAstro.dra[i] = tRA0
            topAstro.rsum[i] = astro.rsum[i]
            i++

        }

    }

    fun limitAngle(L: Double): Double {
        var limit = L
        val limitFactor: Double
        limit /= 360.0
        limitFactor = limit - Math.floor(limit)
        return if (limitFactor > 0)
            360 * limitFactor
        else if (limitFactor < 0)
            360 - 360 * limitFactor
        else
            limit
    }

    fun limitAngle180(L: Double): Double {
        var limit = L
        val limitFactor: Double
        limit /= 180.0
        limitFactor = limit - Math.floor(limit)
        return if (limitFactor > 0)
            180 * limitFactor
        else if (limitFactor < 0)
            180 - 180 * limitFactor
        else
            limit
    }

    fun limitAngle111(L: Double): Double {
        var F: Double
        F = L - Math.floor(L)
        if (F < 0) F += 1.0
        return F
    }

    fun limitAngle180between(L: Double): Double {
        var limit = L
        var limitFactor: Double
        limit /= 360.0
        limitFactor = (limit - Math.floor(limit)) * 360.0
        if (limitFactor < -180)
            limitFactor += 360.0
        else if (limitFactor > 180)
            limitFactor -= 360.0
        return limitFactor
    }

}
