package com.walrusking.wklib.events.player

import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.events.WKDamageEvent
import com.walrusking.wklib.events.WKDamageEventData

/**
 * A class that extends WKDamageEvent to handle player-specific damage events with custom logic.
 *
 * This is an ECS system.
 */
open class WKPlayerDamageEvent : WKDamageEvent() {
	/**
	 * Method to be overridden by subclasses to implement custom damage event handling logic.
	 *
	 * @param event The WKDamageEventData containing information about the damage event and context.
	 */
	override fun onDamage(event: WKDamageEventData) {}

	/**
	 * Overrides the default query to filter for Player entities.
	 *
	 * @return A Query that matches Player entities.
	 */
	override fun getQuery(): Query<EntityStore?> {
		return Query.and(Player.getComponentType())
	}
}