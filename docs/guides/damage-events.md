## Creating Damage Event
You can create a new damage event by extending `WKDamageEvent`:

```kotlin
// This will run for every damage event in the game
class TestDamageEvent : WKDamageEvent() {
    override fun onDamage(event: WKDamageEventData) {
		
    }
}
```

## Creating Player Damage Event
You can create a new player damage event by extending `WKPlayerDamageEvent`:

```kotlin
// This will run for every damage event involving players in the game
class TestPlayerDamageEvent : WKPlayerDamageEvent() {
    override fun onDamage(event: WKDamageEventData) {
        
    }
}
```

## Creating NPC Damage Event
You can create a new NPC damage event by extending `WKNPCDamageEvent`:

```kotlin
// This will run for every damage event involving NPCs in the game
class TestNPCDamageEvent : WKNPCDamageEvent() {
    override fun onDamage(event: WKDamageEventData) {
        
    }
}
```

## Registering Damage Events
You can register all damage events the same way:

```kotlin
override fun setup() {
    Events.registerSystem(TestDamageEvent())
    Events.registerSystem(TestPlayerDamageEvent())
    Events.registerSystem(TestNPCDamageEvent())
}

