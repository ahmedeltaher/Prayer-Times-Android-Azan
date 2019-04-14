package com.azan

/**
 * This class encapsulates prayer times.
 */
class Time {

    var hour: Int = 0 /* prayer time hour */

    var minute: Int = 0 /* prayer time minute */

    var second: Int = 0 /* prayer time second */

    /**
     * @return Extreme calculation switch. The 'getPrayerTimes'
     * function sets this switch to true to indicate that this
     * particular prayer time has been calculated through
     * extreme latitude methods and NOT by conventional
     * means of calculation.
     */
    var isExtreme: Boolean = false /*  */

    constructor(hour: Int, minute: Int, second: Int, extreme: Boolean) : super() {
        this.hour = hour
        this.minute = minute
        this.second = second
        this.isExtreme = extreme
    }

    constructor() {

    }

    /**
     * copy constructor
     * @return a copy of the current instance
     */
    fun copy(): Time {
        return Time(hour, minute, second, isExtreme)
    }

    override fun toString(): String {
        return hour.toString() + ":" + (if (minute < 10) "0$minute" else minute.toString() + "") + ":" + if (second < 10) "0$second" else second.toString() + ""
    }
}
