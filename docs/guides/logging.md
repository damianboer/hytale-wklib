You can create a new `WKLogger` which has some shorthand methods for logging to the console

## Creating

```kotlin
WKLogger("LoggerName")
```

## Using

```kotlin
val logger = WKLogger("LoggerName")
logger.info("This is an info message")
logger.warn("This is a warning message")
logger.error("This is an error message")
```