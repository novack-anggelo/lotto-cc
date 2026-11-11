# Lotto CC — Draw Results App

**Status:** Approved 2026-11-10
**Source:** Case Study – Mobile Apps Developer (PDF) + clarification rounds in chat (2026-11-10)

## Goal
A small native Android app that shows the latest draw results, jackpot heights and next draw dates for
LOTTO 6aus49 (incl. Spiel 77 / SUPER 6) and Eurojackpot, so a user can decide whether to play the next draw.
The codebase itself is the showcase: Clean Architecture, modularisation, MVVM, testable code and a readable git history.

## Requirements
**Product (PDF must-haves)**
- One "draw results" screen with a card for LOTTO 6aus49 and a card for Eurojackpot.
- Each card shows the lottery name, the last draw date and the drawn numbers rendered as balls.
- 6aus49: six numbers + the **Superzahl** in a visually distinct ball (outlined, like the mockup).
- Eurojackpot: five numbers + two **Eurozahlen** in a visually distinct group (shaded, like the mockup).
- Next draw date is displayed ("Nächste Ziehung" / "Next draw").
- Jackpot height (tier `WC_1` of `nextDraw.jackpot.jackpots`) is displayed when present; hidden when absent.
- UI built with Jetpack Compose.

**Bonus (agreed in scope)**
- Spiel 77 and SUPER 6 numbers shown on the 6aus49 card (as in the mockup).
- Tapping a card opens https://www.lotto24.de.
- Pull-to-refresh reloads the data.

**Technical (from user)**
- Native Android, Kotlin, Compose, MVVM, use cases, coroutines + Flow.
- Retrofit for networking, Koin (4.x, Compiler Plugin DSL) for DI.
- Mapping between layers via extension functions.
- Unit tests (JVM) + instrumented Compose UI tests (emulator).
- Strings in English (default) + German (`values-de`); dates/amounts formatted per locale.

**Process (from user)**
- Small compilable commits; feature-oriented PRs of max ~400 changed lines.
- Each goal: worktree → branch `feature/<feature_name>/<task>` from `dev` → implement → self code review →
  push → PR into `dev` → wait for user approval and merge → next goal.

## Out of scope
- Home-screen widget (bonus, explicitly deferred).
- Persistence / offline cache (network-only; repository interface leaves room for one later).
- Other lotteries, prize quotas/winner tables, ticket purchase.
- Emulator-based tests in CI.

## Decisions
| Decision | Choice | Source |
|---|---|---|
| Bonus scope | Spiel77+SUPER6, tap opens link, pull-to-refresh | User, round 1 |
| Caching | Network only | User, round 1 |
| Language | English default + German | User, round 1 |
| Koin | 4.x with Koin Compiler Plugin DSL (compile-time graph check) | User, round 1 |
| Modules | Feature sliced in layers: `:feature:drawresults:{domain,data,presentation}` + `:core:*` | User, round 2 |
| Domain module | Pure Kotlin JVM (no Android dependency) | User, round 3 |
| Gradle config | `build-logic` convention plugins | User, round 3 |
| UI tests | Instrumented (`connectedAndroidTest`), run locally on emulator, results posted in PR | User, rounds 2–3 |
| CI | GitHub Actions on PRs to `dev`: assemble + unit tests + lint | User, rounds 2–3 |
| Merging | User merges on GitHub; I wait, then branch next goal from updated `dev` | User, round 2 |
| Tap target | https://www.lotto24.de | User, round 3 |
| JSON | kotlinx.serialization + Retrofit converter | Delegated (my choice) |
| Error model | `DomainResult<T>` + enum `DomainError` (not `kotlin.Result`): typed, exhaustive `when`, no `Throwable` / `runCatching` cancellation pitfalls | User, PR 3 review |
| Formatting | UI models keep `Instant` / `Money`; dates and jackpot are formatted when rendered with the current locale and zone (a ViewModel outlives locale changes) | Delegated, PR 9 |
| Dates | `java.time.Instant` from `drawDateUtc` in the domain; formatted in the **device time zone** by presentation | User, PR 3 review |
| Navigation | None — single screen, no nav library | Delegated |
| Branding | No Lotto24/lottery logos; colour accents + text only | Delegated (avoid trademark assets) |
| Worktrees | `~/AndroidStudioProjects/lottocc-worktrees/<task>` | Delegated |

## Technical approach

