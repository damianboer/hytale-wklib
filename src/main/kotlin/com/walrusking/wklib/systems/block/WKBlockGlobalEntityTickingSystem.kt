package com.walrusking.wklib.systems.block

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore

/**
 * An abstract system for ticking block global entities within chunks.
 */
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

	/**
	 * Method to be overridden by subclasses to implement custom ticking logic for block global entities.
	 *
	 * @param data The BlockGlobalEntityTickingData containing information about the tick context.
	 */
	abstract fun onTick(data: BlockGlobalEntityTickingData)
}
