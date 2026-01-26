package com.walrusking.wklib.helpers

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.components.Components

/**
 * Get the ExternalData from an EntityStore reference.
 *
 * @receiver Ref<EntityStore> The reference to the EntityStore.
 * @return The ExternalData associated with the EntityStore.
 */
fun Ref<EntityStore>.getExternalData() = store.externalData

/**
 * Get the World from an EntityStore reference.
 *
 * @receiver Ref<EntityStore> The reference to the EntityStore.
 * @return The World associated with the EntityStore.
 */
fun Ref<EntityStore>.getWorld() = store.externalData.world

/**
 * Add a component to an EntityStore reference.
 *
 * @receiver Ref<EntityStore> The reference to the EntityStore.
 * @param component The component type to add.
 */
fun <T : Component<EntityStore>> Ref<EntityStore>.addComponent(component: ComponentType<EntityStore, T>) {
	store.addComponent(this, component)
}

/**
 * Ensure and get a component from an EntityStore reference.
 *
 * @receiver Ref<EntityStore> The reference to the EntityStore.
 * @param componentType The component type to ensure and get.
 * @return The ensured component.
 */
fun <T : Component<EntityStore>> Ref<EntityStore>.ensureAndGetComponent(componentType: ComponentType<EntityStore, T>): T {
	return store.ensureAndGetComponent(this, componentType)
}

/**
 * Get a component from an EntityStore reference.
 *
 * @receiver Ref<EntityStore> The reference to the EntityStore.
 * @param componentType The component type to get.
 * @return The component if it exists, null otherwise.
 */
fun <T : Component<EntityStore>> Ref<EntityStore>.getComponent(componentType: ComponentType<EntityStore, T>): T? {
	return store.getComponent(this, componentType)
}

/**
 * Get a component from an EntityStore reference by component ID.
 *
 * @receiver Ref<EntityStore> The reference to the EntityStore.
 * @param componentId The ID of the component to get.
 * @return The component if it exists, null otherwise.
 */
fun <T : Component<EntityStore>> Ref<EntityStore>.getComponent(componentId: String): T? {
	val componentType = Components.getType<T>(componentId) ?: return null

	return store.getComponent(this, componentType)
}