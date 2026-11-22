# GYM APP (`com.forge.app`) — Project Guide

## Tech Stack & Architecture

- **Language / UI:** Kotlin, Jetpack Compose, Material 3
- **Architecture:** Clean Architecture / MVVM (`ui/` -> `domain/` -> `data/repository/` -> local DB / network)
- **Async & State:** Kotlin Coroutines, `StateFlow` / `Flow`, ViewModel (`viewModelScope`)
- **Persistence & DI:** Room Database, Hilt (version 2.54)
- **Versioning:** Kotlin 2.1.0, Agp 8.7.3, Compose 2024.12.01

## Key Directories

- `app/src/main/kotlin/com/forge/app/ui/` — Compose screens, components, navigation, and ViewModels
- `app/src/main/kotlin/com/forge/app/domain/` — Models, use cases, repository interfaces
- `app/src/main/kotlin/com/forge/app/data/` — Room entities/DAOs, `RepositoryImpl` classes (e.g., `ExerciseRepositoryImpl.kt`), data sources
- `app/src/main/kotlin/com/forge/app/ui/theme/` — Theme customization and color system
- `app/src/main/kotlin/com/forge/app/ui/components/` — Reusable UI components
- `app/src/main/kotlin/com/forge/app/ui/screens/` — Feature-specific screens organized by domain
- `app/src/main/kotlin/com/forge/app/data/db/` — Room database files and migrations
- `app/src/main/kotlin/com/forge/app/data/seed/` — Database seeding utilities

## Build & Verification Commands

- Compile debug APK: `./gradlew assembleDebug`
- Run unit tests: `./gradlew testDebugUnitTest`
- Lint check: `./gradlew lintDebug`
- Compile Kotlin: `./gradlew compileDebugKotlin`

## Coding Rules

- **Targeted Edits Only:** Read only specific files needed for the task; never scan entire repository unless asked.
- **Error Handling:** Wrap repository I/O and Room calls in `Result<T>` or sealed UI states (`Loading`, `Success`, `Error`).
- **Compose Best Practices:** Keep Composables stateless (hoist state to ViewModels), use `collectAsStateWithLifecycle()`, avoid heavy work in composition.
- **Coroutines:** Inject `CoroutineDispatcher`s; use `Dispatchers.IO` for database/network calls, never block `Dispatchers.Main`.
- **Verification:** Run `./gradlew compileDebugKotlin` after editing Kotlin files to check for errors.