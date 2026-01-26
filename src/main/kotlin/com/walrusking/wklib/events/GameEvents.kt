package com.walrusking.wklib.events

import com.hypixel.hytale.builtin.adventure.objectives.events.TreasureChestOpeningEvent
import com.hypixel.hytale.server.core.event.events.ShutdownEvent

class GameEvents {
	companion object {
		/** Handles the ShutdownEvent by running all registered handlers. */
		fun onShutdown(event: ShutdownEvent?) =
			Events.onShutdown.runHandlers(event)

		/** Handles the TreasureChestOpeningEvent by running all registered handlers. */
		fun onTreasureChestOpening(event: TreasureChestOpeningEvent) =
			Events.onTreasureChestOpening.runHandlers(event)
	}
}
