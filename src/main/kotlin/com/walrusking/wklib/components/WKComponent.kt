package com.walrusking.wklib.components

import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.builder.BuilderField
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.hypixel.hytale.component.Component as HComponent

abstract class WKBaseComponent<T>(val id: String) : HComponent<T> {
	abstract override fun clone(): HComponent<T>

	open fun buildCodec(
		fieldName: String,
		field: BuilderField.FieldBuilder<out Any?, in Any, out BuilderCodec.BuilderBase<out Any?, *>?>
	) {
		
	}
}

abstract class WKComponent<T>(id: String) : WKBaseComponent<EntityStore>(id)

abstract class WKBlockComponent<T>(id: String) : WKBaseComponent<ChunkStore>(id)