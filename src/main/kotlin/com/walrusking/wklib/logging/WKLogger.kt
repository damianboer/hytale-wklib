package com.walrusking.wklib.logging

import com.hypixel.hytale.logger.HytaleLogger

/**
 * WKLogger is a wrapper around HytaleLogger to provide simplified logging functionality.
 *
 * @property logger The underlying HytaleLogger instance.
 */
class WKLogger {
	private var logger: HytaleLogger

	/**
	 * Constructs a WKLogger with the specified name.
	 *
	 * @param name The name of the logger.
	 */
	constructor(name: String) {
		logger = HytaleLogger.get(name)
	}

	/**
	 * Retrieves the underlying HytaleLogger instance.
	 *
	 * @return The HytaleLogger instance.
	 */
	fun getLogger(): HytaleLogger {
		return logger
	}

	/**
	 * Logs an informational message.
	 *
	 * @param message The message to log.
	 */
	fun info(message: String) {
		logger.atInfo().log(message)
	}

	/**
	 * Logs a warning message.
	 *
	 * @param message The message to log.
	 */
	fun warn(message: String) {
		logger.atWarning().log(message)
	}

	/**
	 * Logs an error message.
	 *
	 * @param message The message to log.
	 */
	fun error(message: String) {
		logger.atSevere().log(message)
	}

	/**
	 * Logs a configuration message.
	 *
	 * @param message The message to log.
	 */
	fun config(message: String) {
		logger.atConfig().log(message)
	}

	/**
	 * Logs a fine message.
	 *
	 * @param message The message to log.
	 */
	fun fine(message: String) {
		logger.atFine().log(message)
	}

	/**
	 * Logs a finer message.
	 *
	 * @param message The message to log.
	 */
	fun finer(message: String) {
		logger.atFiner().log(message)
	}

	/**
	 * Logs the finest message.
	 *
	 * @param message The message to log.
	 */
	fun finest(message: String) {
		logger.atFinest().log(message)
	}
}