This library contains helper functions, utilities, and other general components to make modding easier.

You can create a new plugin by inheriting from WKPlugin It is not required to use, but it does include a LOGGER for you.
It also contains some helpful methods for interacting with configs.

```kotlin
// The first parameter of WKPlugin is the name of the logging.md
class ExamplePlugin(init: JavaPluginInit) : WKPlugin("MyPlugin", init) {
	override fun setup() {
		LOGGER.info("Plugin Setup")
	}
}
```