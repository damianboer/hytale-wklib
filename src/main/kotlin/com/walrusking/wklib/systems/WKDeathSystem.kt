package com.walrusking.wklib.systems

import com.hypixel.hytale.component.*
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent
import com.hypixel.hytale.server.core.modules.entity.damage.DeathSystems
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.entities.NPCEntity
import com.walrusking.wklib.helpers.getComponent

/**
 * An abstract class that extends DeathSystems.OnDeathSystem to handle death events with custom logic.
 *
 * @param T The type of WKOnDeathEvent to be created and handled.
 */
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

	/**
	 * Method to be overridden by subclasses to implement custom death event handling logic.
	 *
	 * @param event The WKOnDeathEvent containing information about the death event and context.
	 */
	abstract fun onDeath(event: T)

	/**
	 * Overrides the default query to return the death query from the DeathSystems.
	 *
	 * @return The Query for entities with DeathComponent.
	 */
	abstract override fun getQuery(): Query<EntityStore>
}

/**
 * An abstract class that extends WKBaseOnDeathSystem to handle death events with custom logic.
 */
abstract class WKOnDeathSystem : WKBaseOnDeathSystem<WKOnDeathEvent>() {
	/**
	 * Method to be overridden by subclasses to implement custom death event handling logic.
	 *
	 * @param event The WKOnDeathEvent containing information about the death event and context.
	 */
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

/**
 * Data class encapsulating information about a death event.
 *
 * @property ref The reference to the entity that died.
 * @property deathComponent The DeathComponent associated with the death event.
 * @property store The Store containing the entity data.
 * @property commandBuffer The CommandBuffer for issuing commands.
 * @property deathInfo The Damage information that caused the death, if available.
 * @property cause The cause of death, if available.
 * @property killerPlayer The Player that caused the death, if applicable.
 */
open class WKOnDeathEvent(
	val ref: Ref<EntityStore>,
	val deathComponent: DeathComponent,
	val store: Store<EntityStore>,
	val commandBuffer: CommandBuffer<EntityStore>,
) {
	/** The Damage information that caused the death, if available. */
	val deathInfo: Damage? = deathComponent.deathInfo

	/** The cause of death, if available. */
	val cause: DamageCause? = deathComponent.deathCause

	/** The Player that caused the death, if applicable. */
	val killerPlayer: Player? = getKiller(Player.getComponentType())

	/** The NPCEntity that caused the death, if applicable. */
	val killerNPC: NPCEntity? = getKiller<NPCEntity>(NPCEntity.getComponentType()!!)

	/**
	 * Generic method to retrieve the component of the killer entity that caused the death.
	 *
	 * @param T The type of Component to retrieve.
	 * @param componentType The ComponentType of the desired component.
	 * @return The component of type T from the killer entity, or null if not found.
	 */
	fun <T : Component<EntityStore>> getKiller(componentType: ComponentType<EntityStore, T>): T? {
		val source = deathInfo?.source ?: return null

		val entitySource = source as Damage.EntitySource
		val ref = entitySource.ref

		return ref.getComponent(componentType)
	}
}