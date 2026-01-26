package com.walrusking.wklib.events.player

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.components.Components
import com.walrusking.wklib.helpers.ensureAndGetComponent
import com.walrusking.wklib.helpers.getWorld

/**
 * An event that is called when a player is ready.
 *
 * @param ref The reference to the player's entity store.
 * @param player The player who is ready.
 * @param readyId The ID of the ready event.
 */
class WKPlayerReadyEvent(
	ref: Ref<EntityStore>,
	player: Player,
	readyId: Int
) : PlayerReadyEvent(ref, player, readyId) {
	/**
	 * A reference to the player's entity store.
	 */
	val store = playerRef.store

	/**
	 * The world the player is in.
	 */
	val world = playerRef.getWorld()

	/**
	 * Ensures that the player has the specified component and returns it. Should only be called in world execution thread.
	 *
	 * @param T The type of the component.
	 * @param componentId The ID of the component.
	 * @return The component of the specified type.
	 * @throws IllegalArgumentException If the component type is not registered.
	 */
	fun <T : Component<EntityStore>> ensureAndGetComponent(componentId: String): T {
		val type = Components.getType<T>(componentId)
			?: throw IllegalArgumentException("$\"${componentId}\" component type not registered!")

		return playerRef.ensureAndGetComponent(type)
	}
}