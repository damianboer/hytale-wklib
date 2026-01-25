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

	open fun onExecute(data: CommandData) {
	}
}

class CommandData(
	val context: CommandContext,
	val store: Store<EntityStore>,
	val ref: Ref<EntityStore>,
	val world: World,
	val playerRef: PlayerRef,
	val player: Player = store.getComponent(ref, Player.getComponentType()) as Player
) {
	fun <T> arg(arg: RequiredArg<T>): T {
		return arg.get(context)
	}

	fun sendMessage(message: String) {
		sendMessage(Message.raw(message))
	}

	fun sendMessage(message: Message) {
		player.sendMessage(message)
	}

	fun sendFormattedMessage(message: String) {
		playerRef.sendFormattedMessage(message)
	}
}