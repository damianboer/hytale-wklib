## Supported types
The supported types are the same as [here](https://github.com/jacobc16/hytale-wklib/wiki/Configs#supported-types).

## Create Component and Events

```kotlin

// Component to hold custom player data
class MyPlayerData(
	var customField: String? = null,
) : WKComponent<MyPlayerData>("MyPlayerData") {
	override fun clone(): WKComponent<MyPlayerData> {
		return MyPlayerData(customField)
	}

	override fun buildCodec(
		fieldName: String,
		field: BuilderField.FieldBuilder<out Any?, in Any, out BuilderCodec.BuilderBase<out Any?, *>?>
	) {
		when (fieldName) {
			"customField" -> field.addValidator(Validators.nonNull())
		}
	}
}


// Event adding custom data when player is ready
class PlayerEvents {
	companion object {
		fun onPlayerReady(event: WKPlayerReadyEvent) {
			event.world.execute {
				val data = event.ensureAndGetComponent<MyPlayerData>("MyPlayerData")

				if (data.customField == null) {
					data.customField = "RandomNumber-${(1000..9999).random()}"
				}

				LOGGER.info("Player ${event.player.displayName} has customField: ${data.customField}")
			}
		}
	}
}
```

## Register Component and Events

Register the component and event in your main plugin class:

```kotlin
override fun setup() {
	Components.register(MyPlayerData::class.java)
	Events.onPlayerReady.register(PlayerEvents::onPlayerReady)
}
```