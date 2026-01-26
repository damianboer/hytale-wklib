package com.walrusking.wklib.systems.block

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore

/**
 * An abstract class that extends DelayedEntitySystem to handle block entity ticking with custom logic.
 *
 * @param T The type of Component this system will process.
 * @property componentType The ComponentType of the component to be processed.
 * @constructor
 * Creates a new WKBlockDelayedEntitySystem with the specified component type and interval.
 *
 * @param intervalSec The interval in seconds at which the system should tick.
 */
abstract class WKBlockDelayedEntitySystem<T : Component<ChunkStore>>(
	val componentType: ComponentType<ChunkStore, T>,
	intervalSec: Float
) :
	DelayedEntitySystem<ChunkStore>(intervalSec) {

	override fun tick(
		p0: Float,
		p1: Int,
		p2: ArchetypeChunk<ChunkStore>,
		p3: Store<ChunkStore>,
		p4: CommandBuffer<ChunkStore>
	) {
		val component: T = p2.getComponent(p1, componentType) ?: return

		val data = BlockEntityTickingData(p0, p1, p2, p3, p4, component)
		onTick(data)
	}

	/**
	 * Method to be overridden by subclasses to implement custom ticking logic for block entities.
	 *
	 * @param data The BlockEntityTickingData containing information about the tick and context.
	 */
	abstract fun onTick(data: BlockEntityTickingData<T>)

	/**
	 * Overrides the default query to return a query that matches the specified component type.
	 *
	 * @return A Query for ChunkStore that matches the component type.
	 */
	override fun getQuery(): Query<ChunkStore>? {
		return Query.and(componentType)
	}
}