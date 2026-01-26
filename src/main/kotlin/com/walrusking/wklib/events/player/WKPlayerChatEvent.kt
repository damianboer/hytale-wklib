package com.walrusking.wklib.events.player

import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent
import com.hypixel.hytale.server.core.universe.PlayerRef

/**
 * Event triggered when a player sends a chat message.
 *
 * @param sender The player sending the chat message.
 * @param targets The list of players receiving the chat message.
 * @param content The content of the chat message.
 */
class WKPlayerChatEvent(
	sender: PlayerRef,
	targets: List<PlayerRef>,
	content: String
) : PlayerChatEvent(
	sender,
	targets,
	content
)