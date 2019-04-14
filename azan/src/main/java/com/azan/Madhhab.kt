package com.azan

/**
 * Madhhab is used for Assr prayer calculation.
 */
class Madhhab {
    companion object {
        /**
         * Assr prayer shadow ratio: use Shaa'fi mathhab (default)
         */
        val SHAAFI = Madhhab()

        /**
         * Assr prayer shadow ratio: use Hanafi mathhab
         */
        val HANAFI = Madhhab()
    }
}
