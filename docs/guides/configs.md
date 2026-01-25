## Supported Types
- `UUID` — field: `var id: UUID` — example value: `UUID.fromString("123e4567-e89b-12d3-a456-426614174000")` (serialized as string)
- `String` — field: `var name: String` — example value: `"example"`
- `Int` — field: `var count: Int` — example value: `42`
- `Double` — field: `var ratio: Double` — example value: `3.14`
- `Float` — field: `var speed: Float` — example value: `1.5f`
- `Long` — field: `var timestamp: Long` — example value: `1620000000000L`
- `Boolean` — field: `var enabled: Boolean` — example value: `true`
- `Map<String, V>` — field: `var attributes: HashMap<String, Int>` — example value: `hashMapOf("hp" to 100, "mp" to 50)`
- `CLASS` (custom POJO/config) — field: `var settings: MyConfig` — example value: `MyConfig()` (serialized using `buildConfigCodec`)
- `WKBaseComponent` (component) — field: `var component: MyComponent` — example value: `MyComponent()` (serialized using `buildComponentCodec`)

> [!IMPORTANT]  
> Map keys must be `String`. Map values may be simple types or custom classes.


> [!NOTE]  
> Collections such as lists, arrays, and set are not supported currently

## Create Config

You can create the config how every you like, here is an example:

```kotlin
class ExampleConfig {
	companion object {
		lateinit var config: Config<ExampleConfig>
	}

	val exampleSetting: String = "defaultValue"
	val integerSetting: Int = 42
	val booleanSetting: Boolean = true
}
```

## Setup Config

Inside a `WKPlugin` you must set your config:

> [!IMPORTANT]  
> You must do this in the init method and not in your setup method

```kotlin
init {
	// This will set the config up with your plugin name (same one used in the logging.md):
	ExampleConfig.config = withConfig()

	// This will set the config to use the name specified
	ExampleConfig.config = withConfig("MyConfigName") // Ends up being myconfigname.json
}
```

## Loading Config

To load your config you can run the following for each config in plugin setup:

```kotlin
override fun setup() {
	ConfigUtil.loadConfig(this, ExampleConfig.config)

	// Get the config's data
	val config = ExampleConfig.config.get()

	LOGGER.info("Loaded Config - Example Setting: ${config.exampleSetting}, Integer Setting: ${config.integerSetting}, Boolean Setting: ${config.booleanSetting}")
}
```

## Creating and Loading Configs Outside of Plugin

You can create and load configs outside of plugins by doing the following:


> [!IMPORTANT]  
> Make sure you use var so the configs can be upgraded internally

### Config Example
```kotlin
class ExampleConfig(
	var id: String,
	var name: String,
):WKConfig() {
	// Requires a default constructor
	constructor() : this("","")
}
```

### Creating/Loading

In your main plugin class you can do the following or make it a static class and call elsewhere:

```kotlin
override fun setup() {
	// Create new config with default data you want to save
	val example = ExampleConfig("example", "Something Cool")

	// This will get the config if it exists or create a new one and return it, you can then do .get on the config to get its data
	val config = getOrCreateConfig(example.id, example)
}
```

### Upgrading

If you change the structure of the config you can do the following to upgrade it:

```kotlin
class ExampleConfig(
	var id: String,
	var name: String,
	val missingInt: Int
	// Increase the config version when making changes, default is 1
):WKConfig(2) {
	// Requires a default constructor
	constructor() : this("","", 0)
	
	override fun upgradeConfig(oldVersion: Int): List<ConfigChange> {
		// Check the old version and return a list of changes made since then
		when (oldVersion) {
			1 -> {
				return listOf(
					// This will add a new field called missingInt to the config when its loaded and will set the value to the value specified when you create the config
					ConfigChange("missingInt", ConfigChangeType.ADDED)
				)
			}
		}

		return emptyList()
	}
}
```