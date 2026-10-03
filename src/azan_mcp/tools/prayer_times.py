"""Prayer times and Qibla tools — Epic 2 + server info tools — Epic 1."""
from __future__ import annotations

import logging
from datetime import date, datetime, timedelta
from zoneinfo import ZoneInfo
from azan_mcp.data.iran_cities import lookup_city, IRAN_CAPITALS
from mcp.server.fastmcp import FastMCP

from azan_mcp.config import UserConfig, get_config
from azan_mcp.prayer_engine import (
    METHOD_PARAMS,
    PolarDayError,
    PolarNightError,
    bearing_to_cardinal,
    compute_prayer_times,
    qibla_bearing,
)

logger = logging.getLogger(__name__)

# Yeroolee hundaa tartiiba qulqulluun tarreessuuf:
PRAYER_ORDER = ["fajr", "sunrise", "dhuhr", "asr", "sunset", "maghrib", "isha", "midnight"]
FARD_PRAYERS = ["fajr", "dhuhr", "asr", "maghrib", "isha"]


CONFIDENCE_KEY = "high=Bukhari/Muslim, medium=Abu Dawud/Tirmidhi/Nasai/Ibn Majah, low=weak chains"


def _meta(cfg: UserConfig | None = None) -> dict:
    cfg = cfg or get_config()
    return {
        "calculation_method": cfg.calculation_method.value,
        "madhab": cfg.madhab.value,
        "confidence_key": CONFIDENCE_KEY,
        "generated_at": datetime.now(ZoneInfo("UTC")).isoformat(),
    }


def _resolve(
        lat: float | None,
        lng: float | None,
        city: str | None = None
) -> tuple[float, float, str | None]:
    """Resolve coordinates via city name, explicit lat/lng, or default server configuration."""
    if city:
        match = lookup_city(city)
        if match:
            city_lat, city_lng, city_name = match
            return city_lat, city_lng, city_name
    cfg = get_config()
    final_lat = lat if lat is not None else cfg.latitude
    final_lng = lng if lng is not None else cfg.longitude
    return final_lat, final_lng, None


def _get_prayer_times_impl(
        date_str: str | None = None,
        latitude: float | None = None,
        longitude: float | None = None,
        city: str | None = None,
) -> dict:
    cfg = get_config()
    lat, lng, matched_city = _resolve(latitude, longitude, city)
    d = date.fromisoformat(date_str) if date_str else datetime.now(ZoneInfo(cfg.timezone)).date()
    times = compute_prayer_times(lat, lng, d, cfg.calculation_method.value, cfg.madhab.value, cfg.timezone)

    response = {
        "data": {k: v.isoformat() for k, v in times.items()},
        "meta": _meta(cfg),
        "location": {
            "latitude": lat,
            "longitude": lng,
            "matched_city": matched_city,
            "resolved_from": "city_database" if matched_city else (
                "explicit_coords" if latitude is not None else "server_default")
        }
    }
    return response


def _get_next_prayer_impl(latitude: float | None = None, longitude: float | None = None) -> dict:
    cfg = get_config()
    lat, lng = _resolve(latitude, longitude)
    tz = ZoneInfo(cfg.timezone)
    now = datetime.now(tz)
    times = compute_prayer_times(lat, lng, now.date(), cfg.calculation_method.value, cfg.madhab.value, cfg.timezone)
    for prayer in FARD_PRAYERS:
        if times[prayer] > now:
            return {
                "data": {"prayer": prayer.capitalize(), "time": times[prayer].isoformat()},
                "meta": _meta(cfg),
            }
    # Yeroon salaataa hundi yoo darbe — Subhii boruu deebisa
    tomorrow = now.date() + timedelta(days=1)
    times_tomorrow = compute_prayer_times(lat, lng, tomorrow, cfg.calculation_method.value, cfg.madhab.value, cfg.timezone)
    return {
        "data": {"prayer": "Fajr", "time": times_tomorrow["fajr"].isoformat()},
        "meta": _meta(cfg),
    }


