## Create Command

Create a new command by extending the `WKCommand` class:

```kotlin
class TestCommand : WKCommand("test", "A test command") {
	val message = withRequiredArg("message", "The message to send", ArgTypes.STRING)

	override fun onExecute(data: CommandData) {
		data.sendMessage("Test command executed! Message: ${data.arg<String>(message)}")
	}
}
```

## Register Command

Register the command in your main plugin class:

```kotlin
override fun setup() {
	Commands.register(TestCommand())
}
```