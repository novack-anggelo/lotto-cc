# Lotto CC

A small native Android app that shows the latest draw results, jackpot heights and next draw dates
for **LOTTO 6aus49** (with Spiel 77 and SUPER 6) and **Eurojackpot**, so you can decide whether to
play the next draw.

> 🚧 Work in progress. The app is built goal by goal in small pull requests. See the
> [implementation plan](docs/plans/draw-results-app.md).

## Tech stack (target)
- Kotlin, Jetpack Compose, Material 3
- MVVM + use cases (Clean Architecture), multi-module Gradle build with convention plugins
- Coroutines & Flow
- Retrofit + OkHttp + kotlinx.serialization
- Koin with the Koin Compiler Plugin (dependency graph verified at compile time)
- JUnit, Turbine, Compose UI tests

## Requirements
- Android Studio with JDK 25 (the bundled JBR works), Android SDK 37
- minSdk 28

## Build & test
```bash
./gradlew assembleDebug          # build the app
./gradlew test                   # JVM unit tests
./gradlew connectedAndroidTest   # instrumented UI tests (needs a running emulator/device)
./gradlew lintDebug              # Android lint
```

CI (GitHub Actions) runs assemble, unit tests and lint on every pull request to `dev`.

## Data source
Draw results come from the public Lotto24 draw info API:
`GET https://www.lotto24.de/public/v1/drawinfo/aggregated/6aus49,eurojackpot`
