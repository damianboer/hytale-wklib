package com.walrusking.wklib.config

/**
 * Base class for configuration objects with versioning support.
 */
open class WKConfig(
	/**
	 * The version of the configuration.
	 */
	var configVersion: Int = 1
) {
	/**
	 * Upgrade the configuration from an old version to the current version.
	 * Override this method in subclasses to implement specific upgrade logic.
	 *
	 * @param oldVersion The version of the configuration being upgraded.
	 * @return A list of ConfigChange objects representing the changes made during the upgrade.
	 */
	open fun upgradeConfig(oldVersion: Int): List<ConfigChange> {
		return emptyList()
	}
}

/**
 * Data class representing a change made during configuration upgrade.
 *
 * @param fieldName The name of the configuration field that was changed.
 * @param changeType The type of change (added or removed).
 */
data class ConfigChange(val fieldName: String, val changeType: ConfigChangeType)

/**
 * Enum representing the type of configuration change.
 */
enum class ConfigChangeType {
	ADDED,
	REMOVED,
}