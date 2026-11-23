# FORGE — Personal Fitness, Performance & Nutrition OS

<p align="center">
  <img src="app/src/main/res/drawable/ic_forge_splash.xml" width="96" height="96" alt="FORGE Logo" />
</p>

<p align="center">
  <strong>An elite, offline-first, intelligent Personal Fitness & Lifestyle Operating System built for Android.</strong>
</p>

<p align="center">
  <a href="https://developer.android.com"><img src="https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=flat&logo=android" alt="Platform" /></a>
  <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.1.0-7F52FF.svg?style=flat&logo=kotlin" alt="Kotlin" /></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack%20Compose-2024.12.01-4285F4.svg?style=flat&logo=jetpackcompose" alt="Compose" /></a>
  <a href="https://developer.android.com/training/data-storage/room"><img src="https://img.shields.io/badge/Room%20DB-2.6.1-F58220.svg?style=flat" alt="Room" /></a>
  <a href="https://dagger.dev/hilt/"><img src="https://img.shields.io/badge/Hilt%20DI-2.54-00A98F.svg?style=flat" alt="Hilt" /></a>
  <a href="#"><img src="https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-blue.svg?style=flat" alt="Min SDK" /></a>
  <a href="#"><img src="https://img.shields.io/badge/Target%20SDK-35%20(Android%2015)-brightgreen.svg?style=flat" alt="Target SDK" /></a>
  <a href="#"><img src="https://img.shields.io/badge/Tests-Passing%20(14%20Suites)-success.svg?style=flat" alt="Tests" /></a>
</p>

---

## 📌 Overview

**FORGE** is not just another basic gym workout counter. It is a comprehensive, deterministic personal performance ecosystem designed from the ground up for serious athletes, bodybuilders, and fitness enthusiasts.

Engineered with modern Android best practices, **Clean Architecture**, **Jetpack Compose (Material 3)**, and an **offline-first local Room SQLite database**, FORGE operates with zero mandatory cloud accounts, zero telemetry, and zero subscription paywalls. Everything runs locally on-device at peak 120Hz performance.

---

## 🚀 Key Modules & System Architecture

```
                                  ┌───────────────────────────┐
                                  │      FORGE CORE OS        │
                                  └─────────────┬─────────────┘
                                                │
       ┌────────────────────┬───────────────────┼───────────────────┬────────────────────┐
       ▼                    ▼                   ▼                   ▼                    ▼
┌──────────────┐   ┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐ ┌──────────────────┐
│   PHASE 1    │   │     PHASE 2     │ │     PHASE 3     │ │     PHASE 4     │ │     SERVICE      │
│  WORKOUT &   │   │   PERFORMANCE   │ │  COACH & SMART  │ │   NUTRITION &   │ │ FOREGROUND TIMER │
│ STREAKS OS   │   │  INTELLIGENCE   │ │    GOALS OS     │ │  LIFESTYLE OS   │ │  (HEALTH TYPE)   │
└──────────────┘   └─────────────────┘ └─────────────────┘ └─────────────────┘ └──────────────────┘
```

---

## ✨ Features by Phase

### Phase 1 — Core Fitness Execution & Consistency OS
* **Scientific 6-Day Push/Pull/Legs Split:** Pre-configured A/B hypertrophy splits balanced across agonist/antagonist muscle groups.
* **51 Seeded Movement Directory:** Complete with target muscles, biomechanical setup instructions, execution steps, and equipment substitution alternatives.
* **Active Workout Execution Engine:**
  * Real-time set logging with weight (kg), target reps, and completion checkmarks.
  * Historical performance preview (`PREVIOUS` column showing past session load).
  * In-session rest countdown timer overlay with `+15s` increment and instant `SKIP`.
* **Consistency & Rewards System:**
  * Daily workout streaks with animated pulsing fire indicators.
  * 30-day visual activity calendar heatmap.
  * XP progression engine (+100 XP per completed workout) and level title unlocking.
  * Unlockable achievements and badges.
