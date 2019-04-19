package com.azan.astrologicalCalc

/**
 * this class encapsulates a number represented in DMS (Degrees,
 * Minutes, Seconds) format and provides some utility methods
 */
class SimpleTime {

    /**
     *
     * @return degree value of the angle
     */
    var degree: Int = 0
        private set

    /**
     *
     * @return minute value of the angle
     */
    var minute: Int = 0
        private set

    /**
     *
     * @return second value of the angle
     */
    var second: Double = 0.toDouble()
        private set

    /**
     * Construct a DMS object using degree, minute and second values
     * @param degree
     * @param minute
     * @param second
     */
    constructor(degree: Int, minute: Int, second: Double) {
        this.degree = degree
        this.minute = minute
        this.second = second
    }

    /**
     * construct a DMS object from a decimal value
     * @param decimal
     */
    constructor(decimal: Double) {
        val tempmin: Double
        var v: Double
        v = Math.floor(decimal)
        degree = v.toInt()
        tempmin = (decimal - v) * 60.0

        v = Math.floor(tempmin)
        minute = v.toInt()
        second = (tempmin - v) * 60.0

    }

    /**
     * return decimal value of the DMS angle.
     * @param dir reference direction. If direction is `Direction.SOUTH`
     * or `Direction.WEST` then the decimal value is multiplied by -1
     * @return signed decimal value of the DMS angle
     * @see Direction
     */
    fun getDecimalValue(dir: Direction): Double {
        val sum = degree + (minute / 60.0 + second / 3600.0)
        return if (dir === Direction.WEST || dir === Direction.SOUTH) {
            sum * -1.0
        } else sum
    }

    override fun toString(): String {
        return "$degree�$minute'$second''"
    }

}
