package com.walrusking.wklib.systems.player

import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.helpers.getComponent
import com.walrusking.wklib.systems.WKBaseOnDeathSystem
import com.walrusking.wklib.systems.WKOnDeathEvent

/**
 * An abstract system that handles player death events by extending WKBaseOnDeathSystem.
 */
abstract class WKPlayerDeathSystem : WKBaseOnDeathSystem<WKPlayerDeathEvent>(
) {
	/**
	 * Method to be overridden by subclasses to implement custom player death event handling logic.
	 *
	 * @param event The WKPlayerDeathEvent containing information about the player death event and context.
	 */
	override fun onDeath(event: WKPlayerDeathEvent) {

	}

	override fun createEvent(
		ref: Ref<EntityStore>,
		deathComponent: DeathComponent,
		store: Store<EntityStore>,
		commandBuffer: CommandBuffer<EntityStore>
	): WKPlayerDeathEvent {
		return WKPlayerDeathEvent(ref, deathComponent, store, commandBuffer)
	}

	/**
	 * Overrides the default query to filter for entities that have the Player component.
	 *
	 * @return The Query for entities with the Player component.
	 */
	override fun getQuery(): Query<EntityStore> {
		return Query.and(Player.getComponentType())
	}
}

/**
 * Data class encapsulating information about a player death event.
 *
 * @property player The Player entity that has died.
 */
class WKPlayerDeathEvent(
	ref: Ref<EntityStore>,
	deathComponent: DeathComponent,
	store: Store<EntityStore>,
	commandBuffer: CommandBuffer<EntityStore>
) : WKOnDeathEvent(
	ref,
	deathComponent,
	store,
	commandBuffer
) {
	/** The Player entity that has died. */
	val player: Player? = ref.getComponent(Player.getComponentType())
}