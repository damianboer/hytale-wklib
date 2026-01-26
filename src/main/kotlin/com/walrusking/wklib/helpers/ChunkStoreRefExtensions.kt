package com.walrusking.wklib.helpers

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.walrusking.wklib.components.Components

/**
 * Add a component to an ChunkStore reference.
 *
 * @receiver Ref<ChunkStore> The reference to the ChunkStore.
 * @param component The component type to add.
 */
fun <T : Component<ChunkStore>> Ref<ChunkStore>.addComponent(component: ComponentType<ChunkStore, T>) {
	store.addComponent(this, component)
}

/**
 * Ensure and get a component from an ChunkStore reference.
 *
 * @receiver Ref<ChunkStore> The reference to the ChunkStore.
 * @param componentType The component type to ensure and get.
 * @return The ensured component.
 */
fun <T : Component<ChunkStore>> Ref<ChunkStore>.ensureAndGetComponent(componentType: ComponentType<ChunkStore, T>): T {
	return store.ensureAndGetComponent(this, componentType)
}

/**
 * Get a component from an ChunkStore reference.
 *
 * @receiver Ref<ChunkStore> The reference to the ChunkStore.
 * @param componentType The component type to get.
 * @return The component if it exists, null otherwise.
 */
fun <T : Component<ChunkStore>> Ref<ChunkStore>.getComponent(componentType: ComponentType<ChunkStore, T>): T? {
	return store.getComponent(this, componentType)
}

/**
 * Get a component from an ChunkStore reference by component ID.
 *
 * @receiver Ref<ChunkStore> The reference to the ChunkStore.
 * @param componentId The ID of the component to get.
 * @return The component if it exists, null otherwise.
 */
fun <T : Component<ChunkStore>> Ref<ChunkStore>.getComponent(componentId: String): T? {
	val componentType = Components.getBlockType<T>(componentId) ?: return null

	return store.getComponent(this, componentType)
}

/**
 * Get the ExternalData from a ChunkStore reference.
 *
 * @receiver Ref<ChunkStore> The reference to the ChunkStore.
 * @return The ExternalData associated with the ChunkStore.
 */
fun Ref<ChunkStore>.getExternalData() = store.externalData

/**
 * Get the World from a ChunkStore reference.
 *
 * @receiver Ref<ChunkStore> The reference to the ChunkStore.
 * @return The World associated with the ChunkStore.
 */
fun Ref<ChunkStore>.getWorld() = store.externalData.world