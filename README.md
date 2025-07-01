# CoffeeChat

This project is a Spring Boot application for managing CoffeeChat application forms using Solapi.

## Configuration

Configuration values such as Solapi API credentials are expected in an `application.properties` file located in `coffeeChat/src/main/resources`.

1. Copy `application-sample.properties` and name the copy `application.properties`:
   
   ```bash
   cp coffeeChat/src/main/resources/application-sample.properties \
      coffeeChat/src/main/resources/application.properties
   ```
2. Edit the new `application.properties` file and replace the placeholder values with your actual credentials.

The repository ignores `application.properties`, so your private credentials remain local.

## Building

Use Gradle Wrapper to build the project:

```bash
./gradlew build
```

