"""Islamic calendar tools — Epic 4."""
from __future__ import annotations

import logging
from calendar import monthrange
from datetime import date, datetime, timedelta
from zoneinfo import ZoneInfo

from mcp.server.fastmcp import FastMCP

from azan_mcp.data.islamic_events import ISLAMIC_EVENTS

logger = logging.getLogger(__name__)

_CALENDAR_META = {
    "source": "hijridate package (tabular Islamic calendar, civil variant)",
    "confidence": "medium",
    "confidence_key": "high=Bukhari/Muslim, medium=Abu Dawud/Tirmidhi/Nasai/Ibn Majah, low=weak chains",
    "note": "Tabular calendar is an approximation. Actual month starts depend on moon sighting.",
    "disclaimer": "Consult a qualified Islamic authority for moon-sighting-based dates.",
}

HIJRI_MONTHS = [
    "Muharram", "Safar", "Rabi al-Awwal", "Rabi al-Thani",
    "Jumada al-Ula", "Jumada al-Akhirah", "Rajab", "Sha'ban",
    "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah",
]


def _meta() -> dict:
    return {
        **_CALENDAR_META,
        "generated_at": datetime.now(ZoneInfo("UTC")).isoformat(),
    }


def _hijri_to_dict(h) -> dict:  # type: ignore[no-untyped-def]
    return {
        "hijri_year":       h.year,
        "hijri_month":      h.month,
        "hijri_month_name": HIJRI_MONTHS[h.month - 1],
        "hijri_day":        h.day,
        "formatted":        f"{h.day} {HIJRI_MONTHS[h.month - 1]} {h.year} AH",
    }


def _get_hijri_date_impl(date_str: str | None = None) -> dict:
    from hijridate import Gregorian  
    d = date.fromisoformat(date_str) if date_str else date.today()
    h = Gregorian(d.year, d.month, d.day).to_hijri()
    return {"data": _hijri_to_dict(h), "meta": _meta()}


def _convert_gregorian_to_hijri_impl(year: int, month: int, day: int) -> dict:
    from hijridate import Gregorian  
    h = Gregorian(year, month, day).to_hijri()
    return {"data": _hijri_to_dict(h), "meta": _meta()}


def _convert_hijri_to_gregorian_impl(hijri_year: int, hijri_month: int, hijri_day: int) -> dict:
    from hijridate import Hijri  
    if not (1 <= hijri_month <= 12):
        raise ValueError(f"hijri_month must be 1–12, got {hijri_month}")
    if not (1 <= hijri_day <= 30):
        raise ValueError(f"hijri_day must be 1–30, got {hijri_day}")
    g = Hijri(hijri_year, hijri_month, hijri_day).to_gregorian()
    iso = f"{g.year:04d}-{g.month:02d}-{g.day:02d}"
    return {
        "data": {"year": g.year, "month": g.month, "day": g.day, "iso": iso},
        "meta": _meta(),
    }


def _get_islamic_events_impl(hijri_year: int, hijri_month: int | None = None) -> dict:
    from hijridate import Hijri  
    events = [e for e in ISLAMIC_EVENTS if hijri_month is None or e.hijri_month == hijri_month]
    result = []
    for event in events:
        try:
            g = Hijri(hijri_year, event.hijri_month, event.hijri_day).to_gregorian()
            gregorian_date = f"{g.year:04d}-{g.month:02d}-{g.day:02d}"
        except Exception:
            gregorian_date = None
        result.append({
            "name":             event.name,
            "hijri_date":       f"{event.hijri_day} {HIJRI_MONTHS[event.hijri_month - 1]} {hijri_year} AH",
            "gregorian_date":   gregorian_date,
            "is_public_holiday": event.is_public_holiday,
        })
    return {"data": result, "meta": _meta()}


def _get_ramadan_dates_impl(gregorian_year: int) -> dict:
    from hijridate import Hijri  
    # Estimate Hijri year(s) that could overlap with the Gregorian year
    approx = gregorian_year - 622 + round((gregorian_year - 622) / 32.5)
    for hijri_year in range(approx - 1, approx + 3):
        try:
            g_start = Hijri(hijri_year, 9, 1).to_gregorian()
            if g_start.year == gregorian_year:
                g_end = Hijri(hijri_year, 10, 1).to_gregorian() - timedelta(days=1)
                return {
                    "data": {
                        "start":      f"{g_start.year:04d}-{g_start.month:02d}-{g_start.day:02d}",
                        "end":        f"{g_end.year:04d}-{g_end.month:02d}-{g_end.day:02d}",
                        "hijri_year": hijri_year,
                    },
                    "meta": _meta(),
                }
        except Exception:
            continue
    raise ValueError(f"Could not determine Ramadan dates for Gregorian year {gregorian_year}")


