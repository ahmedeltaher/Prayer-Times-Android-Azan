package com.azan

/**
 * Encapsulates the list of times time and shuruq time
 */
class AzanTimes {
    /**
     * Get prayer list as an array
     * @return an array containing the list of prayer times including the
     * shuruq. The size of the array is 6.
     */
    val times = arrayOf(Time(),Time(),Time(),Time(),Time(),Time())


    /**
     * set all prayer calculation to extreme
     *
     * @param extreme extreme boolean value
     */
    fun setAllExtreme(extreme: Boolean) {
        for (i in 0..5) {
            times[i].isExtreme = extreme
        }
    }

    /**
     * Fajr prayer time
     * @return fajr prayer time
     */
    fun fajr(): Time {
        return times[0]
    }

    /**
     * Shuruq time
     * @return shuruq time
     */
    fun shuruq(): Time {
        return times[1]
    }

    /**
     * Thuhr time
     * @return thuhr prayer time
     */
    fun thuhr(): Time {
        return times[2]
    }

    /**
     * Assr time
     * @return assr prayer time
     */
    fun assr(): Time {
        return times[3]
    }

    /**
     * Maghrib time
     * @return maghrib time
     */
    fun maghrib(): Time {
        return times[4]
    }

    /**
     * Ishaa time
     * @return ishaa time
     */
    fun ishaa(): Time {
        return times[5]
    }

    /**
     * convert prayer times to a string.
     * @return prayer times as a string. It contains 6 lines
     */
    override fun toString(): String {
        var ret = ""
        for (i in 0..5) {
            ret += times[i].toString() + "\n"
        }
        return ret
    }

    /**
     * Creates an iterator on the times
     * @return an iterator over the times
     * @see .getTimes
     */
    operator fun iterator(): Iterator<*> {

        return object : Iterator<Any> {
            private var i = 0

            override operator fun hasNext(): Boolean {
                return i < 6
            }

            override operator fun next(): Any {
                return times[i++]
            }

            fun remove() {
                if (i > 0) {
                    i--
                }
            }

        }
    }
}
