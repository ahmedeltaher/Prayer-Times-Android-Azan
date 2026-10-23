"""Zakat and Islamic calculator tools — Epic 6."""
from __future__ import annotations

import logging
import math
from datetime import date, datetime, timedelta
from fractions import Fraction
from zoneinfo import ZoneInfo

from mcp.server.fastmcp import FastMCP

from azan_mcp.config import get_config
from azan_mcp.prayer_engine import compute_prayer_times

logger = logging.getLogger(__name__)

ZAKAT_RATE = Fraction(1, 40)  # 2.5%
GOLD_NISAB_GRAMS   = 85
SILVER_NISAB_GRAMS = 595
SCHOLAR_DISCLAIMER = "Consult a qualified Islamic scholar for your specific situation."


def _meta(source: str = "", extra: dict | None = None) -> dict:
    m: dict = {
        "generated_at": datetime.now(ZoneInfo("UTC")).isoformat(),
        "disclaimer":   SCHOLAR_DISCLAIMER,
        "confidence_key": "high=Bukhari/Muslim, medium=Abu Dawud/Tirmidhi/Nasai/Ibn Majah, low=weak chains",
    }
    if source:
        m["source"] = source
    if extra:
        m.update(extra)
    return m


def _calculate_zakat_impl(
    wealth_type: str,
    amount: float,
    currency: str = "USD",
    gold_price_per_gram: float | None = None,
    silver_price_per_gram: float | None = None,
) -> dict:
    valid_types = ("cash", "gold", "silver", "trade_goods")
    if wealth_type not in valid_types:
        raise ValueError(f"wealth_type must be one of: {', '.join(valid_types)}. Got: {wealth_type!r}")
    if not math.isfinite(amount) or amount <= 0:
        raise ValueError(f"amount must be a finite positive value, got {amount}")
    for price_name, price in (
        ("gold_price_per_gram", gold_price_per_gram),
        ("silver_price_per_gram", silver_price_per_gram),
    ):
        if price is not None and (not math.isfinite(price) or price <= 0):
            raise ValueError(f"{price_name} must be a finite positive value, got {price}")

    nisab_value: float
    nisab_basis: str

    if wealth_type == "gold":
        if gold_price_per_gram is None:
            raise ValueError("gold_price_per_gram is required for wealth_type='gold'")
        nisab_value = gold_price_per_gram * GOLD_NISAB_GRAMS
        nisab_basis = "gold"
    elif wealth_type == "silver":
        if silver_price_per_gram is None:
            raise ValueError("silver_price_per_gram is required for wealth_type='silver'")
        nisab_value = silver_price_per_gram * SILVER_NISAB_GRAMS
        nisab_basis = "silver"
    else:
        # cash / trade_goods: prefer silver nisab (more inclusive)
        if silver_price_per_gram is not None:
            nisab_value = silver_price_per_gram * SILVER_NISAB_GRAMS
            nisab_basis = "silver"
        elif gold_price_per_gram is not None:
            nisab_value = gold_price_per_gram * GOLD_NISAB_GRAMS
            nisab_basis = "gold"
        else:
            raise ValueError(
                "Provide gold_price_per_gram or silver_price_per_gram to determine the nisab threshold."
            )

    nisab_met = amount >= nisab_value
    zakat_due = round(float(Fraction(amount).limit_denominator(10_000_000) * ZAKAT_RATE), 2) if nisab_met else 0.0

    return {
        "data": {
            "zakat_due":       zakat_due,
            "nisab_met":       nisab_met,
            "nisab_threshold": round(nisab_value, 2),
            "nisab_basis":     nisab_basis,
            "zakat_rate":      "2.5%",
            "currency":        currency,
        },
        "meta": _meta("Fiqh standard: 2.5% on wealth held for one lunar year (hawl) above nisab"),
    }


def _calculate_gold_nisab_impl(gold_price_per_gram: float, currency: str = "USD") -> dict:
    if gold_price_per_gram <= 0:
        raise ValueError("gold_price_per_gram must be positive")
    return {
        "data": {
            "nisab_grams":    GOLD_NISAB_GRAMS,
            "current_value":  round(GOLD_NISAB_GRAMS * gold_price_per_gram, 2),
            "currency":       currency,
        },
        "meta": _meta("85g gold = nisab threshold (majority scholarly opinion: Hanafi, Maliki, Shafi'i, Hanbali)"),
    }


def _calculate_silver_nisab_impl(silver_price_per_gram: float, currency: str = "USD") -> dict:
    if silver_price_per_gram <= 0:
        raise ValueError("silver_price_per_gram must be positive")
    return {
        "data": {
            "nisab_grams":   SILVER_NISAB_GRAMS,
            "current_value": round(SILVER_NISAB_GRAMS * silver_price_per_gram, 2),
            "currency":      currency,
        },
        "meta": _meta(
            "595g silver = nisab threshold",
            {"note": "Silver nisab is typically lower than gold nisab today. Some scholars recommend silver nisab to be more inclusive."},
        ),
    }


