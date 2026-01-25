package com.walrusking.wklib.systems

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.components.Components

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

	abstract fun onTick(data: EntityTickingData<T>)

	override fun getQuery(): Query<EntityStore> {
		return Query.and(componentType)
	}
}

abstract class BaseEntityTickingData<StoreType>(
	val deltaTime: Float,
	val index: Int,
	val chunk: ArchetypeChunk<StoreType>,
	val store: Store<StoreType>,
	val commandBuffer: CommandBuffer<StoreType>,
) {
	fun <Comp : Component<StoreType>> getComponent(type: ComponentType<StoreType, Comp>): Comp? {
		return chunk.getComponent(index, type)
	}

	fun getRef(): Ref<StoreType> {
		return chunk.getReferenceTo(index)
	}
}

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
	fun <Comp : Component<EntityStore>> getComponent(componentId: String): Comp? {
		val type = Components.Companion.getType<Comp>(componentId) ?: return null

		return chunk.getComponent(index, type)
	}
}