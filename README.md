# Lotto CC

A small native Android app that shows the latest draw results, jackpot heights and next draw dates
for **LOTTO 6aus49** (with Spiel 77 and SUPER 6) and **Eurojackpot**, so you can decide whether to
play the next draw.

> **About screenshots:** the Lotto24 draw info API currently answers every request with
> `404 Not Found`, including the exact URL from the case study. The app therefore shows its error
> screen with a retry button. To see the UI, open the Compose previews in Android Studio (see
> [Previews](#previews)).

## Features

**Must-haves**
- One card per lottery with the name, the last draw date and the drawn numbers in balls.
- The extra numbers look different, as the case study asks: the 6aus49 **Superzahl** is a ringed
  ball, while the Eurojackpot **Eurozahlen** sit together in a darker group.
- The next draw date is shown on a badge in the lottery's colours.
- The jackpot height (`WC_1`) is shown in a side strip ("23 MIO"). Right after a draw the jackpot
  is not known yet, so the strip stays empty and the draw is still shown.
- Built with Jetpack Compose and Material 3.

**Bonus**
- Spiel 77 and SUPER 6 numbers on the 6aus49 card.
- Tapping a card opens [lotto24.de](https://www.lotto24.de).
- Pull to refresh. The cards stay visible while new data loads.
- Not done: the home screen widget.

## Architecture

MVVM with a use case, split by layer into Gradle modules and built with convention plugins
(`build-logic`).

| Module | Contents |
|---|---|
| `app` | `Application` (starts Koin), `MainActivity` |
| `core:common:domain` | `DomainResult`, `DomainError`, `Money` |
| `core:common:presentation` | Shared strings |
| `core:network` | Shared `Json`, `OkHttpClient`, `Retrofit` |
| `core:designsystem` | Theme, lottery colours, spacing |
| `core:testing` | `MainDispatcherRule` |
| `feature:drawresults:domain` | `Draw` models, `DrawRepository` interface, `GetDrawsUseCase` |
| `feature:drawresults:data` | `DrawApi`, DTOs, `DrawDataMapper`, `DrawRepositoryImpl` |
| `feature:drawresults:presentation` | `DrawsViewModel`, UI models and mapper, `DrawsInfoScreen`, `DrawInfoCard` |

**Data flow:** `DrawApi` → `DrawDataMapper` → `DrawRepositoryImpl` → `GetDrawsUseCase` →
`DrawsViewModel` → `DrawUiMapper` → `DrawsUiState` → Compose.

**Decisions**
- **Errors are values, not exceptions.** The repository turns failures into
  `DomainResult.Failure` (`NETWORK`, `SERVER`, `UNKNOWN`), and the ViewModel maps those to UI
  errors. Coroutine cancellation is rethrown.
- **Lenient mapping.** A malformed entry is skipped instead of failing the whole response. A
  missing jackpot becomes `null`, not an error. A response with no usable draws counts as a
  server error.
- **One request.** `6aus49,eurojackpot,spiel77,super6` are fetched together, and Spiel 77 and
  SUPER 6 are attached to the 6aus49 draw.
- **Money as `BigDecimal` plus a currency code**, so amounts are never rounded through `Double`.
- **Koin with the Koin Compiler Plugin**, so a missing dependency fails the build instead of
  crashing at runtime.
- **Formatting at the edge.** Dates and amounts are formatted for the device locale in the UI layer
  ("Fr., 18.09.2020", "53,3 MIO").

## Tech stack
- Kotlin 2.4, Jetpack Compose, Material 3
- Coroutines and `StateFlow`
- Retrofit, OkHttp and kotlinx.serialization
- Koin 4 with the Koin Compiler Plugin
- JUnit 4, kotlin.test and kotlinx-coroutines-test

## Requirements
- Android Studio with JDK 25 (the bundled JBR works), Android SDK 37
- minSdk 28

## Build and test
```bash
./gradlew assembleDebug   # build the app
./gradlew test            # JVM unit tests
./gradlew lintDebug       # Android lint
```

CI (GitHub Actions) runs assemble, unit tests and lint on every pull request to `dev`.

### Tests
| Test | Covers |
|---|---|
| `DrawDataMapperTest` | DTO → domain: both lotteries, additional games, missing jackpot, skipping bad entries |
| `DrawRepositoryImplTest` | Success, empty response, and how each exception maps to a `DomainError`; cancellation is rethrown |
| `DrawUiMapperTest` | Domain → UI model for both lotteries |
| `DrawsViewModelTest` | Initial load, error mapping, retry, refresh (including double taps) |
| `NetworkModuleTest` | Retrofit setup in Koin |

### Previews
The previews live in `src/debug`, so they never reach the release build.
- `DrawInfoCardPreviews`: every sample card, at normal and 1.5× font size
- `DrawsInfoScreenPreviews`: the screen when loaded, refreshing, loading and in error

## How this was built

On top of the initial setup, I implemented the feature myself, step by step, as the commit history
shows: the domain models
and `DomainResult`, the DTOs, `DrawApi`, the data mapper and repository, the use case, the
ViewModel and its UI state, the UI mapper, the dependency injection wiring, the screen with its
loading, error, URI handling and pull to refresh, and the first version of the card.

I used AI (Claude Code) as an assistant for the project setup and for small, concrete tasks:
- **Initial project setup**, everything before commit `763e930`: the implementation plan in
  `docs/plans`, the convention plugins in `build-logic`, the version catalog, CI, the module
  skeleton, `core:network`, the design system theme and starting Koin in `app`.
- **Unit tests:** `DrawDataMapperTest` and `DrawRepositoryImplTest` (commit `0c1a43f`), and
  `DrawsViewModelTest` and `DrawUiMapperTest` (commit `d9c1225`).
- **Compose previews** (commit `95fcdc3`).
- **Polishing `DrawInfoCard`** from my skeleton: number balls, jackpot strip, formatters
  (commit `3cb0bd8`).
- **Review of my data layer.** It found three issues that I then had fixed: the empty-response
  check didn't return, a missing jackpot hid the whole lottery, and one bad entry failed the whole
  response.
- **Questions while I worked**, for example Kotlin variance (`out T`), Koin's `create()`, and why
  the `R` class of a module wasn't found.

## Known limitations and next steps
- **The API currently returns 404.** The mapping follows the sample response in the case study.
  The `number` field for Spiel 77 and SUPER 6 could not be checked against a live response.
- **Home screen widget** (a bonus item) is not implemented.
- **A failed refresh replaces the cards with the error screen.** Keeping the cards and showing a
  snackbar would be friendlier.
- **Number balls have a fixed size.** At very large font sizes or on narrow screens they get tight.
- **TalkBack reads the numbers one ball at a time.** One description for the whole row would read
  better.
- **No end-to-end test with real JSON** through Retrofit (`MockWebServer` is already a test
  dependency), and no Compose UI tests yet.

## Data source
Draw results come from the public Lotto24 draw info API:
`GET https://www.lotto24.de/public/v1/drawinfo/aggregated/6aus49,eurojackpot,spiel77,super6`
