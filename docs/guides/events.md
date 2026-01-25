> [!NOTE]  
> Events should be static methods.

## Supported Events

### Game Events

#### onShutdown

Type: `ShutdownEvent`

Example:

```kotlin
fun onShutdown(event: ShutdownEvent?) {}
```

#### onTreasureChestOpening

Type: `TreasureChestOpeningEvent`

Example:

```kotlin
fun onTreasureChestOpening(event: TreasureChestOpeningEvent) {}
```

### Player Events

#### onPlayerReady

Type: `WKPlayerReadyEvent`

Example:

```kotlin
fun onPlayerReady(event: WKPlayerReadyEvent) {}
```

#### onPlayerConnect

Type: `PlayerConnectEvent`

Example:

```kotlin
fun onPlayerConnect(event: PlayerConnectEvent) {}
```

#### onPlayerDisconnect

Type: `PlayerDisconnectEvent`

Example:

```kotlin
fun onPlayerDisconnect(event: PlayerDisconnectEvent) {}
```


#### onPlayerChat

Type: `WKPlayerChatEvent`

Example:

```kotlin
fun onPlayerChat(event: WKPlayerChatEvent) {}
```

#### onPlayerSetupConnect

Type: `PlayerSetupConnectEvent`

Example:

```kotlin
fun onPlayerSetupConnect(event: PlayerSetupConnectEvent) {}
```

#### onPlayerSetupDisconnect

Type: `PlayerSetupDisconnectEvent`

Example:

```kotlin
fun onPlayerSetupDisconnect(event: PlayerSetupDisconnectEvent) {}
```

#### onPlayerAddedToWorld

Type: `AddPlayerToWorldEvent`

Example:

```kotlin
fun onPlayerAddedToWorld(event: AddPlayerToWorldEvent) {}
```

#### onPlayerDrainFromWorld

Type: `DrainPlayerFromWorldEvent`

Example:

```kotlin
fun onPlayerDrainFromWorld(event: DrainPlayerFromWorldEvent) {}
```

## Registering events

You can register events like so:
```kotlin
Events.onPlayerReady.register(ClassName::onPlayerReady)
```