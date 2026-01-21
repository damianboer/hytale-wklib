package com.walrusking.wklib.events

import com.hypixel.hytale.builtin.adventure.objectives.events.TreasureChestOpeningEvent
import com.hypixel.hytale.component.ComponentRegistryProxy
import com.hypixel.hytale.component.system.ISystem
import com.hypixel.hytale.event.EventRegistry
import com.hypixel.hytale.server.core.event.events.ShutdownEvent
import com.hypixel.hytale.server.core.event.events.player.*
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

class Events {
	companion object {
		private var eventRegistry: EventRegistry? = null
		private var entityStoreRegistry: ComponentRegistryProxy<EntityStore>? = null

		val onShutdown = EventManager.create<ShutdownEvent>()
		val onTreasureChestOpening = EventManager.create<TreasureChestOpeningEvent>()

		val onPlayerReady = EventManager.create<WKPlayerReadyEvent>()
		val onPlayerConnect = EventManager.create<PlayerConnectEvent>()
		val onPlayerDisconnect = EventManager.create<PlayerDisconnectEvent>()
		val onPlayerChat = EventManager.create<WKPlayerChatEvent>()
		val onPlayerSetupConnect = EventManager.create<PlayerSetupConnectEvent>()
		val onPlayerSetupDisconnect = EventManager.create<PlayerSetupDisconnectEvent>()
		val onPlayerAddedToWorld = EventManager.create<AddPlayerToWorldEvent>()
		val onPlayerDrainFromWorld = EventManager.create<DrainPlayerFromWorldEvent>()

		fun init(eventRegistry: EventRegistry, entityStoreRegistry: ComponentRegistryProxy<EntityStore>) {
			this.eventRegistry = eventRegistry
			this.entityStoreRegistry = entityStoreRegistry

			val registrations: List<(EventRegistry) -> Unit> = listOf(
				{ reg -> reg.registerGlobal(ShutdownEvent::class.java, GameEvents::onShutdown) },
				{ reg ->
					reg.registerGlobal(
						TreasureChestOpeningEvent::class.java,
						GameEvents::onTreasureChestOpening
					)
				},
				{ reg -> reg.registerGlobal(PlayerReadyEvent::class.java, PlayerEvents::onPlayerReady) },
				{ reg -> reg.registerGlobal(PlayerConnectEvent::class.java, PlayerEvents::onPlayerConnect) },
				{ reg -> reg.registerGlobal(PlayerDisconnectEvent::class.java, PlayerEvents::onPlayerDisconnect) },
				{ reg -> reg.registerGlobal(PlayerChatEvent::class.java, PlayerEvents::onPlayerChat) },
				{ reg -> reg.registerGlobal(PlayerSetupConnectEvent::class.java, PlayerEvents::onPlayerSetupConnect) },
				{ reg ->
					reg.registerGlobal(
						PlayerSetupDisconnectEvent::class.java,
						PlayerEvents::onPlayerSetupDisconnect
					)
				},
				{ reg -> reg.registerGlobal(AddPlayerToWorldEvent::class.java, PlayerEvents::onPlayerAddedToWorld) },
				{ reg ->
					reg.registerGlobal(
						DrainPlayerFromWorldEvent::class.java,
						PlayerEvents::onPlayerDrainFromWorld
					)
				}
			)

			this.eventRegistry?.let { reg ->
				registrations.forEach { it(reg) }
			}
		}

		fun registerSystem(system: ISystem<EntityStore>) {
			entityStoreRegistry?.registerSystem(system)
		}
	}
}