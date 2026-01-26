package com.walrusking.wklib.systems.entity

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.components.Components

/**
 * An abstract class that extends EntityTickingSystem to handle ticking logic for entities with a specific component.
 *
 * @param T The type of component associated with the entity.
 * @property componentType The ComponentType representing the specific component to be processed.
 */
abstract class WKEntityTickingSystem<T : Component<EntityStore>>(val componentType: ComponentType<EntityStore, T>) :
	EntityTickingSystem<EntityStore>() {

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
	 * Method to be overridden by subclasses to implement custom ticking logic.
	 *
	 * @param data The EntityTickingData containing information about the entity and context.
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

/**
 * Base class encapsulating common data for entity ticking operations.
 *
 * @param StoreType The type of store associated with the entity.
 * @property deltaTime The time elapsed since the last tick.
 * @property index The index of the entity within the chunk.
 * @property chunk The ArchetypeChunk containing the entity.
 * @property store The Store managing the entities.
 * @property commandBuffer The CommandBuffer for queuing commands.
 */
abstract class BaseEntityTickingData<StoreType>(
	val deltaTime: Float,
	val index: Int,
	val chunk: ArchetypeChunk<StoreType>,
	val store: Store<StoreType>,
	val commandBuffer: CommandBuffer<StoreType>,
) {
	/**
	 * Retrieves a component of the specified type from the entity.
	 *
	 * @param Comp The type of component to retrieve.
	 * @param type The ComponentType representing the specific component.
	 * @return The component of the specified type, or null if not found.
	 */
	fun <Comp : Component<StoreType>> getComponent(type: ComponentType<StoreType, Comp>): Comp? {
		return chunk.getComponent(index, type)
	}

	/**
	 * Retrieves a reference to the entity.
	 *
	 * @return The Ref representing the entity.
	 */
	fun getRef(): Ref<StoreType> {
		return chunk.getReferenceTo(index)
	}
}

/**
 * Data class encapsulating information about an entity during ticking operations.
 *
 * @param T The type of component associated with the entity.
 * @property component The component of type T associated with the entity.
 */
class EntityTickingData<T>(
	deltaTime: Float,
	index: Int,
	chunk: ArchetypeChunk<EntityStore>,
	store: Store<EntityStore>,
	commandBuffer: CommandBuffer<EntityStore>,
	val component: T
) : BaseEntityTickingData<EntityStore>(
	deltaTime,
	index,
	chunk,
	store,
	commandBuffer,
) {
	/**
	 * Retrieves a component of the specified type from the entity using its component ID.
	 *
	 * @param Comp The type of component to retrieve.
	 * @param componentId The ID of the component to retrieve.
	 * @return The component of the specified type, or null if not found.
	 */
	fun <Comp : Component<EntityStore>> getComponent(componentId: String): Comp? {
		val type = Components.Companion.getType<Comp>(componentId) ?: return null

		return chunk.getComponent(index, type)
	}
}