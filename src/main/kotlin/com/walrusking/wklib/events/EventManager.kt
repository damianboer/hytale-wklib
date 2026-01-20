package com.walrusking.wklib.events

import java.util.function.Consumer

class EventManager<T : Any>(val eventClass: Class<T>) {
	val handlers: MutableList<Consumer<T>> = mutableListOf()

	fun register(handler: Consumer<T>) {
		handlers.add(handler)
	}

	fun runHandlers(event: T) {
		for (handler in handlers) {
			handler.accept(event)
		}
	}

	companion object {
		inline fun <reified E : Any> create(): EventManager<E> = EventManager(E::class.java)
	}
}