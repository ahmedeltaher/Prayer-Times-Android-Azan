package com.azan

import com.azan.astrologicalCalc.Utils

class Method {
    var fajrAng: Double = 0.toDouble()

    var ishaaAng: Double = 0.toDouble()

    /**
     *
     * @param imsaakAng The angle difference between Imsaak and Fajr (default
     * is 1.5)
     */
    var imsaakAng: Double = 0.toDouble()

    /**
     *
     * @param fajrInv Fajr Interval is the amount of minutes between Fajr and
     * Shurooq (0 if not used)
     */
    var fajrInv: Int = 0

    /**
     *
     * @param ishaaInv Ishaa Interval is the amount if minutes between Ishaa and
     * Maghrib (0 if not used)
     */
    var ishaaInv: Int = 0

    /**
     *
     * @param imsaakInv Imsaak Interval is the amount of minutes between Imsaak
     * and Fajr. The default is 10 minutes before Fajr if Fajr Interval is set
     */
    var imsaakInv: Int = 0

    /**
     *
     * @param round Method used for rounding seconds
     * @see Rounding
     */
    var round: Rounding? = null

    /**
     *
     * @param madhhab madhhab for calculating assr prayer shadow ratio
     * @see Madhhab
     */
    var madhhab: Madhhab? = null

    /**
     *
     * @param nearestLat Latitude Used for the 'Nearest Latitude' extreme
     * methods. The default is 48.5
     */
    var nearestLat: Double = 0.toDouble()

    /**
     *
     * @param extreme Extreme latitude calculation method
     * @see ExtremeLatitude
     */
    var extremeLatitude: ExtremeLatitude? = null

    /**
     *
     * @param offset Enable Offsets switch (set this to true to activate). This
     * option allows you to add or subtract any amount of minutes to the daily
     * computed prayer times based on values (in minutes) for each prayer in
     * the next xxxOffset parameters
     * For Example: If you want to add 30 seconds to Maghrib and subtract 2
     * minutes from Ishaa:
     * `method.setOffset(true);
     * method.setMaghribOffset(0.5);
     * method.setIshaaOffset(-2); `
     */
    var offset: Boolean = false

    var fajrOffset: Double = 0.toDouble()

    var shurooqOffset: Double = 0.toDouble()

    var thuhrOffset: Double = 0.toDouble()

    var assrOffset: Double = 0.toDouble()

    var maghribOffset: Double = 0.toDouble()

    var ishaaOffset: Double = 0.toDouble()

    constructor() {}

    /**
     * copy constructor
     * @param orig Method to copy from
     */
    constructor(orig: Method) {
        this.fajrAng = orig.fajrAng
        this.ishaaAng = orig.ishaaAng
        this.imsaakAng = orig.imsaakAng
        this.fajrInv = orig.fajrInv
        this.ishaaInv = orig.ishaaInv
        this.imsaakInv = orig.imsaakInv
        this.round = orig.round
        this.madhhab = orig.madhhab
        this.nearestLat = orig.nearestLat
        this.extremeLatitude = orig.extremeLatitude
        this.offset = orig.offset

        this.fajrOffset = orig.fajrOffset
        this.shurooqOffset = orig.shurooqOffset
        this.thuhrOffset = orig.thuhrOffset
        this.assrOffset = orig.assrOffset
        this.maghribOffset = orig.maghribOffset
        this.ishaaOffset = orig.ishaaOffset
    }

    /**
     * Build a method object containing all the information needed to compute
     * prayer time.
     *
     * @param fajrAng Fajr angle
     * @param ishaaAng Ishaa angle
     * @param imsaakAng The angle difference between Imsaak and Fajr (default
     * is 1.5)
     * @param fajrInv Fajr Interval is the amount of minutes between Fajr and
     * Shurooq (0 if not used)
     * @param ishaaInv Ishaa Interval is the amount if minutes between Ishaa and
     * Maghrib (0 if not used)
     * @param imsaakInv Imsaak Interval is the amount of minutes between Imsaak
     * and Fajr. The default is 10 minutes before Fajr if Fajr Interval is set
     * @param round Method used for rounding seconds
     * @param madhhab madhhab for calculating assr prayer shadow ratio
     * @param nearestLat Latitude Used for the 'Nearest Latitude' extreme
     * methods. The default is 48.5
     * @param extreme Extreme latitude calculation method (@see ExtremeLatitude)
     * @param offset Enable Offsets switch (set this to true to activate). This
     * option allows you to add or subtract any amount of minutes to the daily
     * computed prayer times based on values (in minutes) for each prayer in
     * the next xxxOffset parameters
     * For Example: If you want to add 30 seconds to Maghrib and subtract 2
     * minutes from Ishaa:<br></br>
     * `method.setOffset(true);
     * method.setMaghribOffset(0.5);
     * method.setIshaaOffset(-2); `
     * @param fajrOffset fajr offset
     * @param shurooqOffset shurooq offset
     * @param thuhrOffset thuhr offset
     * @param assrOffset assr offset
     * @param maghribOffset maghrib offset
     * @param ishaaOffset ishaa offset
     */
    constructor(fajrAng: Double, ishaaAng: Double, imsaakAng: Double,
                fajrInv: Int, ishaaInv: Int, imsaakInv: Int, round: Rounding, madhhab: Madhhab,
                nearestLat: Double, extreme: ExtremeLatitude, offset: Boolean, fajrOffset: Double,
                shurooqOffset: Double, thuhrOffset: Double, assrOffset: Double,
                maghribOffset: Double, ishaaOffset: Double) {
        this.fajrAng = fajrAng
        this.ishaaAng = ishaaAng
        this.imsaakAng = imsaakAng
        this.fajrInv = fajrInv
        this.ishaaInv = ishaaInv
        this.imsaakInv = imsaakInv
        this.round = round
        this.madhhab = madhhab
        this.nearestLat = nearestLat
        this.extremeLatitude = extreme
        this.offset = offset

        this.fajrOffset = fajrOffset
        this.shurooqOffset = shurooqOffset
        this.thuhrOffset = thuhrOffset
        this.assrOffset = assrOffset
        this.maghribOffset = maghribOffset
        this.ishaaOffset = ishaaOffset

    }

