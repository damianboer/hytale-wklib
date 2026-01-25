package com.walrusking.wklib.systems

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.DelayedEntitySystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

abstract class WKDelayedEntitySystem<T : Component<EntityStore>>(
	val componentType: ComponentType<EntityStore, T>,
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
		val component: T = p2.getComponent(p1, componentType) ?: return

		val data = EntityTickingData(p0, p1, p2, p3, p4, component)
		onTick(data)
	}

	abstract fun onTick(data: EntityTickingData<T>)

	override fun getQuery(): Query<EntityStore> {
		return Query.and(componentType)
	}
}