* **Resilient Workout Foreground Service:**
  * Android 14/15 (`targetSdk 35`) compatible foreground service declaring `FOREGROUND_SERVICE_TYPE_HEALTH`.
  * Ongoing notification displaying live workout duration and rest countdown.

### Phase 2 — Performance Intelligence & Analytics
* **Progress Analytics Dashboard:**
  * Dynamic timeframe filtering: `7D`, `30D`, `90D`, `6M`, and `1Y`.
  * Aggregated metrics: Total completed sessions, cumulative tonnage lifted (kg), total working sets, and reps.
* **Native Canvas Hardware-Accelerated Charts:**
  * `ForgeBarChart`: Weekly and monthly training volume visualization.
  * `ForgeLineChart`: Estimated 1-Rep Max (1RM) curves over time.
* **Automated Personal Record (PR) Detection:**
  * Automatically calculates **Epley 1RM**: $\text{1RM} = \text{Weight} \times \left(1 + \frac{\text{Reps}}{30}\right)$.
  * Distinguishes between absolute load PRs and estimated 1RM PRs.
* **Body Measurements & Physique Tracker:**
  * Track bodyweight (kg), chest, waist, biceps/arms, shoulders, and body fat percentage.
  * Weight trend visualization line chart with historical logging.

### Phase 3 — Personal Fitness Intelligence (Coach OS)
* **Smart Goals Management:**
  * Track Strength, Muscle Gain, Bodyweight Targets, Exercise-Specific Numbers, and Habits.
  * Real-time completion percentage: $\text{Progress} = \frac{\text{Current} - \text{Start}}{\text{Target} - \text{Start}} \times 100\%$.
  * State management: Active, Completed, and Archived filters with target date pickers.
* **Daily Recovery & Readiness Check-In:**
  * Log sleep duration (hours), energy rating (1–5), muscle soreness (1–5), and stress levels (1–5).
  * Dynamic deterministic **Readiness Score** ($\ge 80$: *Ready to Forge*, $60-79$: *Moderate Readiness*, $< 60$: *Active Recovery*).
* **Deterministic Coach Engine:**
  * Rule-based intelligence monitoring training frequency, volume spikes ($> 20\%$), deload recommendations, and streak milestones without medical claims.
* **Weekly Performance Recap Report:**
  * Aggregates weekly volume changes, session consistency, PRs set, and average recovery readiness.

### Phase 4 — Nutrition & Lifestyle OS
* **Daily Nutrition Dashboard:**
  * Real-time circular and horizontal progress bars for Daily Calories, Remaining Calories, Protein, Carbohydrates, and Fats.
  * Meal categorization: `BREAKFAST`, `LUNCH`, `DINNER`, `SNACKS`.
  * Date navigator to inspect past or plan future days.
* **Pre-Seeded 60+ Item Food Database:**
  * Dedicated Indian dietary staples (Paneer, Roti, Dal, Idli, Dosa, Chicken Curry, Rajma, Khichdi, etc.) alongside global fitness foods (Whey Isolate, Oats, Eggs, Chicken Breast, Peanut Butter).
  * Search bar with instant typeahead filter by food category.
  * Serving size multiplier (`0.5x`, `1.0x`, `1.5x`, `2.0x`) and custom gram inputs.
* **Custom Food Creator:**
  * Save custom meals, snacks, or supplements directly to local SQLite database.
* **Hydration Tracker:**
  * Visual water progress indicator with customizable daily target (default 3,000 ml).
  * Quick-add buttons (`+250ml Glass`, `+500ml Bottle`, `+750ml Shaker`, `+1000ml Jug`) and custom ml amounts.
