package com.walrusking.wklib.systems

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.RefChangeSystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

;

/**
 * An abstract class that extends RefChangeSystem to handle component reference changes for EntityStore.
 *
 * @param T The type of component being monitored for reference changes.
 * @property componentType The ComponentType of the component being monitored.
 */
abstract class WKRefChangeSystem<T : Component<EntityStore>>(val componentType: ComponentType<EntityStore, T>) :
	RefChangeSystem<EntityStore, T>() {

	/** Overrides the componentType method to return the specified component type. */
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

	/** Method to be overridden by subclasses to handle component addition events. */
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

	/** Method to be overridden by subclasses to handle component set events. */
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

	/** Method to be overridden by subclasses to handle component removal events. */
	open fun onRemoved(data: RefChangeData<T, EntityStore>) {}


	/** Overrides the getQuery method to return a query for the specified component type. */
	override fun getQuery(): Query<EntityStore> {
		return Query.and(componentType)
	}
}

/**
 * An abstract class that extends RefChangeSystem to handle component reference changes for ChunkStore.
 *
 * @param T The type of component being monitored for reference changes.
 * @property componentType The ComponentType of the component being monitored.
 */
abstract class WKBlockRefChangeSystem<T : Component<ChunkStore>>(val componentType: ComponentType<ChunkStore, T>) :
	RefChangeSystem<ChunkStore, T>() {

	/** Overrides the componentType method to return the specified component type. */
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

	/** Method to be overridden by subclasses to handle component addition events. */
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

	/** Method to be overridden by subclasses to handle component set events. */
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

	/** Method to be overridden by subclasses to handle component removal events. */
	open fun onRemoved(data: RefChangeData<T, ChunkStore>) {}

	/** Overrides the getQuery method to return a query for the specified component type. */
	override fun getQuery(): Query<ChunkStore> {
		return Query.and(componentType)
	}
}

/** Data class encapsulating information about a component reference change event. */
data class RefChangeData<T : Component<StoreType>, StoreType>(
	val ref: Ref<StoreType>,
	val component: T,
	val store: Store<StoreType>,
	val commandBuffer: CommandBuffer<StoreType>
)

/** Data class encapsulating information about a component reference set event. */
data class RefSetChangeData<T : Component<StoreType>, StoreType>(
	val ref: Ref<StoreType>,
	val oldComponent: T?,
	val newComponent: T,
	val store: Store<StoreType>,
	val commandBuffer: CommandBuffer<StoreType>
)