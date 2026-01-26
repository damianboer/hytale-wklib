package com.walrusking.wklib.helpers

import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.util.NotificationUtil
import com.walrusking.wklib.utilities.TextFormat

/**
 * Gets the Player object associated with the PlayerRef.
 *
 * @receiver PlayerRef The reference to the player.
 * @return Player? The Player object if valid, otherwise null.
 */
fun PlayerRef.getPlayer(): Player? {
	val ref = reference

	if (ref == null || !ref.isValid) {
		return null
	}

	val store = ref.store

	return store.getComponent(ref, Player.getComponentType())
}

/**
 * Sends a notification to the player associated with the PlayerRef.
 *
 * @receiver PlayerRef The reference to the player.
 * @param message Message The message to be sent as a notification.
 */
fun PlayerRef.sendNotification(message: Message) {
	NotificationUtil.sendNotification(packetHandler, message)
}

/**
 * Sends a notification to the player associated with the PlayerRef.
 *
 * @receiver PlayerRef The reference to the player.
 * @param message String The message to be sent as a notification.
 */
fun PlayerRef.sendNotification(message: String) {
	NotificationUtil.sendNotification(packetHandler, message)
}

/**
 * Sends a notification with primary and secondary messages to the player associated with the PlayerRef.
 *
 * @receiver PlayerRef The reference to the player.
 * @param primaryMessage Message The primary message of the notification.
 * @param secondaryMessage Message The secondary message of the notification.
 */
fun PlayerRef.sendNotification(primaryMessage: Message, secondaryMessage: Message) {
	NotificationUtil.sendNotification(packetHandler, primaryMessage, secondaryMessage)
}

/**
 * Sends a notification with primary and secondary messages to the player associated with the PlayerRef.
 *
 * @receiver PlayerRef The reference to the player.
 * @param primaryMessage String The primary message of the notification.
 * @param secondaryMessage String The secondary message of the notification.
 */
fun PlayerRef.sendNotification(primaryMessage: String, secondaryMessage: String) {
	NotificationUtil.sendNotification(packetHandler, primaryMessage, secondaryMessage)
}

/**
 * Sends a formatted message to the player associated with the PlayerRef.
 *
 * @receiver PlayerRef The reference to the player.
 * @param message String The message to be sent.
 */
fun PlayerRef.sendFormattedMessage(message: String) {
	sendMessage(TextFormat.format(message))
}

/**
 * Sends a formatted notification to the player associated with the PlayerRef.
 *
 * @receiver PlayerRef The reference to the player.
 * @param message String The message to be sent as a notification.
 */
fun PlayerRef.sendFormattedNotification(message: String) {
	sendNotification(TextFormat.format(message))
}

/**
 * Sends a formatted notification with primary and secondary messages to the player associated with the PlayerRef.
 *
 * @receiver PlayerRef The reference to the player.
 * @param primaryMessage String The primary message of the notification.
 * @param secondaryMessage String The secondary message of the notification.
 */
fun PlayerRef.sendFormattedNotification(primaryMessage: String, secondaryMessage: String) {
	sendNotification(TextFormat.format(primaryMessage), TextFormat.format(secondaryMessage))
}

/**
 * Retrieves the EntityStatMap component of the player associated with the PlayerRef. Such as health, mana, etc.
 *
 * @receiver PlayerRef The reference to the player.
 * @return EntityStatMap? The EntityStatMap component if valid, otherwise null.
 */
fun PlayerRef.getStats(): EntityStatMap? {
	val ref = reference
	if (ref == null || !ref.isValid) {
		return null
	}

	return ref.getComponent(EntityStatMap.getComponentType())
}