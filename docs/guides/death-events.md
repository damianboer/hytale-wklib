## Creating Death Event

You can create a new death event by extending `WKOnDeathSystem`:

```kotlin
class TestDeathEvent : WKOnDeathSystem() {
	override fun onDeath(event: WKOnDeathEvent) {
		val deathInfo = event.deathInfo

		if (deathInfo != null) {
			WKLogger("WKLib: TestDeathEvent").info(
				"Damage Amount: ${deathInfo.amount} | Damage Info Cause: ${event.cause?.id} | Killer: ${event.killerPlayer?.displayName}"
			)
		} else {
			WKLogger("WKLib: TestDeathEvent").info("No Damage Info available.")
		}
	}

	override fun getQuery(): Query<EntityStore> {
		return Query.any()
	}
}
```

## Creating Player Death Event

You can create a new death event by extending `WKPlayerDeathSystem`:

```kotlin
class TestDeathEvent : WKPlayerDeathSystem() {
	override fun onDeath(event: WKPlayerDeathEvent) {
		WKLogger("PlayerDeath").info("${event.player?.displayName} has died!")
	}
}
```

## Registering Death Events

You can register player and general death events the same way:

```kotlin
override fun setup() {
	Events.registerSystem(TestDeathEvent())
}
```

## WKOnDeathEvent

The `WKOnDeathEvent` will let you get the killer using `getKiller(componentType)`. You can do the following to get the player that killed something/someone:

```kotlin
event.getKiller(Player.getComponentType())

// You can also just use the following which does the same thing for you
event.killerPlayer
```