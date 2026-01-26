package com.walrusking.wklib.events

import java.util.function.Consumer

/**
 * Manages event handlers for a specific type of event.
 *
 * @param T The type of event this manager handles.
 * @property eventClass The class of the event type.
 */
class EventManager<T : Any>(val eventClass: Class<T>) {
	val handlers: MutableList<Consumer<T>> = mutableListOf()

	/**
	 * Registers a new event handler.
	 *
	 * @param handler The event handler to register.
	 */
	fun register(handler: Consumer<T>) {
		handlers.add(handler)
	}

	/**
	 * Runs all registered event handlers with the given event.
	 *
	 * @param event The event to pass to the handlers.
	 */
	fun runHandlers(event: T) {
		for (handler in handlers) {
			handler.accept(event)
		}
	}

	/**
	 * Runs all registered event handlers with the given nullable event.
	 *
	 * @param event The nullable event to pass to the handlers.
	 */
	fun runHandlersNullable(event: T?) {
		for (handler in handlers) {
			handler.accept(event!!)
		}
	}

	companion object {
		/**
		 * Creates a new EventManager for the specified event type.
		 *
		 * @return A new EventManager instance for the specified event type.
		 */
		inline fun <reified E : Any> create(): EventManager<E> = EventManager(E::class.java)
	}
}