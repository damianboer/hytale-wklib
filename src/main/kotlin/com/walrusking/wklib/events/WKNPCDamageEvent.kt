package com.walrusking.wklib.events

import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.entities.NPCEntity

/**
 * A class that extends WKDamageEvent to handle NPC-specific damage events with custom logic.
 */
abstract class WKNPCDamageEvent : WKDamageEvent() {
	/**
	 * Method to be overridden by subclasses to implement custom damage event handling logic.
	 *
	 * @param event The WKDamageEventData containing information about the damage event and context.
	 */
	override fun onDamage(event: WKDamageEventData) {}

	/**
	 * Overrides the default query to filter for NPCEntity entities.
	 *
	 * @return A Query that matches NPCEntity entities.
	 */
	override fun getQuery(): Query<EntityStore?> {
		return Query.and(NPCEntity.getComponentType())
	}
}