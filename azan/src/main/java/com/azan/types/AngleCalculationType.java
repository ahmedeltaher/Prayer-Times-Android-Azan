package com.azan.types;

/**
 * Created by AhmedEltaher on 11/11/16.
 */

/**
 * MWL: Muslim World League standard.
 * ISNA: Islamic Society of North America standard.
 * Egypt: Egyptian General Authority of Survey standard.
 * KARACHI: University of Islamic Sciences (Karachi) standard.
 * MUHAMMADIYAH: Muhammadiyah organization (Indonesia) standard.
 */
public enum AngleCalculationType {

    MWL(18, 17),
    ISNA(15, 15),
    EGYPT(19.5, 17.5),
    KARACHI(18, 18),
    MUHAMMADIYAH(20, 18);

    private double fajr, isha;

    AngleCalculationType(double fajr, double isha) {
        this.fajr = fajr;
        this.isha = isha;
    }

    public double getFairAngle() {
        return fajr;
    }

    public double getIshaAngle() {
        return isha;
    }

}
