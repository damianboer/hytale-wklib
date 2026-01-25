## Create EntityTickingSystem

### Supported Types
* WKEntityTickingSystem
* WKGlobalEntityTickingSystem
* WKDelayedEntitySystem
* WKGlobalDelayedEntitySystem
* WKRefChangeSystem

Create a new system by extending the `WKEntityTickingSystem` class:

```kotlin
class ExampleComponent(var exampleField: String) : WKComponent<ExampleComponent>("wklib:example_component") {
	override fun clone(): ExampleComponent {
		return ExampleComponent(exampleField)
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

## Register System

Register the system in your main plugin class:

```kotlin
override fun setup() {
	// This will register both the component and the system
	Components.registerBoth(ExampleComponent::class.java, ExampleSystem::class.java)
}
```

## Create BlockEntityTickingSystem

### Supported Types
* WKBlockEntityTickingSystem
* WKBlockGlobalEntityTickingSystem
* WKBlockDelayedEntitySystem
* WKBlockGlobalDelayedEntitySystem
* WKBlockRefChangeSystem

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

## Register Block System

Register the block system in your main plugin class:

```kotlin
override fun setup() {
	// This will register both the block component and the block system
	Components.registerBlockBoth(TestComponent::class.java, TestTicking::class.java)
}
```

## ECS Systems

### Create ECS System
Create a new ECS system by extending the `WKEntityEventSystem` class:

```kotlin
class TestSystem(eventType: Class<CraftRecipeEvent.Pre>) : WKEntityEventSystem<CraftRecipeEvent.Pre>(eventType) {
	override fun onExecute(data: EventData<CraftRecipeEvent.Pre>) {
		WKLogger("TestSystem").info("CraftRecipeEvent.Pre fireed for recipe: ${data.event.craftedRecipe.id}")
	}
}
```

### Register ECS System
Register the ECS system in your main plugin class:

```kotlin
override fun setup() {
	Events.registerSystem(TestSystem(CraftRecipeEvent.Pre::class.java))
}
```