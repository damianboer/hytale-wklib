package com.walrusking.wklib.commands

import com.hypixel.hytale.server.core.command.system.AbstractCommand
import com.hypixel.hytale.server.core.command.system.CommandRegistry

class Commands {
	companion object {
		var registry: CommandRegistry? = null

		fun init(registry: CommandRegistry) {
			this.registry = registry
		}

		fun register(command: AbstractCommand) {
			registry?.registerCommand(command)
		}
	}
}