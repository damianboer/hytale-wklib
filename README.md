# WKLib Documentation

## Plugins

Create a new plugin by extending the `WKPlugin` class:

It has a built-in LOGGER you can use for logging.

```kotlin
class ExamplePlugin(init: JavaPluginInit) : WKPlugin("ExamplePlugin", init) {
	override fun setup() {
		LOGGER.info("ExamplePlugin has been set up!")
	}
}
```

## Events

Supported events:

- onShutdown
- onTreasureChestOpening
- onPlayerReady
- onPlayerConnect
- onPlayerDisconnect
- onPlayerSetupConnect
- onPlayerSetupDisconnect
- onPlayerAddedToWorld
- onPlayerDrainFromWorld

```kotlin
class ExamplePlugin(init: JavaPluginInit) : WKPlugin("ExamplePlugin", init) {
	companion object {
		fun onPlayerReady(event: WKPlayerReadyEvent) {
			LOGGER.info("Player ${event.player.displayName} is ready!")
		}
	}

	override fun setup() {
		Events.onPlayerReady.register(ExamplePlugin::onPlayerReady)
	}
}

```

## Components

Create a new component by extending the `WKComponent` class:

```kotlin
class TestComponent(
	var exampleField: String = "Example",
) : WKComponent<TestComponent>("wklib:test_component") {
	override fun clone(): TestComponent {
		return TestComponent(exampleField)
	}
}
```

Register the component in your main plugin class:

```kotlin
override fun setup() {
	Components.register(TestComponent::class.java)
}
```

If the component is going on a block, extend the `WKBlockComponent` class instead:

```kotlin
class TestComponent(var exampleField: String = "Example") : WKBlockComponent<TestComponent>("wklib:test_component") {
	override fun clone(): TestComponent {
		return TestComponent(exampleField)
	}

	override fun buildCodec(
		fieldName: String,
		field: BuilderField.FieldBuilder<out Any?, in Any, out BuilderCodec.BuilderBase<out Any?, *>?>
	) {
		when (fieldName) {
			"exampleField" -> field.documentation("An example field for demonstration purposes.").addValidator(
				Validators.nonNull()
			)
		}
	}
}
```

Register the block component in your main plugin class:

```kotlin
override fun setup() {
	Components.registerBlock(TestComponent::class.java)
}
```

## Systems

Create a new system by extending the `WKEntityTickingSystem` class:

```kotlin
class ExampleComponent(var exampleField: String) : WKComponent<ExampleComponent>("wklib:example_component") {
	override fun clone(): ExampleComponent {
		return ExampleComponent(exampleField)
	}

	override fun buildCodec(
		fieldName: String,
		field: BuilderField.FieldBuilder<out Any?, in Any, out BuilderCodec.BuilderBase<out Any?, *>?>
	) {

	}
}

class ExampleSystem(componentType: ComponentType<EntityStore, ExampleComponent>) :
	WKEntityTickingSystem<ExampleComponent>(
		componentType
	) {
	override fun onTick(data: EntityTickingData<ExampleComponent, EntityStore>) {
		val entityIndex = data.index
		val exampleField = data.component.exampleField
		WKLogger("TestTicking").info("Ticking entity $entityIndex with exampleField: $exampleField")
	}
}
```

Register the system in your main plugin class:

```kotlin
override fun setup() {
	// This will register both the component and the system
	Components.registerBoth(ExampleComponent::class.java, ExampleSystem::class.java)
}
```

Create a new block system by extending the `WKBlockEntityTickingSystem` class:

```kotlin
class TestComponent(var exampleField: String = "Example") : WKBlockComponent<TestComponent>("wklib:test_component") {
	// You can modify the fields here to add documentation and validators
	override fun buildCodec(
		fieldName: String,
		field: BuilderField.FieldBuilder<out Any?, in Any, out BuilderCodec.BuilderBase<out Any?, *>?>
	) {
		when (fieldName) {
			"exampleField" -> field.documentation("An example field for demonstration purposes.").addValidator(
				Validators.nonNull()
			)
		}
	}

	override fun clone(): Component<ChunkStore> {
		return TestComponent(exampleField)
	}
}

class TestTicking(componentType: ComponentType<ChunkStore, TestComponent>) : WKBlockEntityTickingSystem<TestComponent>(
	componentType
) {
	override fun onTick(data: EntityTickingData<TestComponent, ChunkStore>) {
		val entityIndex = data.index
		val exampleField = data.component.exampleField
		WKLogger("TestTicking").info("Ticking entity $entityIndex with exampleField: $exampleField")
	}
}
```

Register the block system in your main plugin class:

```kotlin
override fun setup() {
	// This will register both the block component and the block system
	Components.registerBlockBoth(TestComponent::class.java, TestTicking::class.java)
}
```

## ECS Systems

Create a new ECS system by extending the `WKEntityEventSystem` class:

```kotlin
class TestSystem(eventType: Class<CraftRecipeEvent.Pre>) : WKEntityEventSystem<CraftRecipeEvent.Pre>(eventType) {
	override fun onExecute(data: EventData<CraftRecipeEvent.Pre>) {
		WKLogger("TestSystem").info("CraftRecipeEvent.Pre fireed for recipe: ${data.event.craftedRecipe.id}")
	}
}
```

Register the ECS system in your main plugin class:

```kotlin
override fun setup() {
	Events.registerSystem(TestSystem(CraftRecipeEvent.Pre::class.java))
}
```

## Commands

Create a new command by extending the `WKCommand` class:

```kotlin
class TestCommand : WKCommand("test", "A test command") {
	val message = withRequiredArg("message", "The message to send", ArgTypes.STRING)

	override fun onExecute(data: CommandData) {
		data.sendMessage("Test command executed! Message: ${data.arg<String>(message)}")
	}
}
```

Register the command in your main plugin class:

```kotlin
override fun setup() {
	Commands.register(TestCommand())
}
```

## Persistent Player Data

```kotlin

// Component to hold custom player data
class MyPlayerData(var customField: String? = null) : WKComponent<MyPlayerData>("MyPlayerData") {
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

Register the component and event in your main plugin class:

```kotlin
override fun setup() {
	Components.register(MyPlayerData::class.java)
	Events.onPlayerReady.register(PlayerEvents::onPlayerReady)
}
```