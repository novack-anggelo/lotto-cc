# Lotto CC

A small native Android app that shows the latest draw results, jackpot heights and next draw dates
for **LOTTO 6aus49** (with Spiel 77 and SUPER 6) and **Eurojackpot**, so you can decide whether to
play the next draw.

| Light | Dark | Offline |
|---|---|---|
| <img src="docs/screenshots/light.png" width="240" alt="Draw results, light theme"> | <img src="docs/screenshots/dark.png" width="240" alt="Draw results, dark theme"> | <img src="docs/screenshots/error.png" width="240" alt="No connection error with Retry"> |

## Features
**Must have (case study)**
- Drawn numbers shown as balls; the **Superzahl** and the **Eurozahlen** are set apart in a darker
  group.
- Next draw date in a badge.
- Jackpot height in the card's side strip ("4 MILL" / "4 MIO"), hidden when it isn't announced yet.
- Built with Jetpack Compose.

**Bonus**
- Spiel 77 and SUPER 6 numbers on the LOTTO 6aus49 card.
- Tapping a card opens https://www.lotto24.de (or the LOTTO24 app, when it handles the link).

**Also**
- English and German; dates and amounts are formatted for the device's language and time zone.
- Light and dark theme; loading and error states with Retry.
- Accessibility:
  - Text contrast meets WCAG AA.
  - Layouts adapt to large font sizes and right-to-left languages.
  - Screen readers announce bonus numbers ("Superzahl 7") and read Spiel 77 / SUPER 6 digit by digit.

