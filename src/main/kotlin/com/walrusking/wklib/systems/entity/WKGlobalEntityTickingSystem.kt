package com.walrusking.wklib.systems.entity

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

abstract class WKGlobalEntityTickingSystem :
	EntityTickingSystem<EntityStore>() {

	override fun tick(
		p0: Float,
		p1: Int,
		p2: ArchetypeChunk<EntityStore>,
		p3: Store<EntityStore>,
		p4: CommandBuffer<EntityStore>
	) {
		val data = GlobalEntityTickingData(p0, p1, p2, p3, p4)
		onTick(data)
	}

	abstract fun onTick(data: GlobalEntityTickingData)
}

class GlobalEntityTickingData(
	deltaTime: Float,
	index: Int,
	chunk: ArchetypeChunk<EntityStore>,
	store: Store<EntityStore>,
	commandBuffer: CommandBuffer<EntityStore>
) : BaseEntityTickingData<EntityStore>(
	deltaTime,
	index,
	chunk,
	store,
	commandBuffer
)