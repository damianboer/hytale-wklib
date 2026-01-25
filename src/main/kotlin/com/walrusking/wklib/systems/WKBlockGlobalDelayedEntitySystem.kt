package com.walrusking.wklib.systems

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore

abstract class WKBlockGlobalDelayedEntitySystem(
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
		val data = BlockGlobalEntityTickingData(p0, p1, p2, p3, p4)
		onTick(data)
	}

	abstract fun onTick(data: BlockGlobalEntityTickingData)
}