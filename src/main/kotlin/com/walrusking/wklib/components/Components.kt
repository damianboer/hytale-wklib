package com.walrusking.wklib.components

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.ComponentRegistryProxy
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.logging.WKLogger
import com.walrusking.wklib.utilities.CodecUtil

class Components {
	companion object {
		private val componentLogger = WKLogger("WKLib:Components")
		private val systemLogger = WKLogger("WKLib:Systems")
		private val blockComponentLogger = WKLogger("WKLib:BlockComponents")
		private val blockSystemLogger = WKLogger("WKLib:BlockSystems")

		val types = mutableMapOf<String, ComponentType<EntityStore, *>>()
		val blockTypes = mutableMapOf<String, ComponentType<ChunkStore, *>>()

		var registry: ComponentRegistryProxy<EntityStore>? = null
		var chunkRegistry: ComponentRegistryProxy<ChunkStore>? = null

		fun init(
			registry: ComponentRegistryProxy<EntityStore>,
			chunkRegistry: ComponentRegistryProxy<ChunkStore>
		) {
			this.registry = registry
			this.chunkRegistry = chunkRegistry
		}

		private fun <T, C : WKBaseComponent<T>> internalRegister(
			clazz: Class<C>,
			registry: ComponentRegistryProxy<T>,
			typesMap: MutableMap<String, ComponentType<T, out Component<T>>>,
			logger: WKLogger
		): ComponentType<T, C> {
			val component = clazz.getDeclaredConstructor().newInstance()

			if (component.id.isEmpty()) {
				throw IllegalArgumentException("Component ID cannot be empty!")
			}

			if (component.id in typesMap) {
				throw IllegalArgumentException("Component with ID ${component.id} is already registered!")
			}

			val componentId = component.id

			val type = registry.registerComponent(clazz, componentId, CodecUtil.buildComponentCodec(component))

			typesMap[componentId] = type
			logger.info("Registering component: $componentId")

			return type
		}

		fun <C : WKBaseComponent<EntityStore>> register(clazz: Class<C>): ComponentType<EntityStore, C> {
			return internalRegister(clazz, registry!!, types, componentLogger)
		}

		fun registerSystem(system: WKEntityTickingSystem<out Component<EntityStore>>) {
			systemLogger.info("Registering system: ${system.javaClass.name}")
			registry?.registerSystem(system)
		}

		fun <T : WKBaseComponent<EntityStore>, S : WKEntityTickingSystem<T>> registerBoth(
			componentClass: Class<T>,
			systemClass: Class<S>
		) {
			val componentType = register(componentClass)

			val ctor = systemClass.getDeclaredConstructor(ComponentType::class.java)
			try {
				val system = ctor.newInstance(componentType)
				registerSystem(system)
			} catch (e: Exception) {
				throw IllegalArgumentException("Failed to instantiate ticking system ${systemClass.name}", e)
			}
		}

		fun <C : WKBaseComponent<ChunkStore>> registerBlock(clazz: Class<C>): ComponentType<ChunkStore, C> {
			return internalRegister(clazz, chunkRegistry!!, blockTypes, blockComponentLogger)
		}

		fun registerBlockSystem(system: WKBlockEntityTickingSystem<out Component<ChunkStore>>) {
			blockSystemLogger.info("Registering system: ${system.javaClass.name}")
			chunkRegistry?.registerSystem(system)
		}

		fun <T : WKBaseComponent<ChunkStore>, S : WKBlockEntityTickingSystem<T>> registerBlockBoth(
			componentClass: Class<T>,
			systemClass: Class<S>
		) {
			val componentType = registerBlock(componentClass)

			val ctor = systemClass.getDeclaredConstructor(ComponentType::class.java)
			try {
				val system = ctor.newInstance(componentType)
				registerBlockSystem(system)
			} catch (e: Exception) {
				throw IllegalArgumentException("Failed to instantiate ticking system ${systemClass.name}", e)
			}
		}

		fun <T : Component<EntityStore>> getType(
			componentId: String
		): ComponentType<EntityStore, T>? {
			@Suppress("UNCHECKED_CAST")
			return types[componentId] as ComponentType<EntityStore, T>?
		}

		fun <T : Component<ChunkStore>> getBlockType(
			componentId: String
		): ComponentType<ChunkStore, T>? {
			@Suppress("UNCHECKED_CAST")
			return blockTypes[componentId] as ComponentType<ChunkStore, T>?
		}
	}
}
