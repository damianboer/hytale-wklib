package com.walrusking.wklib.systems

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.RefChangeSystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

abstract class WKRefChangeSystem<T : Component<EntityStore>>(val componentType: ComponentType<EntityStore, T>) :
	RefChangeSystem<EntityStore, T>() {

	override fun componentType(): ComponentType<EntityStore, T> {
		return componentType
	}

	override fun onComponentAdded(
		p0: Ref<EntityStore>,
		p1: T,
		p2: Store<EntityStore>,
		p3: CommandBuffer<EntityStore>
	) {
		val data = RefChangeData(p0, p1, p2, p3)
		onAdded(data)
	}

	open fun onAdded(data: RefChangeData<T, EntityStore>) {}

	override fun onComponentSet(
		p0: Ref<EntityStore>,
		p1: T?,
		p2: T,
		p3: Store<EntityStore>,
		p4: CommandBuffer<EntityStore>
	) {
		val data = RefSetChangeData(p0, p1, p2, p3, p4)
		onSet(data)
	}

	open fun onSet(data: RefSetChangeData<T, EntityStore>) {}

	override fun onComponentRemoved(
		p0: Ref<EntityStore>,
		p1: T,
		p2: Store<EntityStore>,
		p3: CommandBuffer<EntityStore>
	) {
		val data = RefChangeData(p0, p1, p2, p3)
		onRemoved(data)
	}

	open fun onRemoved(data: RefChangeData<T, EntityStore>) {}

	override fun getQuery(): Query<EntityStore> {
		return Query.and(componentType)
	}
}

abstract class WKBlockRefChangeSystem<T : Component<ChunkStore>>(val componentType: ComponentType<ChunkStore, T>) :
	RefChangeSystem<ChunkStore, T>() {

	override fun componentType(): ComponentType<ChunkStore, T> {
		return componentType
	}

	override fun onComponentAdded(
		p0: Ref<ChunkStore>,
		p1: T,
		p2: Store<ChunkStore>,
		p3: CommandBuffer<ChunkStore>
	) {
		val data = RefChangeData(p0, p1, p2, p3)
		onAdded(data)
	}

	open fun onAdded(data: RefChangeData<T, ChunkStore>) {}

	override fun onComponentSet(
		p0: Ref<ChunkStore>,
		p1: T?,
		p2: T,
		p3: Store<ChunkStore>,
		p4: CommandBuffer<ChunkStore>
	) {
		val data = RefSetChangeData(p0, p1, p2, p3, p4)
		onSet(data)
	}

	open fun onSet(data: RefSetChangeData<T, ChunkStore>) {}

	override fun onComponentRemoved(
		p0: Ref<ChunkStore>,
		p1: T,
		p2: Store<ChunkStore>,
		p3: CommandBuffer<ChunkStore>
	) {
		val data = RefChangeData(p0, p1, p2, p3)
		onRemoved(data)
	}

	open fun onRemoved(data: RefChangeData<T, ChunkStore>) {}

	override fun getQuery(): Query<ChunkStore> {
		return Query.and(componentType)
	}
}

data class RefChangeData<T : Component<StoreType>, StoreType>(
	val ref: Ref<StoreType>,
	val component: T,
	val store: Store<StoreType>,
	val commandBuffer: CommandBuffer<StoreType>
)

data class RefSetChangeData<T : Component<StoreType>, StoreType>(
	val ref: Ref<StoreType>,
	val oldComponent: T?,
	val newComponent: T,
	val store: Store<StoreType>,
	val commandBuffer: CommandBuffer<StoreType>
)