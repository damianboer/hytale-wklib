package com.walrusking.wklib.commands

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.helpers.sendFormattedMessage

/**
 * A base class for creating player commands in the WKLib framework.
 *
 * @param name The name of the command.
 * @param description A brief description of the command.
 * @param requiresConfirmation Indicates whether the command requires confirmation before execution.
 */
open class WKCommand(name: String, description: String, requiresConfirmation: Boolean = false) :
	AbstractPlayerCommand(name, description, requiresConfirmation) {

	override fun execute(
		p0: CommandContext,
		p1: Store<EntityStore>,
		p2: Ref<EntityStore>,
		p3: PlayerRef,
		p4: World
	) {
		val data = CommandData(p0, p1, p2, p4, p3)
		onExecute(data)
	}

	/**
	 * This method is called when the command is executed.
	 *
	 * @param data The CommandData containing context and player information.
	 */
	open fun onExecute(data: CommandData) {
	}
}

/**
 * A data class that encapsulates the context and relevant information for command execution.
 *
 * @property context The command context.
 * @property store The entity store.
 * @property ref The reference to the entity store.
 * @property world The world in which the command is executed.
 * @property playerRef The reference to the player executing the command.
 * @property player The player executing the command.
 */
class CommandData(
	val context: CommandContext,
	val store: Store<EntityStore>,
	val ref: Ref<EntityStore>,
	val world: World,
	val playerRef: PlayerRef,
	/** The player executing the command. */
	val player: Player = store.getComponent(ref, Player.getComponentType()) as Player
) {
	/**
	 * Retrieves the value of a required argument from the command context.
	 *
	 * @param arg The required argument to retrieve.
	 * @return The value of the required argument.
	 */
	fun <T> arg(arg: RequiredArg<T>): T {
		return arg.get(context)
	}

	/**
	 * Sends a raw message to the player that executed the command.
	 *
	 * @param message The raw message string to send.
	 */
	fun sendMessage(message: String) {
		sendMessage(Message.raw(message))
	}

	/**
	 * Sends a Message object to the player that executed the command.
	 *
	 * @param message The Message object to send.
	 */
	fun sendMessage(message: Message) {
		player.sendMessage(message)
	}

	/**
	 * Sends a formatted message to the player that executed the command.
	 *
	 * @param message The formatted message string to send.
	 */
	fun sendFormattedMessage(message: String) {
		playerRef.sendFormattedMessage(message)
	}
}