**Not included:** the home-screen widget (out of scope) and pull-to-refresh (planned, deferred).
See [Known limitations](#known-limitations).

## Tech stack
| Area | Choice |
|---|---|
| Language & UI | Kotlin 2.4, Jetpack Compose (BOM 2026.09), Material 3 |
| Architecture | Clean Architecture, MVVM, use cases, multi-module Gradle build |
| Async | Coroutines, `StateFlow` |
| Networking | Retrofit 3, OkHttp 5, kotlinx.serialization |
| DI | Koin 4.2 with the **Koin Compiler Plugin**: the whole graph is checked when the app compiles |
| Build | AGP 9.5, Gradle convention plugins in `build-logic/`, version catalog |
| Tests | JUnit 4, kotlinx-coroutines-test, Turbine, MockWebServer, Compose UI tests |
| CI | GitHub Actions |

## Architecture
```
:app ──► :feature:drawresults:presentation ──► :feature:drawresults:domain (pure Kotlin)
  │                 │                                   ▲
  │                 └──► :core:designsystem             │
  └──► :feature:drawresults:data ───────────────────────┘
                    ├──► :core:network
                    └──► :core:common (pure Kotlin) ◄── :app (AndroidLogger)
:core:testing — shared test utilities (MainDispatcherRule)
```

Each layer of the feature is its own Gradle module, so its dependencies are explicit and checked by
the build:

| Module | What it holds |
|---|---|
| `domain` | `LotteryDraw` (one subtype per lottery), `Money`, `DomainResult` / `DomainError`, the repository interface and `GetDrawResultsUseCase`. Pure Kotlin, no Android. |
| `data` | `DrawInfoApi` (Retrofit), DTOs, DTO → domain mappers, `DrawResultsRepositoryImpl`. One request fetches all four lotteries; Spiel 77 / SUPER 6 are attached to the 6aus49 draw they were drawn with. |
| `presentation` | UI models, domain → UI mappers, date and jackpot formatters, `DrawResultsViewModel` (`StateFlow`), the Compose screen and card. |
| `core:designsystem` | Theme, lottery colours, spacing scale, `LotteryBall` and the `DrawnNumbers` row. |
| `core:network` | OkHttp, Retrofit and Json setup. |
| `core:common` | The `Logger` interface (implemented in `:app`). |
| `app` | `Application` (starts Koin), `MainActivity` and the root Koin module. |

**Data flow:** API → DTOs → `toDomain()` → `GetDrawResultsUseCase` → `DrawResultsViewModel` →
`toUiModel()` → Compose. All mapping between layers uses extension functions.

**Key decisions** (the full list, with who decided what, is in the
[implementation plan](docs/plans/draw-results-app.md#decisions)):
- **Times:** stored as `Instant` (UTC) and shown in the device's time zone.
- **Formatting:** dates and amounts are formatted when the screen draws them. A ViewModel outlives
  a language change, so text formatted inside it would go stale.
- **Errors:** a typed `DomainResult` / `DomainError`, not `kotlin.Result`, so every error case
  must be handled and coroutine cancellation is never swallowed.
- **API data:** we trust the API contract. DTO fields are nullable, so one incomplete lottery
  can't break the others, but values are shown as the API sends them.
- **Data source:** network only, no cache.

## Testing
| Kind | Where | Count |
|---|---|---|
| JVM unit tests | domain (7), data (30), network (6), presentation (16) | 59 |
| Instrumented Compose UI tests | designsystem (7), presentation (8) | 15 |

- **Unit tests** cover:
  - the use case
  - DTO parsing and mapping, against the case study's sample response and edge cases
  - the repository, against MockWebServer
  - Koin modules
  - formatters
  - the ViewModel, with Turbine
- **UI tests** cover:
  - ball sizing and wrapping at large font sizes
  - the cards' content
  - the jackpot shown only when known
  - Spiel 77 / SUPER 6
  - loading and error states, and Retry
  - opening the website, with a fake `UriHandler`

## Build & run
**Requirements**
- Android Studio with Android SDK 37; the app supports API 28 and up.
- Gradle runs on JDK 25. `gradle/gradle-daemon-jvm.properties` lets Gradle download it
  automatically; the JBR bundled with Android Studio also works.

```bash
./gradlew assembleDebug                 # build the app
./gradlew installDebug                  # install on a running emulator/device
./gradlew test                          # JVM unit tests
./gradlew connectedDebugAndroidTest     # instrumented UI tests (needs an emulator/device)
./gradlew lintDebug                     # Android lint
```

**CI.** GitHub Actions runs `assembleDebug compileReleaseKotlin test lintDebug` on every pull
request to `dev`. `compileReleaseKotlin` catches debug-only APIs, such as `@Preview`, used from
`main`. CI has no emulator, so the instrumented tests were run locally and their results posted in
each PR.

## How it was built
- **Plan first:** the case study was turned into an
  [implementation plan](docs/plans/draw-results-app.md) after several clarification rounds, then
  split into small goals.
- **One goal per pull request:** each goal got its own git worktree and branch
  (`feature/<feature>/<task>`) and a pull request into `dev` of about 400 changed lines at most,
  made of small commits that each compile.
- **Each PR went through:**
  - a local check: the CI command, plus the UI tests on an emulator
  - an automated code review, with every finding fixed or kept with a written reason
  - CI
  - my review and merge
- **Releases:** `dev` is merged into `main` (#13).

The implementation was written with **Claude Code** (an AI coding agent) under my direction. The next
section is about that direction.

## Developer interventions: questions, pushback and decisions
The agent proposed and implemented; I reviewed every pull request before merging. These are the
places where I questioned a choice, pushed back or set the direction, and what changed because of it.

### Setting the frame
Before any code, I set the working rules:
- Clean Architecture, modularisation, MVVM, use cases
- Koin with the Compiler Plugin DSL
- mapping through extension functions
- unit and UI tests
- small compilable commits and pull requests of about 400 lines at most, on worktrees, each waiting
  for my approval

In the clarification rounds I also chose:
- **Scope:** Spiel 77 / SUPER 6, the website link and pull-to-refresh, but no widget.
- **Data:** network only.
- **Languages:** English and German.
- **Build:** feature modules split into layers, a pure-Kotlin domain, convention plugins.
- **Tests:** instrumented UI tests.
- **CI:** build, unit tests and lint.

### Architecture questions
| What I asked | What changed | PR |
|---|---|---|
| "Why are you using `OffsetDateTime`?" | The domain uses `Instant` from `drawDateUtc`, shown in the device's time zone. | #3 |
| "Does it make sense to have `DomainResult`? Why not Kotlin's `Result`?" | We compared the trade-offs and kept `DomainResult`: typed errors, exhaustive `when`, no `runCatching` cancellation pitfalls. The reasoning is now recorded in the plan. | #3 |
| "What is `AndroidFailureLogger` for? Why call it *Failure*Logger?" | The single-purpose failure logger became a general `Logger` interface in `:core:common`, implemented in `:app`. | #7 |
| "Why do we need a list of supported lotteries?" | `LotteryIds.SUPPORTED` was removed; the request list sits where it's used. | #14 |

### Code-quality pushback
| What I pushed back on | What changed | PR |
|---|---|---|
| Previews in `main`, `@Preview` on the main classpath | Previews moved to `src/debug`, `ui-tooling-preview` became debug-only, and CI compiles release to enforce it. | #8 |
| Several near-identical preview functions | One preview per component, fed by a `CollectionPreviewParameterProvider`. | #10 |
| "Most of those `n.dp` seem like magic numbers" | A `Spacing` scale plus named or derived sizes, in a separate PR to keep #10 small. The refactor also exposed a right-to-left bug in the badge, which was fixed. | #11 |
| "Why `BasicText`?" | Material 3 `Text` already supports `autoSize`; switched for consistency with the rest of the UI. | #10 |
| `JsonPrimitive` and digit-count checks added "just in case" | "We need to trust the API contract": `String` as the API sends it, no extra validation. I made the change; the agent then fixed the stale docs and tests it left behind. | #14 |
| `asSequence()` on a four-item list | Plain list operations. From then on, **Keep It Simple** was a rule for every later review. | #14 |
| A URL constant inside the screen file | Moved to an untranslatable string resource. | #16 |

### Design direction
| My call | Result | PR |
|---|---|---|
| Restyled the balls and row myself: group the Superzahl / Eurozahlen in a darker pill instead of styling each ball | Simpler balls; then "all balls have the same style", so `BallStyle` was removed entirely. | #8 |
| A metal effect instead of flat grey | A static brushed-silver row, chosen over an animated shimmer, with a gunmetal bonus group. | #8 |
| Card: its own preview, metal on the jackpot strip, a paper-like background, elevation and shadow | Paper-toned surfaces for the whole theme, an elevated card and a metal strip. | #10 |
| "I don't like the vertical metal effect; make it slanted like the balls" | Slanted stripes. The strip's stripes only shade away from the text colour, so the jackpot stays readable. | #10 |
| The Eurojackpot strip looked too dark | A lighter bronze, kept within the WCAG AA contrast minimum (4.9:1). | #10 |
| A bigger jackpot number, "MILL" instead of "million", no currency (like the mockup) | A large number with "MILL" / "MIO" below; screen readers still get the full amount. | #10 |
| Spiel 77 / SUPER 6 label text and number colour | Adjusted directly on `dev` to match the mockup ("Spiel77:", "SUPER6:"). | `1abadaf` |

### Scope and prioritisation
- **Spacing refactor:** I moved it to its own PR (#11) instead of growing #10.
- **Pull-to-refresh:** I deferred it in favour of the website link (#16) and this documentation.
- **Release:** I released `dev` to `main` once the must-haves were done (#13).

**Effect overall:** these interventions made the code simpler (KISS, trust the contract, no magic
numbers), kept the app's look close to the mockup while staying accessible, and turned one-off
fixes into rules for the rest of the project, such as debug-only previews, preview providers and
the spacing scale.

## Known limitations
- **Pull-to-refresh** is planned but deferred (goal 13 in the plan).
- **Home-screen widget:** out of scope.
- **Dark mode:** the dark-brown Eurojackpot date badge stands out little from the dark card.
- **TalkBack:** it doesn't announce that tapping a card opens a website.
- **Time zone:** if it changes while the screen is open, dates update only after the screen is
  recreated.
- **Minified release build:** checked by hand on an emulator; CI only compiles it.

## Data source
Draw results come from the public Lotto24 draw info API:
`GET https://www.lotto24.de/public/v1/drawinfo/aggregated/6aus49,eurojackpot,spiel77,super6`
