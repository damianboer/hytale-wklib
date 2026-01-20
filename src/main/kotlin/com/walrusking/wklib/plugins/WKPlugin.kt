package com.walrusking.wklib.plugins

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import com.walrusking.wklib.logging.WKLogger


open class WKPlugin(name: String, init: JavaPluginInit) : JavaPlugin(init) {
	companion object {
		lateinit var LOGGER: WKLogger
	}

	init {
		LOGGER = WKLogger(name)
	}
}