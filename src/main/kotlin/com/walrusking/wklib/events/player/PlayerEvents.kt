package com.walrusking.wklib.events.player

import com.hypixel.hytale.server.core.event.events.player.*
import com.walrusking.wklib.events.Events

class PlayerEvents {
	companion object {
		/**
		 * Called when a player is ready
		 *
		 * @param event The PlayerReadyEvent instance
		 */
		fun onPlayerReady(event: PlayerReadyEvent) {
			Events.onPlayerReady.runHandlers(
				WKPlayerReadyEvent(
					event.playerRef,
					event.player,
					event.readyId
				)
			)
		}

		/**
		 * Called when a player connects
		 *
		 * @param event The PlayerConnectEvent instance
		 */
		fun onPlayerConnect(event: PlayerConnectEvent) =
			Events.onPlayerConnect.runHandlers(event)

		/**
		 * Called when a player disconnects
		 *
		 * @param event The PlayerDisconnectEvent instance
		 */
		fun onPlayerDisconnect(event: PlayerDisconnectEvent) =
			Events.onPlayerDisconnect.runHandlers(event)

		/**
		 * Called when a player sends a chat message
		 *
		 * @param event The PlayerChatEvent instance
		 */
		fun onPlayerChat(event: PlayerChatEvent) {
			Events.onPlayerChat.runHandlers(
				WKPlayerChatEvent(
					event.sender,
					event.targets,
					event.content
				)
			)
		}

		/**
		 * Called when a player is setting up a connection
		 *
		 * @param event The PlayerSetupConnectEvent instance
		 */
		fun onPlayerSetupConnect(event: PlayerSetupConnectEvent) =
			Events.onPlayerSetupConnect.runHandlers(event)

		/**
		 * Called when a player is disconnecting during setup
		 *
		 * @param event The PlayerSetupDisconnectEvent instance
		 */
		fun onPlayerSetupDisconnect(event: PlayerSetupDisconnectEvent) =
			Events.onPlayerSetupDisconnect.runHandlers(event)

		/**
		 * Called when a player is added to the world
		 *
		 * @param event The AddPlayerToWorldEvent instance
		 */
		fun onPlayerAddedToWorld(event: AddPlayerToWorldEvent) =
			Events.onPlayerAddedToWorld.runHandlers(event)

		/**
		 * Called when a player is removed from the world
		 *
		 * @param event The RemovePlayerFromWorldEvent instance
		 */
		fun onPlayerDrainFromWorld(event: DrainPlayerFromWorldEvent) =
			Events.onPlayerDrainFromWorld.runHandlers(event)
	}
}