    /**
     * copy constructor
     * @return a new instance of Method containing a clone of
     * the current instance
     */
    fun copy(): Method {
        return Method(fajrAng, ishaaAng, imsaakAng, fajrInv, ishaaInv,
                imsaakInv, round!!, madhhab!!, nearestLat, extremeLatitude!!, offset,
                fajrOffset, shurooqOffset, thuhrOffset, assrOffset,
                maghribOffset, ishaaOffset)
    }

    fun getOffset(prayer: PrayerTime): Double {
        if (prayer == PrayerTime.FAJR)
            return fajrOffset
        if (prayer == PrayerTime.SHUROOQ)
            return shurooqOffset
        if (prayer == PrayerTime.THUHR)
            return thuhrOffset
        if (prayer == PrayerTime.ASSR)
            return assrOffset
        if (prayer == PrayerTime.MAGHRIB)
            return maghribOffset
        return if (prayer == PrayerTime.ISHAA) ishaaOffset else 0.0
    }

    companion object {

        /*
	 fajrAng, ishaaAng, imsaakAng, fajrInv = 0, ishaaInv = 0, imsaakInv = 0, round = 2, madhhab = 1,
	 nearestLat =  Utils.DEF_NEAREST_LATITUDE , extreme = 5, offset = 0, offList = null
	 */

        val NONE = Method(0.0, 0.0, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0, Rounding.SPECIAL, Madhhab.SHAAFI,
                Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        /**
         * Egyptian General Authority of Survey<br></br>
         *
         *  * Fajr Angle      = 20
         *  * Ishaa Angle     = 18
         *  * Used in:
         * Indonesia, Iraq, Jordan, Lebanon, Malaysia, Singapore, Syria, parts of Africa,
         * parts of United States
         *
         */
        val EGYPT_SURVEY = Method(20.0, 18.0, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0, Rounding.SPECIAL,
                Madhhab.SHAAFI, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        /**
         * University of Islamic Sciences, Karachi (Shaf'i)<br></br>
         *  *       Fajr Angle      = 18
         *  * Ishaa Angle     = 18
         *  * Used in:        Iran, Kuwait, parts of Europe
         *
         */
        val KARACHI_SHAF = Method(18.0, 18.0, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0, Rounding.SPECIAL,
                Madhhab.SHAAFI, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        /**
         * University of Islamic Sciences, Karachi (Hanafi)<br></br>
         *  * Fajr Angle      = 18
         *  * Ishaa Angle     = 18
         *  * Used in:        Afghanistan, Bangladesh, India
         *
         */
        val KARACHI_HANAF = Method(18.0, 18.0, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0,
                Rounding.SPECIAL, Madhhab.HANAFI, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        /**
         * Islamic Society of North America<br></br>
         *  * Fajr Angle      = 15
         *  * Ishaa Angle     = 15
         *  * Used in:        Canada, Parts of UK, parts of United States
         *
         */
        val NORTH_AMERICA = Method(15.0, 15.0, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0,
                Rounding.SPECIAL, Madhhab.SHAAFI, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        /**
         * Muslim World League (MWL)<br></br>
         *  * Fajr Angle      = 18
         *  * Ishaa Angle     = 17
         *  * Used in:        parts of Europe, Far East, parts of United States
         *
         *
         */
        val MUSLIM_LEAGUE = Method(18.0, 17.0, Utils.DEF_IMSAAK_ANGLE, 0, 0, 0,
                Rounding.SPECIAL, Madhhab.SHAAFI, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        /**
         * Om Al-Qurra University<br></br>
         *  * Fajr Angle      = 19
         *  * Ishaa Angle     = 0 (not used)
         *  * Ishaa Interval  = 90 minutes from Al-Maghrib prayer
         * but set to 120 during Ramadan.
         *  * Used in:        Saudi Arabia
         *
         */
        val UMM_ALQURRA = Method(19.0, 0.0, Utils.DEF_IMSAAK_ANGLE, 0, 90, 0, Rounding.SPECIAL,
                Madhhab.SHAAFI, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        /**
         * Fixed Ishaa Angle Interval (always 90)<br></br>
         *  * Fajr Angle      = 19.5
         *  * Ishaa Angle     = 0 (not used)
         *  * Ishaa Interval  = 90 minutes from Al-Maghrib prayer.
         *  * Used in:          Bahrain, Oman, Qatar, United Arab Emirates
         *
         */
        val FIXED_ISHAA = Method(19.5, 0.0, Utils.DEF_IMSAAK_ANGLE, 0, 90, 0,
                Rounding.SPECIAL, Madhhab.SHAAFI, Utils.DEF_NEAREST_LATITUDE, ExtremeLatitude.GOOD_INVALID, false, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }

}
