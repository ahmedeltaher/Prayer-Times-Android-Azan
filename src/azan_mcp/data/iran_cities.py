"""Coordinates and aliases for provincial capitals of Iran."""
from __future__ import annotations

import re

# مختصات رسمی ۳۱ مرکز استان ایران
IRAN_CAPITALS: dict[str, dict] = {
    "tehran": {"name_fa": "تهران", "lat": 35.6892, "lng": 51.3890},
    "mashhad": {"name_fa": "مشهد", "lat": 36.2605, "lng": 59.6168},
    "isfahan": {"name_fa": "اصفهان", "lat": 32.6546, "lng": 51.6680},
    "shiraz": {"name_fa": "شیراز", "lat": 29.5918, "lng": 52.5837},
    "tabriz": {"name_fa": "تبریز", "lat": 38.0962, "lng": 46.2738},
    "karaj": {"name_fa": "کرج", "lat": 35.8400, "lng": 50.9391},
    "ahvaz": {"name_fa": "اهواز", "lat": 31.3183, "lng": 48.6706},
    "qom": {"name_fa": "قم", "lat": 34.6401, "lng": 50.8764},
    "kermanshah": {"name_fa": "کرمانشاه", "lat": 34.3142, "lng": 47.0650},
    "urmia": {"name_fa": "ارومیه", "lat": 37.5527, "lng": 45.0761},
    "rasht": {"name_fa": "رشت", "lat": 37.2808, "lng": 49.5832},
    "zahedan": {"name_fa": "زاهدان", "lat": 29.4963, "lng": 60.8629},
    "hamedan": {"name_fa": "همدان", "lat": 34.7982, "lng": 48.5146},
    "kerman": {"name_fa": "کرمان", "lat": 30.2839, "lng": 57.0834},
    "yazd": {"name_fa": "یزد", "lat": 31.8974, "lng": 54.3569},
    "ardabil": {"name_fa": "اردبیل", "lat": 38.2498, "lng": 48.2933},
    "bandar_abbas": {"name_fa": "بندرعباس", "lat": 27.1832, "lng": 56.2666},
    "arak": {"name_fa": "اراک", "lat": 34.0954, "lng": 49.7013},
    "zanjan": {"name_fa": "زنجان", "lat": 36.6736, "lng": 48.4787},
    "sanandaj": {"name_fa": "سنندج", "lat": 35.3140, "lng": 46.9923},
    "qazvin": {"name_fa": "قزوین", "lat": 36.2797, "lng": 50.0049},
    "khorramabad": {"name_fa": "خرم‌آباد", "lat": 33.4878, "lng": 48.3558},
    "gorgan": {"name_fa": "گرگان", "lat": 36.8427, "lng": 54.4439},
    "sari": {"name_fa": "ساری", "lat": 36.5659, "lng": 53.0586},
    "bojnord": {"name_fa": "بجنورد", "lat": 37.4747, "lng": 57.3290},
    "birjand": {"name_fa": "بیرجند", "lat": 32.8663, "lng": 59.2211},
    "bushehr": {"name_fa": "بوشهر", "lat": 28.9234, "lng": 50.8203},
    "shahrekord": {"name_fa": "شهرکرد", "lat": 32.3256, "lng": 50.8644},
    "semnan": {"name_fa": "سمنان", "lat": 35.5729, "lng": 53.3971},
    "yasuj": {"name_fa": "یاسوج", "lat": 30.6684, "lng": 51.5876},
    "ilam": {"name_fa": "ایلام", "lat": 33.6374, "lng": 46.4227},
}

