# AZAN
  
[![Android Arsenal](https://img.shields.io/badge/Android-Time%20Prayer-brightgreen.svg)](https://android-arsenal.com/details/1/5099)  [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Date%20&%20Time%20Pickers-orange.svg)](https://android-arsenal.com/details/1/7676)![LICENSE](https://img.shields.io/badge/License-Apache%20License%20V2.0-yellow.svg)  ![Build](https://img.shields.io/badge/Azan-jitpack.io-blue.svg)  ![Kotlin](https://img.shields.io/badge/Kotlin-1.3.x-blue.svg)  
  
  
  
**What is Azan ?**   
The Muslim call to ritual prayer made by a muezzin from the minaret of a mosque (or now often played from a recording). Muslims are supposed to pray five times a day. At each time Azan will send a notification to pray. At first, Muslims used to calculate prayer times with sun raise and sun shadows, but to simplify it and make it more accurate, specially in cloudy cities or places where calculating is hard. Mosques currently use a complex calculation based on latitude, longitude, height, timezone and sunrise time.

  
  
  
## What is Azan library?  

Azan aims to calculate prayer times with no hassle, **using a single line of code**. It does the calculation so whenever you want to implement a prayer time application you won't need to go through this headache again.
  
  
![azan-preview](https://user-images.githubusercontent.com/1812129/69456412-df2a3080-0d6a-11ea-98d8-0ebc180984d7.png)
  
Islam Time prayers is very complex to calculate. There are many variables in the calculation, like:  
  
- **latitude**  
- **longitude**  
- **timezone**  
- **height**  
- **The Way of Calculation**   


Azan library allows you to determine latitude, longitude, timezone, height, and takes `The Way of Calculation` as an input. 
  
  
## Supported Calculation Methods :  
  
| Method Name                                          | Fajr Angel           | Isha Angel                                    |  
|:----------------------------------------------------:|:--------------------:|:---------------------------------------------:|  
| Umm al-Qura University, Makkah                       | 18.5                 | 90 min after Maghrib , 120 min during Ramadan |  
| MUHAMMADIYAH                                         | 20                   | 18                                            |  
| Muslim World League (MWL)                            | 18                   | 17                                            |  
| Egyptian General Authority of Survey                 | 19.5                 | 17.5                                          |  
| University of Islamic Sciences, Karachi              | 18                   | 18                                            |  
| Islamic Society of North America (ISNA)              | 15                   | 15                                            |  
| Ithna Ashari                                         | 16                   | 14                                            |  
| Institute of Geophysics, University of Tehran        | 17.7                 | 14                                            |  
| UOIF (Union des organisations islamiques de France)  | 12                   | 12                                            |  
| Kuwait Calc method                                   | 18                   | 17.5                                          |  
  
  
## How to use ?  
  
Add it to dependencies in your gradle file.

### Jitpack
Add `jitpack` as one of your maven repositories:


```
repositories {
   mavenCentral()
   // Other repositories
   maven("https://jitpack.io")
}
```

If you're in Android, preferably add it to your `root` gradle file.

### Dependency

After adding Jitpack, add this line to your dependencies:

```
implementation("com.github:ahmedeltaher:Azan:3.0")
```

### Getting prayer times
     
 
``` 
val today = SimpleDate(GregorianCalendar())
val location = Location(30.045411, 31.236735, 2.0, 0)
val azan = Azan(location, Method.EGYPT_SURVEY)
val prayerTimes = azan.getPrayerTimes(today)
val imsaak = azan.getImsaak(today) 
println("----------------results------------------------") 
println("date ---> " + today.day + " / " + today.month + " / " + today.year)
println("imsaak ---> $imsaak") println("Fajr ---> " + prayerTimes.fajr()) 
println("sunrise --->" + prayerTimes.shuruq())
println("Zuhr --->" + prayerTimes.thuhr())
println("Asr --->" + prayerTimes.assr()) 
println("Maghrib --->" + prayerTimes.maghrib())
println("ISHA  --->" + prayerTimes.ishaa())
println("----------------------------------------") 
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
