# Copilot instructions for this repository

This file is intended to help future Copilot sessions (and other AI assistants) quickly understand how to build, test, run, and follow repository-specific conventions.

---

## Build, run, test, and CI commands

- Use the Gradle wrapper (do not rely on system Gradle):
  - Full build: `./gradlew build`
  - Run application (desktop app): `./gradlew bootRun`
  - Run tests: `./gradlew test`
  - Run a single test class: `./gradlew test --tests "org.llschall.control.board.YourTestClass"`
  - Run a single test method: `./gradlew test --tests "org.llschall.control.board.YourTestClass.yourTestMethod"`

- CI: GitHub Actions workflow `.github/workflows/gradle.yml` uses JDK 25 and runs `./gradlew build`.

- Notes:
  - The project uses a Java toolchain configured to Java 25 (see `build.gradle`).
  - `bootRun` is configured with `-Djava.awt.headless=false` in Gradle tasks to allow Swing UI during local runs.

---

## High-level architecture (big picture)

- Framework: Spring Boot (DI and lifecycle)
- Languages: Mixed Kotlin and Java in the same JVM app
  - Models are implemented in Kotlin (e.g., `CounterModel.kt`)
  - ViewModels and Views are implemented in Java (e.g., `CounterViewModel.java`, `AppWindow.java`)
- UI: Swing (desktop application) with XChart for charts
- Pattern: MVVM (Model — ViewModel — View)
  - Model: domain/state and core business logic
  - ViewModel: exposes model state/commands to the View; keeps UI logic out of the model
  - View: Swing components that observe ViewModel and dispatch actions
- Persistence/Runtime: SQLite JDBC used at runtime (configured as runtimeOnly in Gradle)
- Embedded/Peripheral: Arduino sketch under `ino/control-board/control-board.ino` — the Java app communicates with the Arduino via the `ardwloop` library (dependency declared in Gradle)

---

## Key repository-specific conventions and patterns

- Package root: `org.llschall.control.board` and the following packages are used intentionally:
  - `models/` — data + business logic (Kotlin files)
  - `viewmodels/` — ViewModel classes (Java)
  - `views/` — Swing UI components
  - `services/` — spring-managed services and hardware/communication code

- MVVM must be followed: do not move business logic into Views. All UI-only code belongs in `views` or in the ViewModel.

- Spring beans and injection
  - Prefer constructor injection
  - Use `@Component` on beans managed by Spring
  - Startup hooks use `@EventListener(ApplicationReadyEvent.class)` where needed

- UI and threading
  - Keep Swing UI updates on the Event Dispatch Thread (EDT). Use Swing utilities (EventQueue/SwingUtilities.invokeLater) when creating/updating frames.
  - Do not create static UI components (except Logger). Avoid static mutable UI state.

- Logging
  - Use SLF4J: `LoggerFactory.getLogger(YourClass.class)` and appropriate levels (DEBUG/INFO/ERROR)

- Testing
  - Unit tests for models are expected in `src/test/java` (mirroring package structure).
  - Use `@SpringBootTest` for integration tests when Spring context is required.
  - Naming: `[ClassName]Test` or `[ClassName]Tests`.

- XChart usage
  - Use `CategoryChart` for charts and `XChartPanel<CategoryChart>` to embed in Swing. Keep chart data updates separated from rendering logic.

- Headless/CI considerations
  - CI workflow runs headless; avoid requiring an interactive UI for tests. The project already configures `bootRun` with `java.awt.headless=false` for local runs—be cautious when writing tests that instantiate frames.

---

## Files and places to check (quick map)

- Application main and Spring config: `src/main/java` and `src/main/kotlin` under `org.llschall.control.board`
- Arduino sketch: `ino/control-board/control-board.ino`
- Gradle build: `build.gradle`
- Copilot guidance (existing): `COPILOT.md` — contains voice-of-team rules; consult it for naming and code generation guidance.

---

## Where this integrates with existing docs

This file consolidates build/test/run commands and high-level patterns already present in `README.md` and `COPILOT.md`. For coding-style and generation guidance prefer `COPILOT.md` (it contains detailed naming, annotations, and patterns).

---

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>
