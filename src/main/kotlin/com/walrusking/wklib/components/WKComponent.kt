package com.walrusking.wklib.components

import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.builder.BuilderField
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.component.Component as HComponent

/**
 * Base class for all WKLib components.
 */
abstract class WKBaseComponent<T>(
	/** The unique identifier for the component */
	val id: String
) : HComponent<T> {
	abstract override fun clone(): HComponent<T>

	/**
	 * Edit the field codec builder to add custom serialization behavior.
	 *
	 * @param fieldName The name of the field
	 * @param field The field builder
	 */
	open fun buildCodec(
		fieldName: String,
		field: BuilderField.FieldBuilder<out Any?, in Any, out BuilderCodec.BuilderBase<out Any?, *>?>
	) {

	}
}

/**
 * Base class for entity components.
 */
abstract class WKComponent<T>(id: String) : WKBaseComponent<EntityStore>(id)

/**
 * Base class for block components.
 */
abstract class WKBlockComponent<T>(id: String) : WKBaseComponent<ChunkStore>(id)