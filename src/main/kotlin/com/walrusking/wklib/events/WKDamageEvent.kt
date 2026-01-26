package com.walrusking.wklib.events

import com.hypixel.hytale.component.*
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entity.damage.Damage
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.server.npc.entities.NPCEntity
import com.walrusking.wklib.helpers.getComponent

/**
 * An abstract class that extends WKEntityEventSystem to handle damage events with custom logic.
 *
 * @property eventType The class type of the Damage event to be handled.
 */
abstract class WKDamageEvent : WKEntityEventSystem<Damage>(Damage::class.java) {
	override fun onExecute(data: EventData<Damage>) {
		val eventData = WKDamageEventData(
			data.index,
			data.chunk,
			data.store,
			data.commandBuffer,
			data.event
		)
		onDamage(eventData)
	}

	/**
	 * Method to be overridden by subclasses to implement custom damage event handling logic.
	 *
	 * @param event The WKDamageEventData containing information about the damage event and context.
	 */
	abstract fun onDamage(event: WKDamageEventData)

	/**
	 * Overrides the default query to return the gather damage group from the DamageModule.
	 *
	 * @return The SystemGroup for gathering damage events.
	 */
	override fun getGroup(): SystemGroup<EntityStore?>? {
		return DamageModule.get().gatherDamageGroup;
	}
}

/**
 * Data class encapsulating information about a damage event.
 *
 * @property attackingNPC The NPCEntity that caused the damage, if applicable.
 * @property attackingPlayer The Player that caused the damage, if applicable.
 * @property targetNPC The NPCEntity that is the target of the damage, if applicable.
 * @property targetPlayer The Player that is the target of the damage, if applicable.
 * @property damageAmount The amount of damage dealt.
 */
class WKDamageEventData(
	index: Int,
	chunk: ArchetypeChunk<EntityStore?>,
	store: Store<EntityStore?>,
	commandBuffer: CommandBuffer<EntityStore?>,
	event: Damage
) : EventData<Damage>(
	index,
	chunk,
	store,
	commandBuffer,
	event
) {
	/** The NPCEntity that caused the damage, if applicable. */
	val attackingNPC: NPCEntity? = getAttacker<NPCEntity>(NPCEntity.getComponentType()!!)

	/** The Player that caused the damage, if applicable. */
	val attackingPlayer: Player? = getAttacker(Player.getComponentType())

	/** The reference to the target entity. */
	val targetRef = chunk.getReferenceTo(index)

	/** The NPCEntity that is the target of the damage, if applicable. */
	val targetNPC: NPCEntity? = getTarget<NPCEntity>(NPCEntity.getComponentType()!!)

	/** The Player that is the target of the damage, if applicable. */
	val targetPlayer: Player? = getTarget<Player>(Player.getComponentType())

	/** The amount of damage dealt. */
	val damageAmount: Float = event.amount

	/**
	 * Retrieves a component of the target entity.
	 *
	 * @param T The type of the component to retrieve.
	 * @param componentType The ComponentType of the component to retrieve.
	 * @return The component of type T if it exists on the target entity, otherwise null.
	 */
	@Suppress("UNCHECKED_CAST")
	fun <T : Component<EntityStore>> getTarget(componentType: ComponentType<EntityStore, T>): T? {
		if (!targetRef.isValid) return null;

		val ref = targetRef as Ref<EntityStore>

		return ref.getComponent(componentType)
	}

	/**
	 * Retrieves a component of the attacker entity.
	 *
	 * @param T The type of the component to retrieve.
	 * @param componentType The ComponentType of the component to retrieve.
	 * @return The component of type T if it exists on the attacker entity, otherwise null.
	 */
	fun <T : Component<EntityStore>> getAttacker(componentType: ComponentType<EntityStore, T>): T? {
		val source = event.source

		if (source !is Damage.EntitySource) return null

		val ref = source.ref

		return ref.getComponent(componentType)
	}
}