def _calculate_fasting_times_impl(
    start_date: str,
    end_date: str,
    latitude: float | None = None,
    longitude: float | None = None,
) -> dict:
    cfg = get_config()
    start = date.fromisoformat(start_date)
    end   = date.fromisoformat(end_date)
    if end < start:
        raise ValueError("end_date must be on or after start_date")
    if (end - start).days > 30:
        raise ValueError("Date range must not exceed 31 days")
    lat = latitude if latitude is not None else cfg.latitude
    lng = longitude if longitude is not None else cfg.longitude
    results = []
    current = start
    while current <= end:
        times = compute_prayer_times(lat, lng, current, cfg.calculation_method.value, cfg.madhab.value, cfg.timezone)
        fajr    = times["fajr"]
        maghrib = times["maghrib"]
        fasting_hours = round((maghrib - fajr).total_seconds() / 3600, 2)
        results.append({
            "date":           current.isoformat(),
            "suhoor_ends":    fajr.isoformat(),
            "iftar_begins":   maghrib.isoformat(),
            "fasting_hours":  fasting_hours,
        })
        current += timedelta(days=1)
    return {
        "data": results,
        "meta": _meta("Suhoor ends at Fajr; Iftar begins at Maghrib."),
    }


def _calculate_inheritance_basic_impl(
    estate_value: float,
    currency: str = "USD",
    spouse: str | None = None,
    sons: int = 0,
    daughters: int = 0,
    father: bool = False,
    mother: bool = False,
) -> dict:
    if estate_value <= 0:
        raise ValueError("estate_value must be positive")
    if sons < 0 or daughters < 0:
        raise ValueError("sons and daughters must be non-negative integers")
    if spouse is not None and spouse not in ("husband", "wife"):
        raise ValueError("spouse must be 'husband', 'wife', or None")

    has_children = sons > 0 or daughters > 0
    shares: dict[str, Fraction] = {}

    if spouse == "husband":
        shares["husband"] = Fraction(1, 4) if has_children else Fraction(1, 2)
    elif spouse == "wife":
        shares["wife"] = Fraction(1, 8) if has_children else Fraction(1, 4)

    if mother:
        shares["mother"] = Fraction(1, 6) if has_children else Fraction(1, 3)

    if father:
        shares["father"] = Fraction(1, 6)

    # Fixed daughter shares (when no sons)
    if daughters > 0 and sons == 0:
        shares["daughters"] = Fraction(1, 2) if daughters == 1 else Fraction(2, 3)

    # Residue calculation
    fixed_total = sum(shares.values())
    residue = Fraction(1) - fixed_total

    # Sons + daughters (ta'seeb): residue split 2:1
    if sons > 0 or (daughters > 0 and sons > 0):
        if sons > 0:
            total_parts = sons * 2 + daughters
            if total_parts > 0 and residue > 0:
                per_son_share   = residue * Fraction(2, total_parts)
                per_daughter_share = residue * Fraction(1, total_parts)
                if sons > 0:
                    shares[f"sons (×{sons})"] = per_son_share * sons
                if daughters > 0:
                    shares[f"daughters (×{daughters})"] = per_daughter_share * daughters

    # Father residue when no children
    if father and not has_children:
        shares["father"] = Fraction(1) - sum(v for k, v in shares.items() if k != "father")

    # Aul: proportional reduction if total > 1
    total = sum(shares.values())
    aul_applied = total > 1
    if aul_applied:
        shares = {k: v / total for k, v in shares.items()}

    heirs = []
    distributed = Fraction(0)
    for relation, share in shares.items():
        amount = float(share) * estate_value
        heirs.append({
            "relation": relation,
            "share":    f"{share.numerator}/{share.denominator}",
            "amount":   round(amount, 2),
        })
        distributed += share

    return {
        "data": {
            "heirs":             heirs,
            "total_distributed": round(float(distributed) * estate_value, 2),
            "currency":          currency,
            "aul_applied":       aul_applied,
        },
        "meta": _meta(
            "Quran 4:11-12, 4:176",
            {"note": "Simplified Faraid (fixed shares only). Consult a qualified Islamic scholar for actual inheritance matters."},
        ),
    }


def register(mcp: FastMCP) -> None:

    @mcp.tool()
    def calculate_zakat(
        wealth_type: str,
        amount: float,
        currency: str = "USD",
        gold_price_per_gram: float | None = None,
        silver_price_per_gram: float | None = None,
    ) -> dict:
        """Calculate Zakat due on wealth. wealth_type: cash, gold, silver, trade_goods. Provide gold or silver price per gram to determine nisab."""
        return _calculate_zakat_impl(wealth_type, amount, currency, gold_price_per_gram, silver_price_per_gram)

    @mcp.tool()
    def calculate_gold_nisab(gold_price_per_gram: float, currency: str = "USD") -> dict:
        """Return the Zakat Nisab threshold based on 85 grams of gold at the given price."""
        return _calculate_gold_nisab_impl(gold_price_per_gram, currency)

    @mcp.tool()
    def calculate_silver_nisab(silver_price_per_gram: float, currency: str = "USD") -> dict:
        """Return the Zakat Nisab threshold based on 595 grams of silver at the given price."""
        return _calculate_silver_nisab_impl(silver_price_per_gram, currency)

    @mcp.tool()
    def calculate_fasting_times(
        start_date: str,
        end_date: str,
        latitude: float | None = None,
        longitude: float | None = None,
    ) -> dict:
        """Return Suhoor end (Fajr) and Iftar start (Maghrib) for each day in a date range (max 31 days). Dates in ISO 8601 format."""
        return _calculate_fasting_times_impl(start_date, end_date, latitude, longitude)

    @mcp.tool()
    def calculate_inheritance_basic(
        estate_value: float,
        currency: str = "USD",
        spouse: str | None = None,
        sons: int = 0,
        daughters: int = 0,
        father: bool = False,
        mother: bool = False,
    ) -> dict:
        """Calculate basic Faraid (Islamic inheritance) fixed shares. spouse: 'husband', 'wife', or None. Returns share fractions and amounts per heir."""
        return _calculate_inheritance_basic_impl(estate_value, currency, spouse, sons, daughters, father, mother)
