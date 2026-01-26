package com.walrusking.wklib.plugins

import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import com.hypixel.hytale.server.core.util.Config
import com.walrusking.wklib.config.ConfigUtil
import com.walrusking.wklib.config.WKConfig
import com.walrusking.wklib.logging.WKLogger
import com.walrusking.wklib.utilities.CodecUtil


/**
 * WKPlugin is an abstract class that extends JavaPlugin to provide additional functionality
 * for managing configurations and logging within a plugin.
 *
 * @property pluginName The name of the plugin.
 * @property init The JavaPluginInit instance used for plugin initialization.
 */
open class WKPlugin(val pluginName: String, init: JavaPluginInit) : JavaPlugin(init) {
	/** Logger instance for the plugin */
	val logger = WKLogger(pluginName)

	/**
	 * Helper function to create a Config instance for a given type T using the plugin's name and a codec.
	 *
	 * @param T The type of the configuration data.
	 * @return A Config instance for the specified type T.
	 */
	protected inline fun <reified T> withConfig(): Config<T> {
		return this.withConfig(pluginName, CodecUtil.buildConfigCodec(T::class.java))
	}

	/**
	 * Helper function to create a Config instance for a given type T and configuration name
	 * using a codec.
	 *
	 * @param T The type of the configuration data.
	 * @param configName The name of the configuration.
	 * @return A Config instance for the specified type T and configuration name.
	 */
	protected inline fun <reified T> withConfig(configName: String): Config<T> {
		return this.withConfig(ConfigUtil.getConfigName(configName), CodecUtil.buildConfigCodec(T::class.java))
	}

	/**
	 * Helper function to get or create a Config instance for a given WKConfig type T
	 * using the plugin's name.
	 *
	 * @param T The type of the WKConfig data.
	 * @param data An instance of the WKConfig data to use if creating a new config.
	 * @return A Config instance for the specified WKConfig type T.
	 */
	inline fun <reified T : WKConfig> getOrCreateConfig(
		configName: String,
		data: T,
	): Config<T> {
		return ConfigUtil.getOrCreateConfig(this, configName, data)
	}
}