### Module graph
```
:app ──► :feature:drawresults:presentation ──► :feature:drawresults:domain (JVM)
  │                 │                                   ▲
  │                 └──► :core:designsystem             │
  └──► :feature:drawresults:data ───────────────────────┘
                    ├──► :core:network
                    └──► :core:common (JVM) ◄── :app (AndroidLogger)
:core:testing (JVM) — shared test utilities (MainDispatcherRule), testImplementation only
domain testFixtures — domain fixtures + FakeDrawResultsRepository, reused via testFixtures(project(...))
core:common testFixtures — RecordingLogger
build-logic/ — convention plugins: lotto.android.application, lotto.android.library,
               lotto.android.library.compose, lotto.jvm.library, lotto.koin
```
- **domain**: models, `DrawResultsRepository` interface, `GetDrawResultsUseCase` (one-shot `suspend` returning `DomainResult`, draws in display order), typed `DomainError`. Flows live in the ViewModel (`StateFlow`); refresh calls the use case again.
- **data**: Retrofit `DrawInfoApi`, DTOs, DTO→domain extension mappers, `DrawResultsRepositoryImpl` (logs failures via `Logger`), Koin `drawResultsDataModule`.
- **presentation**: UI models, domain→UI extension mappers, render-time formatters (dates, jackpot), `DrawResultsViewModel` (`StateFlow<DrawResultsUiState>`), Compose screen, Koin `drawResultsPresentationModule`.
- **core:network**: OkHttp/Retrofit/Json setup + Koin `networkModule`.
- **core:common**: platform-independent `Logger` interface (PR 7 review); Android implementation `AndroidLogger` in `:app`.
- **core:designsystem**: theme, lottery colours, `LotteryBall` and ball-row composables.
- **app**: `AndroidLogger` (PR 7); `Application` (startKoin), `MainActivity` and the root Koin module composing `networkModule`, feature modules and the `Logger` binding (goal 11).

### Domain model (as built in PR 3)
```kotlin
sealed interface LotteryDraw {            // one subtype per lottery; Spiel 77 / SUPER 6 added in goal 12
    val lastDrawTime: Instant; val numbers: List<Int>; val nextDraw: NextDraw
    data class Lotto6aus49(..., val superNumber: Int, ...) : LotteryDraw
    data class Eurojackpot(..., val euroNumbers: List<Int>, ...) : LotteryDraw
}
data class NextDraw(val time: Instant, val jackpot: Money?)   // jackpot null when unknown
class Money(val amount: BigDecimal, val currencyCode: String) // equality by value
sealed interface DomainResult<out T> { Success(data) ; Failure(error: DomainError) }
```

### API
`GET https://www.lotto24.de/public/v1/drawinfo/aggregated/6aus49,eurojackpot` (Spiel 77/SUPER 6 appended
in the bonus goal: `…/6aus49,eurojackpot,spiel77,super6`, verified live — they return `drawResult.number` as a string,
SUPER 6 has no jackpot). DTO fields are all nullable so one bad entry never fails the response. Mapping (PR 6): unknown lotteries and entries missing a date, numbers, Superzahl/Eurozahlen or the next draw are dropped; the jackpot is the top prize class `WC_1` and is null when missing, malformed or zero.

### UI state & behaviour
`Loading` → `Content(cards)` | `Error(retry)`. Refresh failure while content is shown keeps the content and shows a
snackbar. Jackpot shown abbreviated in the card's accent strip ("4 MILL" / "4 MIO", no currency; screen readers get the full amount), next draw as a badge
("Nächste Ziehung: Mi., 11.11.2026").

