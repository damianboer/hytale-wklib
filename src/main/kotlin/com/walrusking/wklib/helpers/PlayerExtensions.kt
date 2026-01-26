package com.walrusking.wklib.helpers

import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef

/**
 * Get the PlayerRef component associated with a Player.
 *
 * @receiver Player The player instance from which to retrieve the PlayerRef.
 * @return PlayerRef? The PlayerRef component if it exists and is valid; otherwise, null.
 */
fun Player.getRef(): PlayerRef? {
	val ref = reference
	if (ref == null || !ref.isValid) {
		return null
	}

	val store = ref.store

	return store.getComponent(ref, PlayerRef.getComponentType())
}