from __future__ import annotations

import pytest

from azan_mcp.tools.calculators import (
    _calculate_fasting_times_impl,
    _calculate_gold_nisab_impl,
    _calculate_inheritance_basic_impl,
    _calculate_silver_nisab_impl,
    _calculate_zakat_impl,
)


def test_zakat_cash_above_nisab():
    result = _calculate_zakat_impl("cash", 10_000, gold_price_per_gram=60.0)
    d = result["data"]
    assert d["nisab_met"] is True
    assert abs(d["zakat_due"] - 250.0) < 1.0, f"Expected ~250, got {d['zakat_due']}"
    assert d["zakat_rate"] == "2.5%"


def test_zakat_cash_below_nisab():
    result = _calculate_zakat_impl("cash", 100, silver_price_per_gram=0.85)
    d = result["data"]
    assert d["nisab_met"] is False
    assert d["zakat_due"] == 0


def test_zakat_silver_prefers_silver_nisab():
    result = _calculate_zakat_impl("cash", 5_000, gold_price_per_gram=60.0, silver_price_per_gram=0.85)
    d = result["data"]
    assert d["nisab_basis"] == "silver"


def test_zakat_gold_type():
    result = _calculate_zakat_impl("gold", 200, gold_price_per_gram=60.0)
    d = result["data"]
    assert d["nisab_basis"] == "gold"
    assert d["nisab_threshold"] == pytest.approx(85 * 60.0, abs=1)


def test_zakat_missing_price_raises():
    with pytest.raises(ValueError, match="gold_price_per_gram or silver_price_per_gram"):
        _calculate_zakat_impl("cash", 5_000)


@pytest.mark.parametrize(
    ("wealth_type", "prices"),
    [
        ("cash", {"silver_price_per_gram": -1.0}),
        ("gold", {"gold_price_per_gram": 0.0}),
        ("silver", {"silver_price_per_gram": float("nan")}),
    ],
)
def test_zakat_rejects_nonpositive_or_nonfinite_nisab_prices(wealth_type, prices):
    with pytest.raises(ValueError, match="price_per_gram must be a finite positive value"):
        _calculate_zakat_impl(wealth_type, 100.0, **prices)


def test_zakat_invalid_type_raises():
    with pytest.raises(ValueError, match="wealth_type"):
        _calculate_zakat_impl("livestock", 1_000, gold_price_per_gram=60.0)


def test_gold_nisab():
    result = _calculate_gold_nisab_impl(60.0)
    d = result["data"]
    assert d["nisab_grams"] == 85
    assert d["current_value"] == pytest.approx(5100.0, abs=1)


def test_silver_nisab():
    result = _calculate_silver_nisab_impl(0.85)
    d = result["data"]
    assert d["nisab_grams"] == 595
    assert d["current_value"] == pytest.approx(595 * 0.85, abs=1)


def test_fasting_times_makkah_ramadan():
    result = _calculate_fasting_times_impl("2024-03-12", "2024-03-14")
    days = result["data"]
    assert len(days) == 3
    for day in days:
        assert 12.0 <= day["fasting_hours"] <= 16.0, f"Unexpected fasting hours: {day['fasting_hours']}"
        assert day["suhoor_ends"] < day["iftar_begins"]


def test_fasting_times_range_limit():
    with pytest.raises(ValueError, match="31 days"):
        _calculate_fasting_times_impl("2024-01-01", "2024-03-01")


def test_fasting_times_invalid_range():
    with pytest.raises(ValueError, match="end_date"):
        _calculate_fasting_times_impl("2024-03-14", "2024-03-12")


def test_inheritance_spouse_and_children():
    result = _calculate_inheritance_basic_impl(10_000, spouse="wife", sons=2, daughters=1)
    d = result["data"]
    assert d["total_distributed"] == pytest.approx(10_000, abs=1)
    relations = [h["relation"] for h in d["heirs"]]
    assert "wife" in relations


def test_inheritance_widow_no_children():
    result = _calculate_inheritance_basic_impl(10_000, spouse="wife")
    d = result["data"]
    wife = next(h for h in d["heirs"] if h["relation"] == "wife")
    assert wife["share"] == "1/4"


def test_inheritance_aul_applied():
    """Scenario where fixed shares exceed estate triggers Aul."""
    result = _calculate_inheritance_basic_impl(
        10_000, spouse="wife", daughters=2, father=True, mother=True
    )
    d = result["data"]
    assert d["aul_applied"] is True
    assert d["total_distributed"] == pytest.approx(10_000, abs=1)


def test_inheritance_invalid_estate():
    with pytest.raises(ValueError, match="estate_value"):
        _calculate_inheritance_basic_impl(-1_000)
