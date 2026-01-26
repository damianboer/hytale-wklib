package com.walrusking.wklib.commands

import com.hypixel.hytale.server.core.command.system.AbstractCommand
import com.hypixel.hytale.server.core.command.system.CommandRegistry

/**
 * This class is responsible for registering commands to the CommandRegistry.
 */
class Commands {
	companion object {
		var registry: CommandRegistry? = null

		fun init(registry: CommandRegistry) {
			this.registry = registry
		}

		/**
		 * Registers a command to the CommandRegistry.
		 *
		 * @param command The command to be registered.
		 */
		fun register(command: AbstractCommand) {
			registry?.registerCommand(command)
		}
	}
}