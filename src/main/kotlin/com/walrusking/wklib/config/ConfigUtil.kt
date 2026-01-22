package com.walrusking.wklib.config

import com.hypixel.hytale.server.core.util.Config
import com.walrusking.wklib.logging.WKLogger
import com.walrusking.wklib.plugins.WKPlugin
import com.walrusking.wklib.utilities.CodecUtil
import java.lang.reflect.Modifier
import java.nio.file.Path
import java.util.Locale.getDefault

class ConfigUtil {
	companion object {
		fun getConfigName(configName: String): String {
			return configName.replace(" ", "").lowercase(getDefault())
		}

		inline fun <reified T> getOrCreateConfig(plugin: WKPlugin, configName: String, data: T): Config<T> {
			return getOrCreateConfig(plugin.dataDirectory, configName, data)
		}

		inline fun <reified T> getOrCreateConfig(path: Path, configName: String, data: T): Config<T> {
			val config = Config<T>(path, getConfigName(configName), CodecUtil.buildConfigCodec(T::class.java))

			loadConfig(path, configName, config, data)

			return config
		}

		fun loadConfig(plugin: WKPlugin, config: Config<*>) {
			val pluginName = plugin.pluginName

			val configName = getConfigName(pluginName)
			val dataDirectory = plugin.dataDirectory

			loadConfig(dataDirectory, config, configName)
		}

		fun loadConfig(path: Path, config: Config<*>, configName: String) {
			val configName = getConfigName(configName)
			val pluginConfig = path.resolve("$configName.json").toFile()

			config.load()

			if (!pluginConfig.exists()) {
				config.save()
				WKLogger("WKLib:Configs").info("Created new config $configName")
			} else {
				WKLogger("WKLib:Configs").info("Loaded existing config $configName")
			}
		}

		fun <T> loadConfig(path: Path, configName: String, config: Config<T>, data: T) {
			val configName = getConfigName(configName)
			val pluginConfig = path.resolve("$configName.json").toFile()

			config.load()

			if (!pluginConfig.exists()) {
				val s = config.get()
				s?.javaClass?.declaredFields?.forEach { field ->
					if (Modifier.isStatic(field.modifiers)) {
						return@forEach
					}

					field.trySetAccessible()

					try {
						field.set(s, field.get(data))
					} catch (e: IllegalAccessException) {
						e.printStackTrace()
					}
				}
				config.save()
				WKLogger("WKLib:Configs").info("Created new config $configName")
			} else {
				WKLogger("WKLib:Configs").info("Loaded existing config $configName")
			}
		}
	}
}