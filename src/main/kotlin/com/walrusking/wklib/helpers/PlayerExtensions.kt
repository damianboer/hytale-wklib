package com.walrusking.wklib.helpers

import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef


fun Player.getRef(): PlayerRef? {
	val ref = reference
	if (ref == null || !ref.isValid) {
		return null
	}

	val store = ref.store

	return store.getComponent(ref, PlayerRef.getComponentType())
}