def _get_eid_dates_impl(gregorian_year: int) -> dict:
    from hijridate import Hijri  
    approx = gregorian_year - 622 + round((gregorian_year - 622) / 32.5)
    eid_fitr = eid_adha = None
    for hijri_year in range(approx - 1, approx + 3):
        try:
            g_fitr = Hijri(hijri_year, 10, 1).to_gregorian()
            if g_fitr.year == gregorian_year and eid_fitr is None:
                eid_fitr = f"{g_fitr.year:04d}-{g_fitr.month:02d}-{g_fitr.day:02d}"
            g_adha = Hijri(hijri_year, 12, 10).to_gregorian()
            if g_adha.year == gregorian_year and eid_adha is None:
                eid_adha = f"{g_adha.year:04d}-{g_adha.month:02d}-{g_adha.day:02d}"
        except Exception:
            continue
    if not eid_fitr or not eid_adha:
        raise ValueError(f"Could not determine Eid dates for Gregorian year {gregorian_year}")
    return {
        "data": {"eid_al_fitr": eid_fitr, "eid_al_adha": eid_adha},
        "meta": _meta(),
    }


def _get_voluntary_fasting_dates_impl(gregorian_year: int, gregorian_month: int) -> dict:
    from hijridate import Gregorian  
    if not (1 <= gregorian_month <= 12):
        raise ValueError(f"gregorian_month must be 1–12, got {gregorian_month}")
    _, days_in_month = monthrange(gregorian_year, gregorian_month)
    results: list[dict] = []
    for day in range(1, days_in_month + 1):
        d = date(gregorian_year, gregorian_month, day)
        h = Gregorian(d.year, d.month, d.day).to_hijri()
        reasons = []
        # Mondays and Thursdays
        if d.weekday() == 0:
            reasons.append("Monday (Sunnah fast)")
        if d.weekday() == 3:
            reasons.append("Thursday (Sunnah fast)")
        # Ayyam al-Beed: 13, 14, 15 of Hijri month
        if h.day in (13, 14, 15):
            reasons.append(f"Ayyam al-Beed (Day {h.day} of Hijri month — white days)")
        # Day of Arafah: 9 Dhu al-Hijjah
        if h.month == 12 and h.day == 9:
            reasons.append("Day of Arafah (9 Dhu al-Hijjah) — expiates two years of sins")
        # 6 days of Shawwal (1 Shawwal is Eid — not fasted; days 2–7 commonly)
        if h.month == 10 and 2 <= h.day <= 7:
            reasons.append(f"6 days of Shawwal (Day {h.day - 1} of 6)")
        if reasons:
            results.append({
                "gregorian_date": d.isoformat(),
                "hijri_date": f"{h.day} {HIJRI_MONTHS[h.month - 1]} {h.year} AH",
                "day_of_week": d.strftime("%A"),
                "reasons": reasons,
            })
    return {
        "data": results,
        "meta": {
            **_meta(),
            "source": "Sahih Muslim 1162 (Mon/Thu), Bukhari 1981 (Ayyam al-Beed), Muslim 1162 (Arafah), Muslim 1164 (6 Shawwal)",
        },
    }


def register(mcp: FastMCP) -> None:

    @mcp.tool()
    def get_hijri_date(date: str | None = None) -> dict:
        """Return today's (or a given ISO date's) equivalent in the Islamic Hijri calendar."""
        return _get_hijri_date_impl(date)

    @mcp.tool()
    def convert_gregorian_to_hijri(year: int, month: int, day: int) -> dict:
        """Convert a Gregorian calendar date to its Hijri equivalent."""
        return _convert_gregorian_to_hijri_impl(year, month, day)

    @mcp.tool()
    def convert_hijri_to_gregorian(hijri_year: int, hijri_month: int, hijri_day: int) -> dict:
        """Convert a Hijri calendar date to its Gregorian equivalent."""
        return _convert_hijri_to_gregorian_impl(hijri_year, hijri_month, hijri_day)

    @mcp.tool()
    def get_islamic_events(hijri_year: int, hijri_month: int | None = None) -> dict:
        """Return named Islamic events for a Hijri year. Optionally filter by hijri_month (1–12)."""
        return _get_islamic_events_impl(hijri_year, hijri_month)

    @mcp.tool()
    def get_ramadan_dates(gregorian_year: int) -> dict:
        """Return the Gregorian start and end dates of Ramadan for a given year."""
        return _get_ramadan_dates_impl(gregorian_year)

    @mcp.tool()
    def get_eid_dates(gregorian_year: int) -> dict:
        """Return the Gregorian dates of Eid al-Fitr and Eid al-Adha for a given year."""
        return _get_eid_dates_impl(gregorian_year)

    @mcp.tool()
    def get_voluntary_fasting_dates(gregorian_year: int, gregorian_month: int) -> dict:
        """Return all recommended voluntary fasting days in a given Gregorian month: Mondays/Thursdays, Ayyam al-Beed, Day of Arafah, 6 days of Shawwal."""
        return _get_voluntary_fasting_dates_impl(gregorian_year, gregorian_month)
