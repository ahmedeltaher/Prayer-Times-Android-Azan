# AZAN


[![Android Arsenal](https://img.shields.io/badge/Android-Time%20Prayer-brightgreen.svg)](https://android-arsenal.com/details/1/5099)
![LICENSE](https://img.shields.io/badge/License-Apache%20License%20V2.0-brightgreen.svg)
![Build](https://img.shields.io/badge/Azan-jitpack.io-blue.svg)


**what is azan ?** 

``` 
The Muslim call to ritual prayer made by a muezzin from the minaret of a mosque
(or now often played from a recording) . 
Muslims have 5 prayers per day , at each time all muslims are notified by Azan ,
to pray. first Muslims used to calcualte prayer time with sun raise and sun shadows ,
but to simplyied it and make it moreaccurent speacilly in cloudy cities , 
Mosques currently use a complex calcualtion based on latitude ,
longitude,height,timezone, sun raise time.
```



## what is Azan library?

Library aiming to calculate prayer time with one line code , if you implement prayer time application , there is no need to do this headache again .

![Azan](./azan-preview.png)

-Islam Time prayers is every complex to calculate , cause there is many variables in this calculations like :

- **latitude**
- **longitude**
- **timezone**
- **height**
- **The Way of Calculation** 

you can determin your latitude, longitude, timezone, height, The Way of Calculation .


## Supported Calculation Methods :

| Method Name                                          | Fajr Angel           | Isha Angel                                    |
|:----------------------------------------------------:|:--------------------:|:---------------------------------------------:|
| Umm al-Qura University, Makkah                       | 18.5                 | 90 min after Maghrib , 120 min during Ramadan |
| MUHAMMADIYAH                                         | 20                   | 18                                            |
| Muslim World League (MWL)                            | 18                   | 17                                            |
| Egyptian General Authority of Survey                 | 19.5                 | 17.5                                          |
| University of Islamic Sciences, Karachi              | 18                   | 18                                            |
| Islamic Society of North America (ISNA)              | 15                   | 15                                            |
| Ithna Ashari                                         | 16	                  | 14                                            |
| Institute of Geophysics, University of Tehran        | 17.7                 | 14                                            |
| UOIF (Union des organisations islamiques de France)  | 12                   | 12                                            |
| Kuwait Calc method                                   | 18                   | 17.5                                          |


## How to use ?

Add it to dependencies in your gradle file 

- Add it in your root `build.gradle` at the end of repositories:

  - in your `allprojects` section , you have to add  `maven { url 'https://jitpack.io' }` 
 
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
 
 - in your `build.gradle` in your `app` module , Add the dependency `compile 'com.github.ahmedeltaher:Azan:2.1'` . 
 
 ```
 	dependencies {
 	        compile 'com.github.ahmedeltaher:Azan:2.1'
 	}
 ```

**Example how to get prayer times**

```
GregorianCalendar date = new GregorianCalendar();

PrayerTimes prayerTimes = new TimeCalculator().date(date).location(-6.38043079,106.85337984, 0,7).timeCalculationMethod(EGYPT).calculateTimes();

prayerTimes.setUseSecond(true);

prayerTimes.getPrayTime(PrayersType.FAJR);

prayerTimes.getPrayTime(PrayersType.SUNRISE);

prayerTimes.getPrayTime(PrayersType.ZUHR);

prayerTimes.getPrayTime(PrayersType.MAGHRIB);

prayerTimes.getPrayTime(PrayersType.ISHA);
```

 ## LICENSE

Ahmed Eltaher 2016

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

