# easy-cyclewall


## Name

easy-cyclewall : Android application from my easy brand


## Description

Small Android application to cycle the wallpaper according perdiodic times

## Technologies covered

- Kotlin
- KSP
- Compose
- Coroutines
- Flow
- Room (database ORM)
- Architecture (database-dao-repository-viewmodel)
- Hilt (injection framework)
- Navigation 3
- Coil (Async Image)
- External App call (Document, Camera)

## Entities

- Cycle () // a given period during a picture will be used as wallpaper
- Next () // derived from Cycle computing the next event according to the current day


## Relations 

Cycle and Next share the same id

## Special tricks

- No heavy UI tricks
- No heavy VM tricks
- Apply the best current cycle to the phone wallpaper
- The best current is :
  + the current day is in the timelap
  + the duration is the smallest
- Why so complex
  + Cycle can be partially or fully overlapped
  + for exemple :
     winter (3 months )
     christmas hollydays (15 days)
     christmas night/day (2 days)
     new year (1/2 days)  	 

## Dependencies

- Mainly my beloved android lib (all standard required libs)
- No other special libs

### In libs.version.toml
    [versions]
    hassenforder-android = "XX"

    [libraries]
    fr-uha-android = { module = "fr.uha.hassenforder:easy.android", version.ref = "hassenforder.android" }

### In settings-gradle.kts

    dependencyResolutionManagement {
       repositories {
            ...
            maven(url = "https://maven.pkg.github.com/hassenforder/easy-android") {
                credentials {
                    username = providers.gradleProperty("github-user").get()
                    password = providers.gradleProperty("github-pat").get()
                }
            }
            maven(
                url = "https://jitpack.io"
            )
            ...
        }
    }

## License

Copyright © 2026 Hassenforder

Software released under the MIT license.

See the LICENSE file for more information.

https://opensource.org/licenses/MIT

## Project status

Ongoing