def _get_time_until_next_prayer_impl(latitude: float | None = None, longitude: float | None = None) -> dict:
    cfg = get_config()
    tz = ZoneInfo(cfg.timezone)
    now = datetime.now(tz)
    next_p = _get_next_prayer_impl(latitude, longitude)
    next_time = datetime.fromisoformat(next_p["data"]["time"])
    delta = next_time - now
    total_minutes = int(delta.total_seconds() / 60)
    hours, mins = divmod(total_minutes, 60)
    if hours > 0:
        human = f"{hours} hour{'s' if hours != 1 else ''} {mins} minute{'s' if mins != 1 else ''}"
    else:
        human = f"{mins} minute{'s' if mins != 1 else ''}"
    return {
        "data": {
            "prayer": next_p["data"]["prayer"],
            "time": next_p["data"]["time"],
            "minutes_until": total_minutes,
            "human_readable": human,
        },
        "meta": _meta(cfg),
    }


def _get_qibla_direction_impl(latitude: float | None = None, longitude: float | None = None) -> dict:
    cfg = get_config()
    lat, lng = _resolve(latitude, longitude)
    bearing = qibla_bearing(lat, lng)
    return {
        "data": {
            "bearing": round(bearing, 2),
            "cardinal": bearing_to_cardinal(bearing),
            "description": f"Face {bearing_to_cardinal(bearing)} ({bearing:.1f}°) toward the Kaaba in Makkah.",
        },
        "meta": _meta(cfg),
    }


def _get_monthly_prayer_calendar_impl(
    year: int,
    month: int,
    latitude: float | None = None,
    longitude: float | None = None,
) -> dict:
    if not (1 <= month <= 12):
        raise ValueError(f"month must be 1–12, got {month}")
    cfg = get_config()
    lat, lng = _resolve(latitude, longitude)
    calendar = []
    d = date(year, month, 1)
    while d.month == month:
        try:
            times = compute_prayer_times(lat, lng, d, cfg.calculation_method.value, cfg.madhab.value, cfg.timezone)
            calendar.append({"date": d.isoformat(), **{k: v.isoformat() for k, v in times.items()}})
        except (PolarDayError, PolarNightError) as e:
            calendar.append({"date": d.isoformat(), "error": str(e)})
        d += timedelta(days=1)
    return {"data": calendar, "meta": _meta(cfg)}


def _get_sunnah_prayer_times_impl(
    date_str: str | None = None,
    latitude: float | None = None,
    longitude: float | None = None,
) -> dict:
    cfg = get_config()
    lat, lng = _resolve(latitude, longitude)
    d = date.fromisoformat(date_str) if date_str else datetime.now(ZoneInfo(cfg.timezone)).date()
    times = compute_prayer_times(lat, lng, d, cfg.calculation_method.value, cfg.madhab.value, cfg.timezone)
    sunrise = times["sunrise"]
    dhuhr   = times["dhuhr"]
    maghrib = times["maghrib"]
    isha    = times["isha"]
    fajr_tomorrow = compute_prayer_times(
        lat, lng, d + timedelta(days=1), cfg.calculation_method.value, cfg.madhab.value, cfg.timezone
    )["fajr"]
    duha_start  = sunrise + timedelta(minutes=20)
    duha_end    = dhuhr   - timedelta(minutes=15)
    awwabin_start = maghrib + timedelta(minutes=5)
    awwabin_end   = isha    - timedelta(minutes=15)
    night_len   = fajr_tomorrow - isha
    tahajjud_start = isha + timedelta(seconds=int(night_len.total_seconds() * 2 / 3))
    tahajjud_end   = fajr_tomorrow - timedelta(minutes=15)
    return {
        "data": {
            "duha": {
                "start": duha_start.isoformat(),
                "end":   duha_end.isoformat(),
                "description": "Ishraq/Duha prayer window (20 min after sunrise until 15 min before Dhuhr)",
                "source": "Sahih Muslim 748",
            },
            "awwabin": {
                "start": awwabin_start.isoformat(),
                "end":   awwabin_end.isoformat(),
                "description": "Awwabin — 6 rak'at after Maghrib before Isha. Equivalent to 12 years of worship (Tirmidhi 435).",
                "source": "Tirmidhi 435",
            },
            "tahajjud": {
                "start": tahajjud_start.isoformat(),
                "end":   tahajjud_end.isoformat(),
                "description": "Tahajjud window (last third of night, between Isha and Fajr)",
                "source": "Sahih Muslim 1163",
            },
        },
        "meta": {**_meta(cfg), "source": "Fiqh-derived from Fajr/Sunrise/Maghrib/Dhuhr/Isha times"},
    }