* **Macro Targets Engine:**
  * Fitness goal presets:
    * **Maintenance:** 2,200 kcal (140g P, 240g C, 65g F, 30g Fiber, 3,000ml Water)
    * **Muscle Gain (Bulk):** 2,600 kcal (170g P, 300g C, 75g F, 35g Fiber, 3,500ml Water)
    * **Fat Loss (Cut):** 1,800 kcal (160g P, 160g C, 50g F, 30g Fiber, 3,000ml Water)
  * Real-time caloric validation: $\text{Calories} = 4P + 4C + 9F$.
* **Weekly Meal Planner & Grocery Engine:**
  * Day-of-week meal planner (`Mon` to `Sun`).
  * Automated 1-click **Weekly Grocery Shopping List** generator aggregating required quantities across all planned meals.

---

## 🛠️ Tech Stack & Architecture

| Layer | Technology | Details |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.1.0 | 100% Coroutines & Flow |
| **UI Framework** | Jetpack Compose (BOM 2024.12.01) | Declarative UI, Single-Activity architecture |
| **Design System** | Material 3 + Custom FORGE Theme | Dark athletic aesthetic (`#101310` background, `#A3E635` accent) |
| **Dependency Injection** | Dagger Hilt 2.54 | Scoped ViewModels and Singleton repositories |
| **Local Persistence** | Room Database 2.6.1 | SQLite Room v3 with type converters and seeder |
| **Preferences** | AndroidX DataStore 1.1.1 | Reactive key-value storage for checklist & user state |
| **Service Layer** | Android Foreground Service | Compliant with Android 14/15 health service requirements |
| **Charts** | Native Jetpack Compose Canvas | 60/120fps hardware-accelerated bar and line charts |
| **Testing** | JUnit 4 + Coroutines Test | Unit testing across domain, viewmodels, and database |

### Directory Structure

```
app/src/main/kotlin/com/forge/app/
├── data/
│   ├── db/
│   │   ├── dao/             # Room DAOs (Workout, Exercise, Goal, Food, Hydration, etc.)
│   │   └── entities/        # Room Database Entity models
│   ├── preferences/         # DataStore preference stores
│   ├── repository/          # Repository implementations
│   └── seed/                # ForgeSeeder (51 exercises) & FoodDatabaseSeeder (60+ foods)
├── di/                      # Hilt Dependency Injection modules
├── domain/                  # CoachEngine, Domain models, and business logic
├── service/                 # WorkoutTimerService (Android Foreground Service)
└── ui/
    ├── components/          # Reusable ForgeButton, ForgeCharts, StreakWidget, etc.
    ├── navigation/          # ForgeNavigation graph & ForgeDestination routes
    ├── screens/
    │   ├── coach/           # Coach & Weekly Report screens
    │   ├── consistency/     # Streaks, Calendar Heatmap & Badges
    │   ├── exercise/        # Exercise Directory & Details
    │   ├── goals/           # Smart Goals & Add Goal form
    │   ├── history/         # Workout session history log
    │   ├── nutrition/       # Nutrition Dashboard, Food Search, Hydration, Planner
    │   ├── programme/       # 6-Day Push/Pull/Legs training routines
    │   ├── progress/        # Analytics, PRs, and Body Measurements
    │   ├── recovery/        # Daily Recovery check-in & Readiness score
    │   ├── today/           # Main Today Dashboard & daily habits checklist
    │   └── workout/         # Active workout tracking session
    └── theme/               # Color, Typography, and ForgeTheme
```

---

## 🧭 Navigation Hierarchy

FORGE uses a single `NavHost` containing **19 destinations** with smooth back-stack handling:

