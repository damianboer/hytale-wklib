package com.walrusking.wklib.plugins

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import com.hypixel.hytale.server.core.util.Config
import com.walrusking.wklib.config.ConfigUtil
import com.walrusking.wklib.logging.WKLogger
import com.walrusking.wklib.utilities.CodecUtil


open class WKPlugin(val pluginName: String, init: JavaPluginInit) : JavaPlugin(init) {
	val LOGGER = WKLogger(pluginName)

	protected inline fun <reified T> withConfig(): Config<T> {
		return this.withConfig(pluginName, CodecUtil.buildConfigCodec(T::class.java))
	}

	protected inline fun <reified T> withConfig(configName: String): Config<T> {
		return this.withConfig(ConfigUtil.getConfigName(configName), CodecUtil.buildConfigCodec(T::class.java))
	}

	fun <T> getOrCreateConfig(
		configName: String,
		data: T,
		codec: com.hypixel.hytale.codec.builder.BuilderCodec<T>
	): Config<T> {
		return ConfigUtil.getOrCreateConfig(this, configName, data, codec)
	}
}