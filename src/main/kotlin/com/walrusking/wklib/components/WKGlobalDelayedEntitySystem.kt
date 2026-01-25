package com.walrusking.wklib.components

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

abstract class WKGlobalDelayedEntitySystem(
	intervalSec: Float
) :
	DelayedEntitySystem<EntityStore>(intervalSec) {

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
