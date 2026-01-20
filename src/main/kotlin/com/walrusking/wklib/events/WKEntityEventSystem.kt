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

open class WKEntityEventSystem<T : EcsEvent>(eventType: Class<T>) : EntityEventSystem<EntityStore, T>(eventType) {
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

	open fun onExecute(data: EventData<T>) {

	}

	override fun getQuery(): Query<EntityStore?>? {
		return Archetype.empty()
	}
}

data class EventData<T : EcsEvent>(
	val index: Int,
	val type: ArchetypeChunk<EntityStore?>,
	val store: Store<EntityStore?>,
	val commandBuffer: CommandBuffer<EntityStore?>,
	val event: T,
)