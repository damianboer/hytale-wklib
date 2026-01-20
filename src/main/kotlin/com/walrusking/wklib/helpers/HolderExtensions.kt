package com.walrusking.wklib.helpers

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.Holder
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.components.Components

fun <T : Component<EntityStore>> Holder<EntityStore>.addAndGetComponent(typeId: String): T {
	if (typeId.isEmpty()) {
		throw IllegalArgumentException("Component ID cannot be empty!")
	}

	val type =
		Components.getType<T>(typeId) ?: throw IllegalArgumentException("Component with ID $typeId is not registered!")

	val componentInstance = getComponent(type)

	if (componentInstance != null) {
		return componentInstance
	} else {
		@Suppress("UNCHECKED_CAST")
		addComponent(type, type.typeClass.getDeclaredConstructor().newInstance() as (T))
	}

	return getComponent(type)!!
}