def _get_server_info_impl() -> dict:
    cfg = get_config()
    return {
        "data": {
            "latitude":           cfg.latitude,
            "longitude":          cfg.longitude,
            "timezone":           cfg.timezone,
            "calculation_method": cfg.calculation_method.value,
            "madhab":             cfg.madhab.value,
            "iqama_offsets":      cfg.iqama_offsets,
        },
        "meta": {"generated_at": datetime.now(ZoneInfo("UTC")).isoformat()},
    }


def _list_calculation_methods_impl() -> dict:
    methods = []
    for method_id, params in METHOD_PARAMS.items():
        entry: dict = {"id": method_id, "fajr_angle": params.fajr_angle}
        if getattr(params, "maghrib_angle", None) is not None:
            entry["maghrib_angle"] = params.maghrib_angle
        if params.isha_angle is not None:
            entry["isha_angle"] = params.isha_angle
        if params.isha_minutes is not None:
            entry["isha_minutes_after_maghrib"] = params.isha_minutes
        if params.isha_minutes_ramadan is not None:
            entry["isha_minutes_ramadan"] = params.isha_minutes_ramadan
        methods.append(entry)
    return {
        "data": methods,
        "meta": {"generated_at": datetime.now(ZoneInfo("UTC")).isoformat()},
    }


def register(mcp: FastMCP) -> None:

    @mcp.tool()
    def get_server_info() -> dict:
        """Return the currently active server configuration: location, timezone, calculation method, madhab, and iqama offsets."""
        return _get_server_info_impl()

    @mcp.tool()
    def list_calculation_methods() -> dict:
        """List all supported prayer time calculation methods with their Fajr, Maghrib, and Isha angles."""
        return _list_calculation_methods_impl()

    @mcp.tool()
    def get_prayer_times(
            date: str | None = None,
            city: str | None = None,
            latitude: float | None = None,
            longitude: float | None = None,
    ) -> dict:
        """
        Return daily prayer times (including Sunset and Midnight) for a given location and date.

        Parameters:
        - date: ISO 8601 string (YYYY-MM-DD), defaults to today.
        - city: Name of an Iranian provincial capital in Persian or common English spellings
                (e.g., 'Tehran', 'Isfahan', 'Esfahan', 'مشهد', 'Shiraz', 'Tabriz').
        - latitude / longitude: Optional coordinates.

        Important instructions for Model:
        If the user asks for an Iranian city that is NOT a provincial capital (e.g., Kashan, Kish, Babol),
        the model should estimate its coordinates and pass them to latitude/longitude, or pass the nearest
        provincial capital in the city parameter.
        """
        return _get_prayer_times_impl(date, latitude, longitude, city)

    @mcp.tool()
    def get_next_prayer(
        latitude: float | None = None,
        longitude: float | None = None,
    ) -> dict:
        """Return the name and time of the next upcoming prayer from right now."""
        return _get_next_prayer_impl(latitude, longitude)

    @mcp.tool()
    def get_time_until_next_prayer(
        latitude: float | None = None,
        longitude: float | None = None,
    ) -> dict:
        """Return minutes until the next prayer and a human-readable countdown string (e.g. '2 hours 15 minutes')."""
        return _get_time_until_next_prayer_impl(latitude, longitude)

    @mcp.tool()
    def get_qibla_direction(
        latitude: float | None = None,
        longitude: float | None = None,
    ) -> dict:
        """Return the Qibla bearing in degrees and 16-point cardinal direction toward the Kaaba in Makkah."""
        return _get_qibla_direction_impl(latitude, longitude)

    @mcp.tool()
    def get_monthly_prayer_calendar(
        year: int,
        month: int,
        latitude: float | None = None,
        longitude: float | None = None,
    ) -> dict:
        """Return a full month's prayer schedule including Sunset and Midnight. month is 1-12."""
        return _get_monthly_prayer_calendar_impl(year, month, latitude, longitude)

    @mcp.tool()
    def get_sunnah_prayer_times(
        date: str | None = None,
        latitude: float | None = None,
        longitude: float | None = None,
    ) -> dict:
        """Return Duha (Ishraq) and Tahajjud time windows for a given date."""
        return _get_sunnah_prayer_times_impl(date, latitude, longitude)