package com.walrusking.wklib.events

import com.hypixel.hytale.component.Archetype
import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.EcsEvent
import com.hypixel.hytale.component.system.EntityEventSystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.logging.WKLogger

/**
 * An abstract class that extends EntityEventSystem to handle entity events with custom logic.
 *
 * @param T The type of EcsEvent this system will handle.
 * @property eventType The class type of the event to be handled.
 */
abstract class WKEntityEventSystem<T : EcsEvent>(eventType: Class<T>) : EntityEventSystem<EntityStore, T>(eventType) {
	override fun handle(
		p0: Int,
		p1: ArchetypeChunk<EntityStore?>,
		p2: Store<EntityStore?>,
		p3: CommandBuffer<EntityStore?>,
		p4: T
	) {
		val data = EventData(
			p0,
			p1,
			p2,
			p3,
			p4
		)

		try {
			onExecute(data)
		} catch (e: Exception) {
			WKLogger("WKLib:WKEntityEventSystem").error("Error executing event system for event type ${eventType.name}: ${e.message}")
		}
	}

	/**
	 * Method to be overridden by subclasses to implement custom event handling logic.
	 *
	 * @param data The EventData containing information about the event and context.
	 */
	abstract fun onExecute(data: EventData<T>)

	/**
	 * Overrides the default query to return an empty archetype, indicating no specific component requirements.
	 *
	 * @return An empty Query for EntityStore.
	 */
	override fun getQuery(): Query<EntityStore?>? {
		return Archetype.empty()
	}
}

/**
 * Data class encapsulating information about an entity event.
 *
 * @param T The type of EcsEvent.
 * @property index The index of the entity in the chunk.
 * @property chunk The ArchetypeChunk containing the entities.
 * @property store The Store managing the entities.
 * @property commandBuffer The CommandBuffer for issuing commands to entities.
 * @property event The event instance being processed.
 */
open class EventData<T : EcsEvent>(
	val index: Int,
	val chunk: ArchetypeChunk<EntityStore?>,
	val store: Store<EntityStore?>,
	val commandBuffer: CommandBuffer<EntityStore?>,
	val event: T,
)