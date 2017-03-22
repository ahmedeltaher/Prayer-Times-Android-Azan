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
 * Ithna Ashari: Shia Ithna Ashari, Leva Research Institute, Qum.
 * TEHRAN: Institute of Geophysics, University of Tehran .
 * UOIF: Union des organisations islamiques de France .
 * Kuwait: University of Islamic Sciences, Karachi	.
 */
public enum AngleCalculationType {

    MWL(18, 17),
    ISNA(15, 15),
    EGYPT(19.5, 17.5),
    KARACHI(18, 18),
    MUHAMMADIYAH(20, 18),
    ITHNA_ASHARI(16, 14),
    TEHRAN(17.7, 14),
    UOIF(12, 12),
    KUWAIT(18, 17.5),
    UMM_AL_QURA(18.5, 19);

    private double fajr, isha;

    AngleCalculationType(double fajr, double isha) {
        this.fajr = fajr;
        this.isha = isha;
    }

    public double getFajrAngle() {
        return fajr;
    }

    public double getIshaAngle() {
        return isha;
    }

}
