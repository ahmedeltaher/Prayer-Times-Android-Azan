from __future__ import annotations

import json
import os
from dataclasses import dataclass, field
from enum import Enum
from zoneinfo import ZoneInfo, ZoneInfoNotFoundError


class CalculationMethod(str, Enum):
    UMM_AL_QURA  = "umm_al_qura"
    MUHAMMADIYAH = "muhammadiyah"
    MWL          = "mwl"
    EGYPT        = "egypt"
    KARACHI      = "karachi"
    ISNA         = "isna"
    ITHNA_ASHARI = "ithna_ashari"
    TEHRAN       = "tehran"
    UOIF         = "uoif"
    KUWAIT       = "kuwait"


class Madhab(str, Enum):
    HANAFI = "hanafi"
    SHAFI  = "shafi"


@dataclass
class UserConfig:
    latitude: float
    longitude: float
    timezone: str
    calculation_method: CalculationMethod = CalculationMethod.MWL
    madhab: Madhab = Madhab.SHAFI
    iqama_offsets: dict[str, int] = field(default_factory=lambda: {
        "fajr": 20, "dhuhr": 10, "asr": 10, "maghrib": 5, "isha": 15,
    })

    def __post_init__(self) -> None:
        try:
            ZoneInfo(self.timezone)
        except ZoneInfoNotFoundError:
            raise ValueError(f"Invalid IANA timezone: {self.timezone!r}")
        if not (-90 <= self.latitude <= 90):
            raise ValueError(f"Latitude must be -90 to 90, got {self.latitude}")
        if not (-180 <= self.longitude <= 180):
            raise ValueError(f"Longitude must be -180 to 180, got {self.longitude}")
        # Coerce string enums from JSON
        if isinstance(self.calculation_method, str):
            self.calculation_method = CalculationMethod(self.calculation_method)
        if isinstance(self.madhab, str):
            self.madhab = Madhab(self.madhab)


DEFAULT_CONFIG = UserConfig(
    latitude=35.6892,
    longitude=51.3890,
    timezone="Asia/Tehran",
    calculation_method=CalculationMethod.TEHRAN,
    madhab=Madhab.SHAFI,
)

_config: UserConfig | None = None


def load_config(path: str | None = None) -> UserConfig:
    global _config
    if _config is not None:
        return _config
    config_path = path or os.environ.get("AZAN_CONFIG")
    if not config_path:
        _config = DEFAULT_CONFIG
        return _config
    with open(config_path, encoding="utf-8") as f:
        data = json.load(f)
    _config = UserConfig(**data)
    return _config


def get_config() -> UserConfig:
    return load_config()


def reset_config() -> None:
    """Reset cached config — used in tests."""
    global _config
    _config = None
