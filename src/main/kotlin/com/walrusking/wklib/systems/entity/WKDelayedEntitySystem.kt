package com.walrusking.wklib.systems.entity

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * An abstract class that extends DelayedEntitySystem to process entities with a specific component type at defined intervals.
 *
 * @param T The type of component associated with the entities being processed.
 * @property componentType The ComponentType representing the type of component to be processed.
 * @constructor
 * Creates a WKDelayedEntitySystem with the specified component type and interval.
 *
 * @param intervalSec The interval in seconds at which the system should process entities.
 */
abstract class WKDelayedEntitySystem<T : Component<EntityStore>>(
	val componentType: ComponentType<EntityStore, T>,
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
		val component: T = p2.getComponent(p1, componentType) ?: return

		val data = EntityTickingData(p0, p1, p2, p3, p4, component)
		onTick(data)
	}

	/**
	 * Method to be overridden by subclasses to implement custom logic for processing entities.
	 *
	 * @param data The EntityTickingData containing information about the entity being processed and context.
	 */
	abstract fun onTick(data: EntityTickingData<T>)

	/**
	 * Overrides the default query to return a query that matches entities with the specified component type.
	 *
	 * @return The Query for entities with the specified component type.
	 */
	override fun getQuery(): Query<EntityStore> {
		return Query.and(componentType)
	}
}