```
[Bottom Navigation Bar]
  ├── Today ("today")
  │     ├── Active Workout ("workout?dayId={dayId}")
  │     ├── Exercise Directory ("exercises") -> Detail ("exercise/{id}")
  │     ├── Workout History ("history")
  │     └── Nutrition OS ("nutrition")
  │           ├── Food Search ("food_search/{mealType}/{dateKey}")
  │           ├── Hydration Tracker ("hydration")
  │           ├── Macro Targets ("macro_targets")
  │           └── Meal Planner & Grocery ("meal_planner")
  ├── Train ("programme")
  │     └── Start Workout ("workout?dayId={dayId}")
  ├── Progress ("progress")
  │     ├── Personal Records ("personal_records")
  │     ├── Exercise Analytics ("exercise_analytics/{id}")
  │     └── Body Measurements ("body_measurements")
  ├── Coach ("coach")
  │     ├── Weekly Report ("weekly_report")
  │     └── Recovery Check-in ("recovery")
  └── Streaks ("consistency")
```

---

## 🧪 Testing & Verification

FORGE has a comprehensive automated unit test suite covering calculations, domain logic, state flows, and navigation routes:

```bash
./gradlew testDebugUnitTest
```

### Verified Test Suites (14 Classes, 0 Failures):
* `ComprehensiveFeatureAndButtonTest` — End-to-end verification of all 19 routes, bottom nav items, 1RM Epley calculations, recovery readiness formulas, and macro balances.
* `CoachEngineTest` — Deterministic coach insight evaluation rules.
* `ConsistencyAndStreakTest` — Daily streak calculation and level XP award logic.
* `ExerciseAndSeedDataTest` — Validates all 51 seeded exercises, muscle groups, and cues.
* `FoodDatabaseCoverageTest` — Validates 60+ pre-seeded Indian and fitness food items and macros.
* `NavigationRouteTest` — Deep link route parameter construction.
* `NutritionMacroCalculationTest` — Macro calories, serving size scaling, and hydration progress.
* `ProgrammeSplitAndFinisherTest` — 6-day split schedule and exercise order integrity.
* `ProgressAndAnalyticsTest` — Epley 1RM calculation and PR detection.
* `ProgressDashboardViewModelTest` — Stats aggregation and time-range filters.
* `RepositoryAndViewModelTest` — Repository data layer and state mapping.
* `TodayViewModelTest` — Today dashboard state and daily habit checklist toggle.
* `WorkoutExecutionLifecycleTest` — Active session state transitions and set completion.
* `WorkoutTimerLogicTest` — Rest timer countdown, `+15s` increment, and skip logic.

---

## 📦 How to Build & Install

### Prerequisites
* **Android Studio:** Ladybug (2024.2.1+) or newer
* **JDK:** Java 17 or higher
* **Android SDK:** Platform 35 (Android 15)
* **Gradle:** Included wrapper (8.7+)

### 1. Clone the Repository
```bash
git clone https://github.com/Harshasr42/forge-ai.git
cd forge-ai
```

### 2. Build the Debug APK
```bash
# On Linux / macOS:
./gradlew assembleDebug

# On Windows:
.\gradlew.bat assembleDebug
```
The compiled APK will be output to:  
`app/build/outputs/apk/debug/app-debug.apk`

### 3. Install Pre-Compiled APK Directly
If you have `adb` installed and your phone connected via USB or wireless debugging:
```bash
adb install FORGE.apk
```
Or simply copy `FORGE.apk` directly to your Android device and install it using your file manager.

---

## 🎨 Design Philosophy

* **Dark Athletic High-Contrast Palette:** `#101310` deep obsidian background with vibrant `#A3E635` (Electric Lime) accents for superior readability in gym lighting conditions.
* **Zero Cognitive Friction:** Big touch targets, quick set completion checkmarks, auto-advancing rest timers, and single-tap food and hydration logging.
* **Deterministic & Trustworthy:** No hallucinated "AI" predictions. Coaching insights, 1RM stats, and recovery recommendations are calculated strictly from evidence-based formulas and self-reported workout data.

---

## 📄 License & Credits

Developed with ❤️ by **[Harsha S R](https://github.com/Harshasr42)**.  
Licensed under the [MIT License](LICENSE).
