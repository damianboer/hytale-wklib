package com.walrusking.wklib.systems.block

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.walrusking.wklib.components.Components
import com.walrusking.wklib.systems.entity.BaseEntityTickingData

/**
 * An abstract class that extends EntityTickingSystem to handle ticking logic for block entities with a specific component.
 *
 * @param T The type of Component this system will operate on.
 * @property componentType The ComponentType of the component to be processed during ticking.
 */
abstract class WKBlockEntityTickingSystem<T : Component<ChunkStore>>(val componentType: ComponentType<ChunkStore, T>) :
	EntityTickingSystem<ChunkStore>() {

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
	 * @param data The BlockEntityTickingData containing information about the ticking context and component.
	 */
	abstract fun onTick(data: BlockEntityTickingData<T>)

	/**
	 * Overrides the default query to return a query that matches entities with the specified component type.
	 *
	 * @return A Query for ChunkStore that matches the specified component type.
	 */
	override fun getQuery(): Query<ChunkStore>? {
		return Query.and(componentType)
	}
}

/**
 * Data class encapsulating information about a block entity ticking event.
 *
 * @constructor
 * Creates a new BlockGlobalEntityTickingData instance.
 *
 * @param deltaTime The time elapsed since the last tick.
 * @param index The index of the entity in the chunk.
 * @param chunk The ArchetypeChunk containing the entities.
 * @param store The Store managing the entities.
 * @param commandBuffer The CommandBuffer for issuing commands to entities.
 */
class BlockGlobalEntityTickingData(
	deltaTime: Float,
	index: Int,
	chunk: ArchetypeChunk<ChunkStore>,
	store: Store<ChunkStore>,
	commandBuffer: CommandBuffer<ChunkStore>
) : BaseEntityTickingData<ChunkStore>(
	deltaTime,
	index,
	chunk,
	store,
	commandBuffer
)

/**
 * Data class encapsulating information about a block entity ticking event with a specific component.
 *
 * @constructor
 * Creates a new BlockEntityTickingData instance.
 *
 * @param deltaTime The time elapsed since the last tick.
 * @param index The index of the entity in the chunk.
 * @param chunk The ArchetypeChunk containing the entities.
 * @param store The Store managing the entities.
 * @param commandBuffer The CommandBuffer for issuing commands to entities.
 * @param component The specific component instance associated with the entity.
 */
class BlockEntityTickingData<T>(
	deltaTime: Float,
	index: Int,
	chunk: ArchetypeChunk<ChunkStore>,
	store: Store<ChunkStore>,
	commandBuffer: CommandBuffer<ChunkStore>,
	val component: T
) : BaseEntityTickingData<ChunkStore>(
	deltaTime,
	index,
	chunk,
	store,
	commandBuffer,
) {
	/**
	 * Retrieves a component of the block entity by its component ID.
	 *
	 * @param Comp The type of the component to retrieve.
	 * @param componentId The string identifier of the component.
	 * @return The component instance if found, otherwise null.
	 */
	fun <Comp : Component<ChunkStore>> getComponent(componentId: String): Comp? {
		val type = Components.getBlockType<Comp>(componentId) ?: return null

		return chunk.getComponent(index, type)
	}
}