## Implementation steps (one goal = one PR into `dev`)
| # | Branch | Goal | Verification |
|---|---|---|---|
| 1 | `feature/project_setup/build_logic` | `build-logic` convention plugins; `:app` migrated to them; catalog updated (Koin, Retrofit, serialization, coroutines, test libs); template tests removed | `./gradlew assembleDebug` |
| 2 | `feature/project_setup/ci` | GitHub Actions workflow (assemble, `test`, `lint`) on PRs to `dev`; README skeleton; this plan committed | Workflow green on the PR |
| 3 | `feature/draw_results/domain` | `:feature:drawresults:domain` (JVM) models, repository interface, `GetDrawResultsUseCase`; test fixtures | Unit tests for use case + `Money` |
| 4 | `feature/draw_results/network` | `:core:network` Retrofit/OkHttp/Json + Koin `networkModule` | Build + Koin compile check |
| 5 | `feature/draw_results/data_dtos` | `:feature:drawresults:data` DTOs + `DrawInfoApi` | JSON fixture deserialization tests |
| 6 | `feature/draw_results/data_mapping` | DTO→domain extension mappers | Mapper unit tests (superNumber vs euroNumbers, null/empty jackpot, unknown lottery) |
| 7 | `feature/draw_results/data_repository` | `DrawResultsRepositoryImpl`, error mapping, Koin `dataModule` | Repository tests (MockWebServer: 200, 500, IO error) |
| 8 | `feature/draw_results/design_system` | `:core:designsystem` theme, lottery colours, `LotteryBall` (regular / Superzahl / Eurozahl) | Previews + instrumented ball tests |
| 9 | `feature/draw_results/viewmodel` | `:feature:drawresults:presentation` UI models, domain→UI mappers (dates, jackpot), `DrawResultsViewModel`; `:core:testing` (`MainDispatcherRule`) | ViewModel (Turbine) + mapper unit tests |
| 10 | `feature/draw_results/screen` | Compose screen: cards, jackpot strip, next-draw badge, loading/error; EN+DE strings | Instrumented Compose UI tests |
| 10b | `feature/design_system/spacing` | `Spacing` scale in `:core:designsystem`; dp literals replaced by spacing steps and named component sizes (PR 10 review) | Existing UI tests; visual check |
| 11 | `feature/draw_results/app_wiring` | `Application` + startKoin, `MainActivity` hosts screen, template theme removed | App runs on emulator (screenshot in PR) |
| 12a | `feature/additional_lotteries/data` | Spiel 77 / SUPER 6: domain fields on the 6aus49 draw, request all four lotteries, attach numbers drawn with the 6aus49 draw | DTO + mapper + repository unit tests |
| 12b | `feature/additional_lotteries/ui` | Render Spiel 77 / SUPER 6 on the 6aus49 card (as in the mockup) | UI model mapper unit tests + UI test |
| 13 | `feature/pull_to_refresh/refresh` | `PullToRefreshBox`, VM refresh state, snackbar on failure (deferred by the user; after 14) | VM unit tests + UI test |
| 14 | `feature/website_link/open_link` | Tap card → open https://www.lotto24.de | UI test (intent / UriHandler fake) |
| 15 | `feature/project_setup/docs` | README: screenshots, features, architecture, module graph, how to build/test, decisions, developer interventions, known limitations | Review |

Each PR: small commits, self-review with `/code-review` before push, description with summary, test evidence
(unit + local instrumented output where UI changed), and the Claude Code attribution footer.

## Acceptance criteria & verification
- [ ] App launches and shows 6aus49 and Eurojackpot cards from the live API — manual run on emulator (PR 11)
- [ ] Numbers rendered as balls; Superzahl and Eurozahlen visually distinct — UI tests (PR 8, 10)
- [ ] Last draw date and next draw date shown per lottery — UI + mapper tests (PR 9, 10)
- [ ] Jackpot shown when present, hidden when absent — mapper + UI tests (PR 6, 9, 10)
- [ ] Loading and error/retry states — VM + UI tests (PR 9, 10)
- [ ] Spiel 77 and SUPER 6 shown on 6aus49 card — tests (PR 12)
- [ ] Pull-to-refresh reloads; failure keeps content — tests (PR 13)
- [ ] Tapping a card opens lotto24.de — UI test (PR 14)
- [ ] German strings when device language is German — UI test with locale (PR 10)
- [ ] Domain module has no Android dependency — enforced by JVM module (PR 3)
- [ ] Koin graph verified at compile time — build (PR 4+)
- [ ] CI green on every PR from PR 2 onwards

## Risks & open notes
- **Bleeding-edge toolchain** (AGP 9.5.1, Gradle 9.6): resolved in PR 1. Kotlin bumped 2.2.10 → 2.4.20 (newest
  version supported by Koin Compiler Plugin 1.2.3); the Gradle daemon runs on JDK 25 (`gradle-daemon-jvm.properties`),
  bytecode targets JVM 17.
- **PR 1 size**: build-logic + catalog may approach 400 lines; will split if exceeded.
- **Live API dependency**: tests never hit the network (fixtures + MockWebServer).
- The case-study PDF stays untracked (not committed).
- Since PRs are sequential and you merge each before the next, there's no stacking; each branch starts from latest `dev`.
