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

		inline fun <reified T : WKConfig> getOrCreateConfig(plugin: WKPlugin, configName: String, data: T): Config<T> {
			return getOrCreateConfig(plugin.dataDirectory, configName, data)
		}

		inline fun <reified T : WKConfig> getOrCreateConfig(path: Path, configName: String, data: T): Config<T> {
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

		fun <T : WKConfig> loadConfig(path: Path, configName: String, config: Config<T>, data: T) {
			val cfgName = getConfigName(configName)
			val pluginConfig = path.resolve("$cfgName.json").toFile()

			config.load()

			val s = config.get()
			if (s == null) {
				WKLogger("WKLib:Configs").warn("Config instance is null for $cfgName, skipping")
				return
			}

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
				WKLogger("WKLib:Configs").info("Created new config $cfgName")
				return;
			}

			var changed = false

			val configChanges = s.upgradeConfig(s.configVersion)
			if (configChanges.isNotEmpty()) {
				for (change in configChanges) {
					when (change.changeType) {
						ConfigChangeType.ADDED -> {
							val field = data.javaClass.getDeclaredField(change.fieldName)
							field.trySetAccessible()
							if (Modifier.isFinal(field.modifiers)) {
								WKLogger("WKLib:Configs").warn("Field ${change.fieldName} is final, cannot set value for config $cfgName")
								continue
							}
							val newValue = field.get(data)
							field.set(s, newValue)
							changed = true
							WKLogger("WKLib:Configs").info("Added field ${change.fieldName} to config $cfgName")
						}

						else -> {}
					}
				}
			}

			if (changed) {
				s.configVersion = data.configVersion
				config.save()
				WKLogger("WKLib:Configs").info("Upgraded config $cfgName to new version")
			} else {
				WKLogger("WKLib:Configs").info("Loaded existing config $cfgName")
			}
		}
	}
}