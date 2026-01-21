package com.walrusking.wklib.components

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.helpers.getComponent

abstract class WKBaseOnDeathSystem<T : WKOnDeathEvent>(
) : DeathSystems.OnDeathSystem() {
	override fun onComponentAdded(
		p0: Ref<EntityStore>,
		p1: DeathComponent,
		p2: Store<EntityStore>,
		p3: CommandBuffer<EntityStore>
	) {
		val event = createEvent(p0, p1, p2, p3)
		onDeath(event)
	}

	protected abstract fun createEvent(
		ref: Ref<EntityStore>,
		deathComponent: DeathComponent,
		store: Store<EntityStore>,
		commandBuffer: CommandBuffer<EntityStore>
	): T

	abstract fun onDeath(event: T)

	abstract override fun getQuery(): Query<EntityStore>
}

abstract class WKOnDeathSystem : WKBaseOnDeathSystem<WKOnDeathEvent>() {
	override fun onDeath(event: WKOnDeathEvent) {

	}

	override fun createEvent(
		ref: Ref<EntityStore>,
		deathComponent: DeathComponent,
		store: Store<EntityStore>,
		commandBuffer: CommandBuffer<EntityStore>
	): WKOnDeathEvent {
		return WKOnDeathEvent(ref, deathComponent, store, commandBuffer)
	}
}

abstract class WKPlayerDeathSystem : WKBaseOnDeathSystem<WKPlayerDeathEvent>(
) {
	override fun onDeath(event: WKPlayerDeathEvent) {

	}

	override fun createEvent(
		ref: Ref<EntityStore>,
		deathComponent: DeathComponent,
		store: Store<EntityStore>,
		commandBuffer: CommandBuffer<EntityStore>
	): WKPlayerDeathEvent {
		return WKPlayerDeathEvent(ref, deathComponent, store, commandBuffer)
	}

	override fun getQuery(): Query<EntityStore> {
		return Query.and(Player.getComponentType())
	}
}

open class WKOnDeathEvent(
	val ref: Ref<EntityStore>,
	val deathComponent: DeathComponent,
	val store: Store<EntityStore>,
	val commandBuffer: CommandBuffer<EntityStore>,
) {
	val deathInfo: Damage? = deathComponent.deathInfo
	val cause: DamageCause? = deathComponent.deathCause
	val killerPlayer: Player? = getKiller(Player.getComponentType())

	fun <T : Component<EntityStore>> getKiller(componentType: ComponentType<EntityStore, T>): T? {
		val source = deathInfo?.source ?: return null

		val entitySource = source as Damage.EntitySource
		val ref = entitySource.ref

		return ref.getComponent(componentType)
	}
}

class WKPlayerDeathEvent(
	ref: Ref<EntityStore>, deathComponent: DeathComponent, store: Store<EntityStore>,
	commandBuffer: CommandBuffer<EntityStore>
) : WKOnDeathEvent(
	ref, deathComponent,
	store, commandBuffer
) {
	val player: Player? = ref.getComponent(Player.getComponentType())
}