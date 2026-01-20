package com.walrusking.wklib

import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import com.walrusking.wklib.commands.Commands
import com.walrusking.wklib.components.Components
import com.walrusking.wklib.events.Events
import com.walrusking.wklib.plugins.WKPlugin


class WKLib(init: JavaPluginInit) : WKPlugin("WKLib", init) {
	override fun setup() {
		LOGGER.info("Initializing WKLib...")

		Components.init(entityStoreRegistry, chunkStoreRegistry)
		Events.init(eventRegistry, entityStoreRegistry)
		Commands.init(commandRegistry)
	}
}