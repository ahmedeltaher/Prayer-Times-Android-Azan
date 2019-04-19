package com.azan

import com.azan.astrologicalCalc.*
import java.util.*


/**
 * This the main class of the JITL library. You can use static methods
 * to do qibla calculation but you will need to create a Azan instance
 * to calculate prayer times for a specific location, using a specified method.
 *
 */
class Azan
/**
 * Creates the jitl main class
 * @param location the location
 * @param method the method used in the calculation. You can use
 * predefined methods for example `Method.MUSLIM_LEAGUE` or creates
 * your own personalized method.
 */
(private var location: Location?, private var method: Method?) {


    /* This is Used for storing some formulae results between
	 * multiple getPrayerTimes calls*/
    private val astroCache = Astro()

    /**
     * generate qibla direction
     * @return a SimpleTime object containing qibla direction for the current location
     */
    val northQibla: SimpleTime
        get() = getNorthQibla(location!!)

    /**
     * changes the location
     * @param loc the new location
     */
    fun setLocation(loc: Location) {
        this.location = loc
    }

    /**
     * changes the method
     * @param method the new method
     */
    fun setMethod(method: Method) {
        this.method = method
    }

    /**
     * generates prayer times
     * @param date the date of prayers
     * @return a AzanTimes object containing time of different
     * prayers
     */
    fun getPrayerTimes(date: GregorianCalendar): AzanTimes {
        return getPrayerTimes(SimpleDate(date))
    }

    /**
     * Generate prayer times from a GregorianCalendar date
     * @param date the date of prayers
     * @param pt instance of a AzanTimes object
     */
    fun getPrayerTimes(date: GregorianCalendar, pt: AzanTimes) {
        getPrayerTimes(SimpleDate(date), pt)
    }

    /**
     * Create a AzanTimes instance and fill it with prayer times
     * @param date SimpleDate object
     * @return a AzanTimes instance containing prayer times
     */
    fun getPrayerTimes(date: SimpleDate): AzanTimes {
        val dp = AzanTimes()
        getPrayerTimes(date, dp)

        return dp
    }

    /**
     * Generate prayer times from a SimpleDate date
     * @param date SimpleDate object
     * @param pt instance of a AzanTimes object
     */
    fun getPrayerTimes(date: SimpleDate, pt: AzanTimes) {
        val dc: DayCouple

        dc = getDayInfo(date, location!!.gmtDiff)
        getPrayerTimesByDay(dc, pt, PrayerTime.FAJR)
    }

    internal fun getPrayerTimesByDay(dc: DayCouple, pt: AzanTimes, type: PrayerTime) {
        getPrayerTimesByDay(method, dc, pt, type)
    }

    internal fun getPrayerTimesByDay(method: Method?, dc: DayCouple, pt: AzanTimes,
                                     type: PrayerTime) {
        var i: Int
        var invalid: Int
        val th: Double
        val sh: Double
        val mg: Double
        val fj: Double
        val `is`: Double
        val ar: Double
        val lat: Double
        val lon: Double
        val dec: Double
        val tempPrayer = DoubleArray(6)
        val tAstro = Astro()

        lat = location!!.degreeLat
        lon = location!!.degreeLong
        invalid = 0

        /* Start by filling the tAstro structure with the appropriate astronomical
		 * values for this day. We also pass the cache structure to update and check
		 * if the actual values are already available. */
        AstrologyFormulas
                .getAstroValuesByDay(dc.julianDay, location!!, astroCache, tAstro)
        dec = Utils.DEG_TO_RAD(tAstro.dec[1])

        /* Get Time Times formulae results for this day of year and this
		 * location. The results are NOT the actual prayer times */
        fj = getFajIsh(lat, dec, method!!.fajrAng)
        sh = getShoMag(location!!, tAstro, PrayerTime.SHUROOQ)
        th = getThuhr(lon, tAstro)
        ar = getAssr(lat, dec, method.madhhab)
        mg = getShoMag(location!!, tAstro, PrayerTime.MAGHRIB)
        `is` = getFajIsh(lat, dec, method.ishaaAng)

        /* Calculate all prayer times as Base-10 numbers in Normal circumstances */
        /* Fajr */
        if (fj == 99.0) {
            tempPrayer[0] = 99.0
            invalid = 1
        } else {
            tempPrayer[0] = th - fj
        }

        if (sh == 99.0) {
            invalid = 1
        }

        tempPrayer[1] = sh
        tempPrayer[2] = th
        tempPrayer[3] = th + ar
        tempPrayer[4] = mg

        if (mg == 99.0) {
            invalid = 1
        }

        /* Ishaa */
        if (`is` == 99.0) {
            tempPrayer[5] = 99.0
            invalid = 1
        } else {
            tempPrayer[5] = th + `is`
        }

        /* Calculate all prayer times as Base-10 numbers in Extreme Latitudes (if
		 * needed) */

        /* Reset status of extreme switches */
        pt.setAllExtreme(false)

        if (method.extremeLatitude != ExtremeLatitude.NONE_EX && !((method.extremeLatitude == ExtremeLatitude.GOOD_INVALID
                        || method.extremeLatitude == ExtremeLatitude.LAT_INVALID
                        || method.extremeLatitude == ExtremeLatitude.SEVEN_NIGHT_INVALID
                        || method.extremeLatitude == ExtremeLatitude.SEVEN_DAY_INVALID || method
                        .extremeLatitude == ExtremeLatitude.HALF_INVALID) && invalid == 0)) {
            var exdecPrev: Double
            var exdecNext: Double
            var exTh = 99.0
            var exFj = 99.0
            var exIs = 99.0
            var exAr = 99.0
            var exSh = 99.0
            var exMg = 99.0
            //exIm=99

            var portion = 0.0
            var nGoodDay = 0.0
            var exinterval = 0
            val exLoc = location!!.copy()
            val exAstroPrev: Astro
            val exAstroNext: Astro
            val ext = method.extremeLatitude

            /* Nearest Latitude (Method.nearestLat) */
            if (ext == ExtremeLatitude.LAT_ALL || ext == ExtremeLatitude.LAT_ALWAYS || ext == ExtremeLatitude.LAT_INVALID) {
                /*
			case LAT_ALL:
			case LAT_ALWAYS:
			case LAT_INVALID:
			*/

                /* xxxthamer: we cannot compute this when interval is set because
				 * angle==0 . Only the if-invalid methods would work */
                exLoc.degreeLat = method.nearestLat
                exFj = getFajIsh(method.nearestLat, dec, method
                        .fajrAng)
                //exIm = getFajIsh(method.getNearestLat(), dec, method.getImsaakAng());
                exIs = getFajIsh(method.nearestLat, dec, method
                        .ishaaAng)
                exAr = getAssr(method.nearestLat, dec, method.madhhab)
                exSh = getShoMag(exLoc, tAstro, PrayerTime.SHUROOQ)
                exMg = getShoMag(exLoc, tAstro, PrayerTime.MAGHRIB)

                //switch (ext) {
                if (ext == ExtremeLatitude.LAT_ALL) {
                    //case LAT_ALL:
                    tempPrayer[0] = th - exFj
                    tempPrayer[1] = exSh
                    tempPrayer[3] = th + exAr
                    tempPrayer[4] = exMg
                    tempPrayer[5] = th + exIs
                    pt.setAllExtreme(true)
                } else if (ext == ExtremeLatitude.LAT_ALWAYS) {
                    tempPrayer[0] = th - exFj
                    tempPrayer[5] = th + exIs
                    pt.fajr().isExtreme = true
                    pt.ishaa().isExtreme = true
                } else if (ext == ExtremeLatitude.LAT_INVALID) {
                    if (tempPrayer[0] == 99.0) {
                        tempPrayer[0] = th - exFj
                        pt.fajr().isExtreme = true
                    }
                    if (tempPrayer[5] == 99.0) {
                        tempPrayer[5] = th + exIs
                        pt.ishaa().isExtreme = true
                    }
                }

            } else if (ext == ExtremeLatitude.GOOD_ALL || ext == ExtremeLatitude.GOOD_INVALID
                    || ext == ExtremeLatitude.GOOD_DIF) {
                /* Nearest Good Day */
                exAstroPrev = astroCache
                exAstroNext = astroCache

                /* Start by getting last or next nearest Good Day */
                i = 0
                while (i <= dc.lastDay) {

                    /* last closest day */
                    nGoodDay = dc.julianDay - i
                    AstrologyFormulas.getAstroValuesByDay(nGoodDay, location!!, exAstroPrev,
                            tAstro)
                    exdecPrev = Utils.DEG_TO_RAD(tAstro.dec[1])
                    exFj = getFajIsh(lat, exdecPrev, method.fajrAng)

                    if (exFj != 99.0) {
                        exIs = getFajIsh(lat, exdecPrev, method.ishaaAng)
                        if (exIs != 99.0) {
                            exTh = getThuhr(lon, tAstro)
                            exSh = getShoMag(location!!, tAstro, PrayerTime.SHUROOQ)
                            exMg = getShoMag(location!!, tAstro, PrayerTime.MAGHRIB)
                            exAr = getAssr(lat, exdecPrev, method.madhhab)
                            break
                        }
                    }

                    /* Next closest day */
                    nGoodDay = dc.julianDay + i
                    AstrologyFormulas.getAstroValuesByDay(nGoodDay, location!!, exAstroNext,
                            tAstro)
                    exdecNext = Utils.DEG_TO_RAD(tAstro.dec[1])
                    exFj = getFajIsh(lat, exdecNext, method.fajrAng)
                    if (exFj != 99.0) {
                        exIs = getFajIsh(lat, exdecNext, method.ishaaAng)
                        if (exIs != 99.0) {
                            exTh = getThuhr(lon, tAstro)
                            exSh = getShoMag(location!!, tAstro, PrayerTime.SHUROOQ)
                            exMg = getShoMag(location!!, tAstro, PrayerTime.MAGHRIB)
                            exAr = getAssr(lat, exdecNext, method.madhhab)
                            break
                        }
                    }
                    i++
                }

                if (ext == ExtremeLatitude.GOOD_ALL) {
                    tempPrayer[0] = exTh - exFj
                    tempPrayer[1] = exSh
                    tempPrayer[2] = exTh
                    tempPrayer[3] = exTh + exAr
                    tempPrayer[4] = exMg
                    tempPrayer[5] = exTh + exIs
                    pt.setAllExtreme(true)

                } else if (ext == ExtremeLatitude.GOOD_INVALID) {
                    if (tempPrayer[0] == 99.0) {
                        tempPrayer[0] = exTh - exFj
                        pt.fajr().isExtreme = true
                    }
                    if (tempPrayer[5] == 99.0) {
                        tempPrayer[5] = exTh + exIs
                        pt.ishaa().isExtreme = true
                    }
                } else if (ext == ExtremeLatitude.GOOD_DIF) {
                    /* Nearest Good Day: Different good days for Fajr and Ishaa (Not
					 * implemented) */
                }
            } else if (ext == ExtremeLatitude.SEVEN_NIGHT_ALWAYS || ext == ExtremeLatitude.SEVEN_NIGHT_INVALID
                    || ext == ExtremeLatitude.SEVEN_DAY_ALWAYS || ext == ExtremeLatitude.SEVEN_DAY_INVALID
                    || ext == ExtremeLatitude.HALF_ALWAYS || ext == ExtremeLatitude.HALF_INVALID) {


                /* xxxthamer: For clarity, we may need to move the HALF_* methods
				 * into their own separate case statement. */

                if (ext == ExtremeLatitude.SEVEN_NIGHT_ALWAYS || ext == ExtremeLatitude.SEVEN_NIGHT_INVALID) {
                    portion = (24 - (tempPrayer[4] - tempPrayer[1])) * (1 / 7.0)
                } else if (ext == ExtremeLatitude.SEVEN_DAY_ALWAYS || ext == ExtremeLatitude.SEVEN_DAY_INVALID) {
                    portion = (tempPrayer[4] - tempPrayer[1]) * (1 / 7.0)
                } else if (ext == ExtremeLatitude.HALF_ALWAYS || ext == ExtremeLatitude.HALF_INVALID) {
                    portion = (24.0 - tempPrayer[4] - tempPrayer[1]) * (1 / 2.0)
                }

                if (method.extremeLatitude == ExtremeLatitude.SEVEN_NIGHT_INVALID
                        || method.extremeLatitude == ExtremeLatitude.SEVEN_DAY_INVALID
                        || method.extremeLatitude == ExtremeLatitude.HALF_INVALID) {
                    if (tempPrayer[0] == 99.0) {
                        if (method.extremeLatitude == ExtremeLatitude.HALF_INVALID)
                            tempPrayer[0] = portion - method.fajrInv / 60.0
                        else
                            tempPrayer[0] = tempPrayer[1] - portion
                        pt.fajr().isExtreme = true
                    }
                    if (tempPrayer[5] == 99.0) {
                        if (method.extremeLatitude == ExtremeLatitude.HALF_INVALID)
                            tempPrayer[5] = portion + method.ishaaInv / 60.0
                        else
                            tempPrayer[5] = tempPrayer[4] + portion
                        pt.ishaa().isExtreme = true
                    }
                } else { /* for the always methods */

                    if (method.extremeLatitude == ExtremeLatitude.HALF_ALWAYS) {
                        tempPrayer[0] = portion - method.fajrInv / 60.0
                        tempPrayer[5] = portion + method.ishaaInv / 60.0
                    } else {
                        tempPrayer[0] = tempPrayer[1] - portion
                        tempPrayer[5] = tempPrayer[4] + portion
                    }
                    pt.fajr().isExtreme = true
                    pt.ishaa().isExtreme = true
                }
            } else if (ext == ExtremeLatitude.MIN_ALWAYS) {
                /* Do nothing here because this is implemented through fajrInv and
				 * ishaaInv structure members */
                tempPrayer[0] = tempPrayer[1]
                tempPrayer[5] = tempPrayer[4]
                pt.fajr().isExtreme = true
                pt.ishaa().isExtreme = true
            } else if (ext == ExtremeLatitude.MIN_INVALID) {
                if (tempPrayer[0] == 99.0) {
                    exinterval = (method.fajrInv.toDouble() / 60.0).toInt()
                    tempPrayer[0] = tempPrayer[1] - exinterval
                    pt.fajr().isExtreme = true
                }
                if (tempPrayer[5] == 99.0) {
                    exinterval = (method.ishaaInv.toDouble() / 60.0).toInt()
                    tempPrayer[5] = tempPrayer[4] + exinterval
                    pt.ishaa().isExtreme = true
                }
            } /* end switch */
        } /* end extreme */

        /* Apply intervals if set */
        if (method.extremeLatitude != ExtremeLatitude.MIN_INVALID
                && method.extremeLatitude != ExtremeLatitude.HALF_INVALID
                && method.extremeLatitude != ExtremeLatitude.HALF_ALWAYS) {
            if (method.fajrInv != 0)
                tempPrayer[0] = tempPrayer[1] - method.fajrInv / 60.0
            if (method.ishaaInv != 0)
                tempPrayer[5] = tempPrayer[4] + method.ishaaInv / 60.0
        }

        /* Final Step: Fill the Time array by doing decimal degree to
		 * Time structure conversion*/
        if (type == PrayerTime.IMSAAK || type == PrayerTime.NEXTFAJR) {
            base6hm(tempPrayer[0], method, pt.fajr(), type)
        } else {
            val timeArray = arrayOf(PrayerTime.FAJR, PrayerTime.SHUROOQ, PrayerTime.THUHR, PrayerTime.ASSR, PrayerTime.MAGHRIB, PrayerTime.ISHAA)

            i = 0
            while (i < 6) {
                base6hm(tempPrayer[i], method, pt.times[i], timeArray[i])
                i++
            }
        }

    }

    internal fun base6hm(bs: Double, method: Method, pt: Time, type: PrayerTime) {
        var bs = bs
        var min: Double
        var sec: Double

        if (bs == 99.0) {
            pt.hour = 99
            pt.minute = 99
            pt.second = 0
            return
        }

        /* Add offsets */
        if (method.offset) {
            if (type == PrayerTime.IMSAAK || type == PrayerTime.NEXTFAJR)
                bs += method.fajrOffset / 60.0
            else
                bs += method.getOffset(type) / 60.0
        }

        /* Fix after minus offsets before midnight */
        if (bs < 0) {
            while (bs < 0)
                bs = 24 + bs
        }

        min = (bs - Math.floor(bs)) * 60
        sec = (min - Math.floor(min)) * 60

        /* Add rounding minutes */
        if (method.round == Rounding.NORMAL) {
            if (sec >= Utils.DEFAULT_ROUND_SEC)
                bs += 1 / 60.0
            /* compute again */
            min = (bs - Math.floor(bs)) * 60
            sec = 0.0

        } else if (method.round == Rounding.SPECIAL || method.round == Rounding.AGRESSIVE) {
            if (type == PrayerTime.FAJR || type == PrayerTime.THUHR
                    || type == PrayerTime.ASSR || type == PrayerTime.MAGHRIB
                    || type == PrayerTime.ISHAA || type == PrayerTime.NEXTFAJR) {
                if (method.round == Rounding.SPECIAL) {
                    if (sec >= Utils.DEFAULT_ROUND_SEC) {
                        bs += 1 / 60.0
                        min = (bs - Math.floor(bs)) * 60
                    }
                } else if (method.round == Rounding.AGRESSIVE) {
                    if (sec >= Utils.AGGRESSIVE_ROUND_SEC) {
                        bs += 1 / 60.0
                        min = (bs - Math.floor(bs)) * 60
                    }
                }
                sec = 0.0
            } else {
                //case Time.SHUROOQ:
                //case Time.IMSAAK:
                sec = 0.0
            }
        }

        /* Add daylight saving time and fix after midnight times */
        bs += location!!.dst.toDouble()
        if (bs >= 24) {
            bs = Math.IEEEremainder(bs, 24.0)
        }

        pt.hour = bs.toInt()
        pt.minute = min.toInt()
        pt.second = sec.toInt()

    }

    /**
     * Generate imsaak time
     * @param date GregorianCalendar date
     * @return imsaak time
     */
    fun getImsaak(date: GregorianCalendar): Time {
        return getImsaak(SimpleDate(date))
    }

    /**
     * Generate imsaak time
     * @param date SimpleDate date
     * @return imsaak time
     */
    fun getImsaak(date: SimpleDate): Time {

        var tmpConf: Method
        val dc: DayCouple
        val temp = AzanTimes()

        tmpConf = method!!.copy()

        if (method!!.fajrInv != 0) {
            if (method!!.imsaakInv == 0)
                tmpConf
                        .fajrInv = (tmpConf.fajrInv + Utils.DEF_IMSAAK_INTERVAL).toInt()
            else
                tmpConf.fajrInv = tmpConf.fajrInv + method!!
                        .imsaakInv

        } else if (method!!.imsaakInv != 0) {
            /* use an inv even if al-Fajr is computed (Indonesia?) */
            tmpConf.fajrOffset = tmpConf.fajrOffset + method!!.imsaakInv * -1
            tmpConf.offset = true
        } else {
            tmpConf.fajrAng = tmpConf.fajrAng + method!!.imsaakAng
        }

        dc = getDayInfo(date, location!!.gmtDiff)
        getPrayerTimesByDay(tmpConf, dc, temp, PrayerTime.IMSAAK)

        /* xxxthamer: We probably need to check whether it's possible to compute
		 * Imsaak normally for some extreme methods first */
        /* In case of an extreme Fajr time calculation use intervals for Imsaak and
		 * compute again */
        if (temp.fajr().isExtreme) {
            tmpConf = method!!.copy()
            if (method!!.imsaakInv == 0) {
                tmpConf.fajrOffset = tmpConf.fajrOffset - Utils.DEF_IMSAAK_INTERVAL
                tmpConf.offset = true
            } else {
                tmpConf.fajrOffset = tmpConf.fajrOffset - method!!.imsaakInv
                tmpConf.offset = true
            }
            getPrayerTimesByDay(tmpConf, dc, temp, PrayerTime.IMSAAK)
        }

        return temp.fajr()
    }

    /**
     * Generate next day imsaak time
     * @param date GregorianCalendar date
     * @return next day imsaak time
     */
    fun getNextDayImsaak(date: GregorianCalendar): Time {
        return getNextDayImsaak(SimpleDate(date))
    }

    /**
     * Generate next day imsaak time
     * @param date SimpleDate date
     * @return next day imsaak time
     */
    fun getNextDayImsaak(date: SimpleDate): Time {
        /* Copy the date structure and increment for next day.*/
        val tempd = date.copy()
        tempd.day = tempd.day + 1

        return getImsaak(tempd)
    }

    /**
     * Generate next day fajr time
     * @param date GregorianCalendar date
     * @return next day fajr time
     */
    fun getNextDayFajr(date: GregorianCalendar): Time {
        return getNextDayFajr(SimpleDate(date))
    }

    /**
     * Generate next day fajr time
     * @param date SimpleDate date
     * @return next day fajr time
     */
    fun getNextDayFajr(date: SimpleDate): Time {

        val temp = AzanTimes()
        val dc: DayCouple

        dc = getDayInfo(date, location!!.gmtDiff)
        dc.julianDay = dc.julianDay + 1
        getPrayerTimesByDay(dc, temp, PrayerTime.NEXTFAJR)
        return temp.fajr().copy()
    }

    companion object {

        /**
         * minor version of jitl
         */
        /**
         * Minor version of Azan
         * @return Azan minor version
         */
        val minorVersion = 0

        /**
         * major version of jitl
         */
        /**
         * Major version of Azan
         * @return Azan major version
         */
        val majorVersion = 1

        internal fun getFajIsh(Lat: Double, dec: Double, Ang: Double): Double {

            val part1 = Math.cos(Utils.DEG_TO_RAD(Lat)) * Math.cos(dec)
            val part2 = -Math.sin(Utils.DEG_TO_RAD(Ang)) - Math.sin(Utils.DEG_TO_RAD(Lat)) * Math.sin(dec)

            val part3 = part2 / part1
            return if (part3 <= Utils.INVALID_TRIGGER) {
                99.0
            } else Utils.DEG_TO_10_BASE * Utils.RAD_TO_DEG(Math.acos(part3))

        }

        internal fun getShoMag(loc: Location, astro: Astro, type: PrayerTime): Double {
            val lhour: Double
            var M: Double
            val sidG: Double
            var ra0 = astro.ra[0]
            var ra2 = astro.ra[2]
            val A: Double
            val B: Double
            val H: Double
            var sunAlt: Double
            val R: Double
            val tH: Double

            val part1 = Math.sin(Utils.DEG_TO_RAD(loc.degreeLat)) * Math.sin(Utils.DEG_TO_RAD(astro.dec[1]))
            val part2a = Utils.CENTER_OF_SUN_ANGLE
            val part2 = Math.sin(Utils.DEG_TO_RAD(part2a)) - part1
            val part3 = Math.cos(Utils.DEG_TO_RAD(loc.degreeLat)) * Math.cos(Utils.DEG_TO_RAD(astro.dec[1]))

            val part4 = part2 / part3

            if (part4 <= -1 || part4 >= 1)
                return 99.0

            lhour = AstrologyFormulas.limitAngle180(Utils.RAD_TO_DEG(Math.acos(part4)))
            M = (astro.ra[1] - loc.degreeLong - astro.sid[1]) / 360.0

            if (type == PrayerTime.SHUROOQ)
                M = M - lhour / 360.0
            if (type == PrayerTime.MAGHRIB)
                M = M + lhour / 360.0

            M = AstrologyFormulas.limitAngle111(M)

            sidG = AstrologyFormulas.limitAngle(astro.sid[1] + 360.985647 * M)

            ra0 = astro.ra[0]
            ra2 = astro.ra[2]

            if (astro.ra[1] > 350 && astro.ra[2] < 10)
                ra2 += 360.0
            if (astro.ra[0] > 350 && astro.ra[1] < 10)
                ra0 = 0.0

            A = astro.ra[1] + M * (astro.ra[1] - ra0 + (ra2 - astro.ra[1]) + (ra2 - astro
                    .ra[1] - (astro.ra[1] - ra0)) * M) / 2.0

            B = astro.dec[1] + M * (astro.dec[1] - astro.dec[0]
                    + (astro.dec[2] - astro.dec[1]) + (astro
                    .dec[2] - astro.dec[1] - (astro
                    .dec[1] - astro.dec[0])) * M) / 2.0

            H = AstrologyFormulas.limitAngle180between(sidG + loc.degreeLong - A)

            tH = H - Utils.RAD_TO_DEG(astro.dra[1])

            sunAlt = Utils.RAD_TO_DEG(Math.asin(Math.sin(Utils.DEG_TO_RAD(loc
                    .degreeLat)) * Math.sin(Utils.DEG_TO_RAD(B)) + (Math.cos(Utils.DEG_TO_RAD(loc.degreeLat))
                    * Math.cos(Utils.DEG_TO_RAD(B))
                    * Math.cos(Utils.DEG_TO_RAD(tH)))))

            sunAlt += AstrologyFormulas.getRefraction(loc, sunAlt)

            R = M + (sunAlt - Utils.CENTER_OF_SUN_ANGLE + Utils.ALTITUDE_REFRACTION * Math
                    .pow(loc.seaLevel, 0.5)) / (360.0
                    * Math.cos(Utils.DEG_TO_RAD(B))
                    * Math.cos(Utils.DEG_TO_RAD(loc.degreeLat)) * Math
                    .sin(Utils.DEG_TO_RAD(tH)))

            return R * 24.0

        }

        internal fun getThuhr(lon: Double, astro: Astro): Double {

            var M: Double
            val sidG: Double
            var ra0 = astro.ra[0]
            var ra2 = astro.ra[2]
            val A: Double
            val H: Double

            M = (astro.ra[1] - lon - astro.sid[1]) / 360.0
            M = AstrologyFormulas.limitAngle111(M)
            sidG = astro.sid[1] + 360.985647 * M

            if (astro.ra[1] > 350 && astro.ra[2] < 10)
                ra2 += 360.0
            if (astro.ra[0] > 350 && astro.ra[1] < 10)
                ra0 = 0.0

            A = astro.ra[1] + M * (astro.ra[1] - ra0 + (ra2 - astro.ra[1]) + (ra2 - astro
                    .ra[1] - (astro.ra[1] - ra0)) * M) / 2.0

            H = AstrologyFormulas.limitAngle180between(sidG + lon - A)

            return 24.0 * (M - H / 360.0)
        }

        internal fun getAssr(Lat: Double, dec: Double, madhhab: Madhhab?): Double {
            var part1: Double
            val part2: Double
            val part3: Double
            val part4: Double
            val mathhabValue = if (madhhab == Madhhab.SHAAFI) 1 else 2

            part1 = mathhabValue + Math.tan(Utils.DEG_TO_RAD(Lat) - dec)
            if (part1 < 1 || Lat < 0)
                part1 = mathhabValue - Math.tan(Utils.DEG_TO_RAD(Lat) - dec)

            part2 = Utils.PI / 2.0 - Math.atan(part1)
            part3 = Math.sin(part2) - Math.sin(Utils.DEG_TO_RAD(Lat)) * Math.sin(dec)
            part4 = part3 / (Math.cos(Utils.DEG_TO_RAD(Lat)) * Math.cos(dec))

            /*  if (part4 > 1) */
            /*      return 99; */

            return Utils.DEG_TO_10_BASE * Utils.RAD_TO_DEG(Math.acos(part4))
        }

        internal fun getDayofYear(year: Int, month: Int, day: Int): Int {
            var day = day
            var i: Int
            val isLeap = if (year and 3 == 0 && (year % 100 != 0 || year % 400 == 0))
                1
            else
                0

            val dayList = arrayOf(charArrayOf(0.toChar(), 31.toChar(), 28.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 30.toChar(), 31.toChar()), charArrayOf(0.toChar(), 31.toChar(), 29.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 31.toChar(), 30.toChar(), 31.toChar(), 30.toChar(), 31.toChar()))

            i = 1
            while (i < month) {
                day += dayList[isLeap][i].toInt()
                i++
            }

            return day
        }

        internal fun getDayInfo(date: SimpleDate, gmt: Double): DayCouple {
            val ld: Int
            val jd: Double
            ld = getDayofYear(date.year, 12, 31)
            jd = AstrologyFormulas.getJulianDay(date, gmt)
            return DayCouple(ld, jd)
        }

        /* Obtaining the direction of the shortest distance towards Qibla by uMath.sing the
	 * great circle formula */

        /**
         * generate qibla direction
         * @param loc location where to calculate
         * @return a SimpleTime object containg qibla direction
         */
        fun getNorthQibla(loc: Location): SimpleTime {
            /* xxxthamer: reduce Utils.DEG_TO_RAD usage */
            val num: Double
            val denom: Double
            num = Math.sin(Utils.DEG_TO_RAD(loc.degreeLong) - Utils.DEG_TO_RAD(Utils.KAABA_LONG))
            denom = Math.cos(Utils.DEG_TO_RAD(loc.degreeLat)) * Math
                    .tan(Utils.DEG_TO_RAD(Utils.KAABA_LAT)) - Math.sin(Utils.DEG_TO_RAD(loc.degreeLat)) * Math
                    .cos(Utils.DEG_TO_RAD(loc.degreeLong) - Utils
                            .DEG_TO_RAD(Utils.KAABA_LONG))
            return SimpleTime(Utils.RAD_TO_DEG(Math.atan2(num, denom)))

        }
    }

}
