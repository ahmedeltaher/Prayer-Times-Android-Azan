package com.azan.astrologicalCalc

import java.util.*

class SimpleDate {
    var day: Int = 0

    var month: Int = 0

    var year: Int = 0

    /**
     * TODO
     * @param day
     * @param month
     * @param year
     */
    constructor(day: Int, month: Int, year: Int) {
        this.day = day
        this.month = month
        this.year = year
    }

    /**
     * TODO
     * @param gCalendar
     */
    constructor(gCalendar: GregorianCalendar) {
        this.day = gCalendar.get(GregorianCalendar.DATE)
        this.month = gCalendar.get(GregorianCalendar.MONTH) + 1
        this.year = gCalendar.get(GregorianCalendar.YEAR)
    }

    fun copy(): SimpleDate {
        return SimpleDate(day, month, year)
    }

}
