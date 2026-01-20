package com.walrusking.wklib.helpers

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

fun Ref<EntityStore>.getExternalData() = store.externalData

fun Ref<EntityStore>.getWorld() = store.externalData.world

fun <T : Component<EntityStore>> Ref<EntityStore>.addComponent(component: ComponentType<EntityStore, T>) {
	store.addComponent(this, component)
}

fun <T : Component<EntityStore>> Ref<EntityStore>.ensureAndGetComponent(componentType: ComponentType<EntityStore, T>): T {
	return store.ensureAndGetComponent(this, componentType)
}

fun <T : Component<EntityStore>> Ref<EntityStore>.getComponent(componentType: ComponentType<EntityStore, T>): T? {
	return store.getComponent(this, componentType)
}