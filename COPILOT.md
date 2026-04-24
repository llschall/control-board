# Copilot Code Generation Guidelines

## Project Context

- **Framework**: Spring Boot 3.x
- **Language**: Java 25
- **Build Tool**: Gradle
- **UI Framework**: Swing with XChart for charts
- **Architecture**: MVVM (Model-View-ViewModel)

## Code Style Rules

### Naming Conventions

- **Classes**: PascalCase (e.g., `AppWindow`, `CounterViewModel`)
- **Methods/Variables**: camelCase (e.g., `updateCounterDisplay()`, `counterLabel`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_COUNTER_VALUE`)

### Package Structure

```
org.llschall.boot.controller/
├── models/          (Data models: CounterModel)
├── viewmodels/      (ViewModel classes: CounterViewModel)
├── views/           (Swing components: AppWindow)
└── services/        (Business logic)
```

## Architecture Patterns

### MVVM Pattern (Must Follow)

- **Model**: Data + Logic (e.g., `CounterModel`)
- **ViewModel**: State Management (e.g., `CounterViewModel`)
- **View**: UI Components (e.g., `AppWindow`)

### Component Annotations

- Use `@Component` for Spring-managed beans
- Use `@EventListener(ApplicationReadyEvent.class)` for startup logic
- Use `@Autowired` or constructor injection for dependencies

## Coding Standards

### Methods

- Keep methods focused and single-responsibility
- Add JavaDoc comments for public methods
- Use meaningful method names (avoid `doSomething()`, use `updateDisplay()`)

### Logging

- Use `LoggerFactory.getLogger(ClassName.class)`
- Log at appropriate levels: DEBUG for verbose, INFO for important, ERROR for failures

## Dependency Usage

### XChart Integration

- Use `CategoryChart` for bar/line charts
- Use `XChartPanel<CategoryChart>` for Swing integration
- Store chart history in `List<Integer>` or similar collections

### Swing Components

- Use `BorderLayout` for main panels
- Use `FlowLayout` for control panels
- Always set `setDefaultCloseOperation()` on frames

## Testing Requirements

- Create unit tests for all model classes
- Use `@SpringBootTest` for integration tests
- Test class naming: `[ClassName]Test` or `[ClassName]Tests`
- Place tests in `src/test/java` with matching package structure

## DO's ✅

- ✅ Use Spring Boot conventions
- ✅ Implement MVVM architectural pattern
- ✅ Add proper logging with SLF4J
- ✅ Handle headless environments
- ✅ Create comprehensive unit tests
- ✅ Use constructor injection for dependencies
- ✅ Keep UI logic separate from business logic
- ✅ Document public APIs with JavaDoc

## DON'Ts ❌

- ❌ Don't use static fields for UI components (except Logger)
- ❌ Don't create UI components outside of event listeners
- ❌ Don't mix business logic with view code
- ❌ Don't use raw types for collections
- ❌ Don't skip error handling in UI code
- ❌ Don't ignore headless mode

## Gradle Configuration

- Dependencies in `build.gradle`
- JDK version: 25
- Use `implementation` for production code
- Use `testImplementation` for test dependencies

---
*Guidelines for GitHub Copilot code generation | Last Updated: April 24, 2026*

