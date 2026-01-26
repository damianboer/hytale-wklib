package com.walrusking.wklib.systems.entity

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Abstract class for creating global delayed entity systems.
 *
 * @constructor
 * Creates a WKGlobalDelayedEntitySystem with the specified interval in seconds.
 *
 * @param intervalSec The interval in seconds at which the system should tick.
 */
abstract class WKGlobalDelayedEntitySystem(
	intervalSec: Float
) :
	DelayedEntitySystem<EntityStore>(intervalSec) {

	override fun tick(
		p0: Float,
		p1: Int,
		p2: ArchetypeChunk<EntityStore>,
		p3: Store<EntityStore>,
		p4: CommandBuffer<EntityStore>
	) {
		val data = GlobalEntityTickingData(p0, p1, p2, p3, p4)
		onTick(data)
	}

	/**
	 * Method to be overridden by subclasses to implement custom ticking logic.
	 *
	 * @param data The GlobalEntityTickingData containing information about the tick context.
	 */
	abstract fun onTick(data: GlobalEntityTickingData)
}
