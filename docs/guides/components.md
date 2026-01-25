## Create Component

Create a new component by extending the `WKComponent` class:

```kotlin
// First parameter of WKComponent is the component ID
class TestComponent(
	var exampleField: String = "Example",
) : WKComponent<TestComponent>("wklib:test_component") {
	override fun clone(): TestComponent {
		return TestComponent(exampleField)
	}
}
```

## Registering

Register the component in your main plugin class:

```kotlin
override fun setup() {
	Components.register(TestComponent::class.java)
}
```

## Create Block Components

If the component is going on a block, extend the `WKBlockComponent` class instead:

```kotlin
class TestComponent(
	var exampleField: String = "Example"
) : WKBlockComponent<TestComponent>("wklib:test_component") {
	override fun clone(): TestComponent {
		return TestComponent(exampleField)
	}

    // This can be added to any component type to add documentation or validators to a specific field.
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

## Registering Block Components

Register the block component in your main plugin class:

```kotlin
override fun setup() {
	Components.registerBlock(TestComponent::class.java)
}
```

## Getting Types

You can get a component type by using the following:

```kotlin
// Normal component
Components.getType("componentID")

// Block Component
Components.getBlockType("componentID")
```