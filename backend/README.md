# CYMI backend

This is the backend for Can You Make It.

## Technologies

-   Java
-   Gradle
-   Spring Boot
-   Spring Data JPA
-   Spring Security
-   Spring Web
-   PostgreSQL

## Development process

### Formatting

Eclipse is configured to automatically format and clean up files on save. This configuration is committed to version control and sits under `.settings`.

Currently, the Gradle build is not performing any formatting or style checks.

### Tests and coverage

After running the Gradle test task, we have:

-   A test summary at /build/reports/tests/test/index.html
-   A coverage report at /build/reports/jacoco/test/html/index.html

## Conventions

### Dealing with null

-   Null checks are performed at compile time using Spring null-safety annotations
    -   At package level, parameters, return values and fields are made non-nullable by default using `@org.springframework.lang.NonNullApi` and `@org.springframework.lang.NonNullFields`
        -   Exception: packages containing only entities do not use `@org.springframework.lang.NonNullFields` because this does not work well with JPA conventions
        -   Exception: the `com.mistermicheels.cymi.config.security` package does not use `@org.springframework.lang.NonNullApi` because this would make it impractical to implement the required method overrides
        -   Exception: packages containing only user input definitions do not use `@org.springframework.lang.NonNullFields` because this does not work well for classes that don't define a constructor
-   Avoid passing `null` or a null reference into methods
    -   Exception: using overloading to implement optional parameters
    -   Indicate nullable parameters with `@Nullable`
-   Use `Optional` for methods potentially returning null
    -   Exception: classes defining API responses or API errors use getters with `@Nullable` instead of returning `Optional` because using `Optional` would result in explicit `null` values in JSON responses
-   When defining user input, check required properties at runtime using `@NotNull` and explicitly mark optional properties as `@Nullable`
-   Code dealing with library/framework classes should take care to check for null as needed

### Returning entities from components

For each entity, we define one or more interfaces that describe the shape of retrieved data and the methods that code outside of the entity's component should be able to call on the entity.
When public component services are returning entities, they should use these interfaces in the return type rather than using the entity class itself.

Benefits:

-   Helps prevent the JPA "N+1 query" problem by making it harder to accidentally access fields that haven't been retrieved
    -   Code inside the component still needs to be careful about this, but code outside of the component can simply rely on the compiler when using retrieved entities
-   Helps prevent unintended changes or leakage of sensitive data by restricting the entity methods that can be used outside of the entity's component

### Package encapsulation for components

-   Don't make classes in components public unless we need to
    -   Go for package-private when we can, in order to hide the component's internals from the outside world
-   Don't make constructors in component classes public unless we need to
    -   Go for package-private when we can
-   Note that this becomes tricky for "nested" packages, as Java treats them as completely different packages
    -   Additional static checking might be needed in that case

### Dependency injection

Use constructor injection where we can (easier to test, can make dependency fields final, ...)

### App-specific configuration properties

Isolate configuration properties into separate POJOs using `@ConfigurationProperties`.

We use package-private setters. This offers some protection against accidental changes to the properties from within the code, but still provides a simple way to set defaults for the properties and also allow them to be overridden by specifying a value through properties file, environment variable, ...

### Input validation

In web part, only validate data types and the presence of properties. All more complex validations should sit in the components.
