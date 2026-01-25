package com.walrusking.wklib.systems

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.walrusking.wklib.components.Components

abstract class WKBlockGlobalEntityTickingSystem :
	EntityTickingSystem<ChunkStore>() {

	override fun tick(
		p0: Float,
		p1: Int,
		p2: ArchetypeChunk<ChunkStore>,
		p3: Store<ChunkStore>,
		p4: CommandBuffer<ChunkStore>
	) {
		val data = BlockGlobalEntityTickingData(p0, p1, p2, p3, p4)
		onTick(data)
	}

	abstract fun onTick(data: BlockGlobalEntityTickingData)
}

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

	abstract fun onTick(data: BlockEntityTickingData<T>)

	override fun getQuery(): Query<ChunkStore>? {
		return Query.and(componentType)
	}
}

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
	fun <Comp : Component<ChunkStore>> getComponent(componentId: String): Comp? {
		val type = Components.Companion.getBlockType<Comp>(componentId) ?: return null

		return chunk.getComponent(index, type)
	}
}