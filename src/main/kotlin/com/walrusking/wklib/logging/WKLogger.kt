package com.walrusking.wklib.logging

import com.hypixel.hytale.logger.HytaleLogger

class WKLogger {
	private var logger: HytaleLogger

	constructor(name: String) {
		logger = HytaleLogger.get(name)
	}

	fun getLogger(): HytaleLogger {
		return logger
	}

	fun info(message: String) {
		logger.atInfo().log(message)
	}

	fun warn(message: String) {
		logger.atWarning().log(message)
	}

	fun error(message: String) {
		logger.atSevere().log(message)
	}

	fun config(message: String) {
		logger.atConfig().log(message)
	}

	fun fine(message: String) {
		logger.atFine().log(message)
	}

	fun finer(message: String) {
		logger.atFiner().log(message)
	}

	fun finest(message: String) {
		logger.atFinest().log(message)
	}
}