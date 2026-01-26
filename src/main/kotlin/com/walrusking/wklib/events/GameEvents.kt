package com.walrusking.wklib.events

import com.hypixel.hytale.builtin.adventure.objectives.events.TreasureChestOpeningEvent
import com.hypixel.hytale.server.core.event.events.ShutdownEvent
import com.hypixel.hytale.server.core.universe.world.events.AddWorldEvent
import com.hypixel.hytale.server.core.universe.world.events.RemoveWorldEvent
import com.hypixel.hytale.server.core.universe.world.events.StartWorldEvent

class GameEvents {
	companion object {
		/** Handles the ShutdownEvent by running all registered handlers. */
		fun onShutdown(event: ShutdownEvent?) =
			Events.onShutdown.runHandlersNullable(event)

		/** Handles the TreasureChestOpeningEvent by running all registered handlers. */
		fun onTreasureChestOpening(event: TreasureChestOpeningEvent) =
			Events.onTreasureChestOpening.runHandlers(event)

		/** Handles the AddWorldEvent by running all registered handlers. */
		fun onWorldAdded(event: AddWorldEvent) =
			Events.onWorldAdded.runHandlers(event)

		/** Handles the RemoveWorldEvent by running all registered handlers. */
		fun onWorldRemoved(event: RemoveWorldEvent) =
			Events.onWorldRemoved.runHandlers(event)

		/** Handles the StartWorldEvent by running all registered handlers. */
		fun onWorldStarted(event: StartWorldEvent) =
			Events.onWorldStarted.runHandlers(event)
	}
}
