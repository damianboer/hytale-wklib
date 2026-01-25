package com.walrusking.wklib.systems

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore

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

	abstract fun onTick(data: BlockEntityTickingData<T>)

	override fun getQuery(): Query<ChunkStore>? {
		return Query.and(componentType)
	}
}