# نگاشت املای متداول انگلیسی و فارسی به کلید استاندارد
CITY_ALIASES: dict[str, str] = {
    # Tehran
    "tehran": "tehran", "teheran": "tehran", "تهران": "tehran",
    # Mashhad
    "mashhad": "mashhad", "meshed": "mashhad", "mashad": "mashhad", "مشهد": "mashhad",
    # Isfahan
    "isfahan": "isfahan", "esfahan": "isfahan", "ispahan": "isfahan", "اصفهان": "isfahan",
    # Shiraz
    "shiraz": "shiraz", "شیراز": "shiraz",
    # Tabriz
    "tabriz": "tabriz", "تبریز": "tabriz",
    # Karaj
    "karaj": "karaj", "کرج": "karaj",
    # Ahvaz
    "ahvaz": "ahvaz", "ahwaz": "ahvaz", "اهواز": "ahvaz",
    # Qom
    "qom": "qom", "ghom": "qom", "qum": "qom", "قم": "qom",
    # Kermanshah
    "kermanshah": "kermanshah", "bakhtaran": "kermanshah", "کرمانشاه": "kermanshah",
    # Urmia
    "urmia": "urmia", "orumieh": "urmia", "orumiyeh": "urmia", "urmiya": "urmia", "ارومیه": "urmia",
    # Rasht
    "rasht": "rasht", "رشت": "rasht",
    # Zahedan
    "zahedan": "zahedan", "زاهدان": "zahedan",
    # Hamedan
    "hamedan": "hamedan", "hamadan": "hamedan", "همدان": "hamedan",
    # Kerman
    "kerman": "kerman", "کرمان": "kerman",
    # Yazd
    "yazd": "yazd", "یزد": "yazd",
    # Ardabil
    "ardabil": "ardabil", "ardebil": "ardabil", "اردبیل": "ardabil",
    # Bandar Abbas
    "bandar_abbas": "bandar_abbas", "bandarabbas": "bandar_abbas", "bandar abbas": "bandar_abbas",
    "بندرعباس": "bandar_abbas", "بندر عباس": "bandar_abbas",
    # Arak
    "arak": "arak", "اراک": "arak",
    # Zanjan
    "zanjan": "zanjan", "زنجان": "zanjan",
    # Sanandaj
    "sanandaj": "sanandaj", "senendaj": "sanandaj", "سنندج": "sanandaj",
    # Qazvin
    "qazvin": "qazvin", "ghazvin": "qazvin", "قزوین": "qazvin",
    # Khorramabad
    "khorramabad": "khorramabad", "khoramabad": "khorramabad", "خرم آباد": "khorramabad", "خرم‌آباد": "khorramabad",
    # Gorgan
    "gorgan": "gorgan", "esterabad": "gorgan", "گرگان": "gorgan",
    # Sari
    "sari": "sari", "ساری": "sari",
    # Bojnord
    "bojnord": "bojnord", "bojnourd": "bojnord", "bojnurd": "bojnord", "بجنورد": "bojnord",
    # Birjand
    "birjand": "birjand", "بیرجند": "birjand",
    # Bushehr
    "bushehr": "bushehr", "boushehr": "bushehr", "bushire": "bushehr", "بوشهر": "bushehr",
    # Shahrekord
    "shahrekord": "shahrekord", "shahr-e kord": "shahrekord", "shahr e kord": "shahrekord", "شهرکرد": "shahrekord",
    # Semnan
    "semnan": "semnan", "سمنان": "semnan",
    # Yasuj
    "yasuj": "yasuj", "yasooj": "yasuj", "یاسوج": "yasuj",
    # Ilam
    "ilam": "ilam", "ایلام": "ilam",
}


def lookup_city(city_query: str) -> tuple[float, float, str] | None:
    if not city_query:
        return None
    cleaned = re.sub(r"[\s\-_]+", " ", city_query.strip().lower())
    compact = cleaned.replace(" ", "")

    canonical = CITY_ALIASES.get(cleaned) or CITY_ALIASES.get(compact)
    if canonical and canonical in IRAN_CAPITALS:
        data = IRAN_CAPITALS[canonical]
        return data["lat"], data["lng"], data["name_fa"]
    return None