# Backend

This is the backend for Can You Make It.

## Technologies

- Java
- Gradle
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Web
- PostgreSQL

## Development process

After running the Gradle test task, we have:
- A test summary at /build/reports/tests/test/index.html
- A coverage report at /build/reports/jacoco/test/html/index.html

## Conventions

### Dealing with null

- Avoid passing null into a method
    - Exception when using null to be explicit about the absence of a value and the method is in the same class
    - Exception when using method overloading as a mechanism to implement optional parameters
- Use Optional for methods potentially returning null
- Code dealing with user input or library/framework classes should take care to check for null as needed

### Package encapsulation for components

- Don't make classes in components public unless we need to
    - Go for package-private when we can, in order to hide the component's internals from the outside world
- Don't make constructors in component classes public unless we need to
    - Go for package-private when we can
- Note that this becomes tricky for "nested" packages, as Java treats them as completely different packages
    - Additional static checking might be needed in that case

### Dependency injection

Use constructor injection where we can (easier to test, can make dependency fields final, ...)

### App-specific configuration properties

Isolate configuration properties into separate POJOs using `@ConfigurationProperties`.

We use package-private setters. This offers some protection against accidental changes to the properties from within the code, but still provides a simple way to set defaults for the properties and also allow them to be overridden by specifying a value through properties file, environment variable, ...

### Input validation

In web part, only validate data types and the presence of propterties. All more complex validations should sit in the components.