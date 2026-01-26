package com.walrusking.wklib.components

import com.hypixel.hytale.component.Component
import com.hypixel.hytale.component.ComponentRegistryProxy
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.system.ISystem
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.walrusking.wklib.logging.WKLogger
import com.walrusking.wklib.utilities.CodecUtil

/**
 * Object for registering and managing components and their systems.
 */
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

		/**
		 * Registers a component class and returns its ComponentType.
		 *
		 * @param C The type of the component extending WKBaseComponent.
		 * @param clazz The class of the component to register.
		 * @return The ComponentType associated with the registered component.
		 * @throws IllegalArgumentException if the component ID is empty or already registered.
		 */
		fun <C : WKBaseComponent<EntityStore>> register(clazz: Class<C>): ComponentType<EntityStore, C> {
			return internalRegister(clazz, registry!!, types, componentLogger)
		}

		/**
		 * Registers a system for EntityStore components.
		 *
		 * @param system The system to register.
		 */
		fun registerSystem(system: ISystem<EntityStore>) {
			systemLogger.info("Registering system: ${system.javaClass.name}")
			registry?.registerSystem(system)
		}

		/**
		 * Registers both a component and its associated system.
		 *
		 * @param T The type of the component extending WKBaseComponent.
		 * @param S The type of the system extending ISystem.
		 * @param componentClass The class of the component to register.
		 * @param systemClass The class of the system to register.
		 * @throws IllegalArgumentException if instantiation of the system fails.
		 */
		fun <T : WKBaseComponent<EntityStore>, S : ISystem<EntityStore>> registerBoth(
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

		/**
		 * Registers a block component class and returns its ComponentType.
		 *
		 * @param C The type of the block component extending WKBaseComponent.
		 * @param clazz The class of the block component to register.
		 * @return The ComponentType associated with the registered block component.
		 * @throws IllegalArgumentException if the component ID is empty or already registered.
		 */
		fun <C : WKBaseComponent<ChunkStore>> registerBlock(clazz: Class<C>): ComponentType<ChunkStore, C> {
			return internalRegister(clazz, chunkRegistry!!, blockTypes, blockComponentLogger)
		}

		/**
		 * Registers a system for ChunkStore components.
		 *
		 * @param system The system to register.
		 */
		fun registerBlockSystem(system: ISystem<ChunkStore>) {
			blockSystemLogger.info("Registering system: ${system.javaClass.name}")
			chunkRegistry?.registerSystem(system)
		}

		/**
		 * Registers both a block component and its associated system.
		 *
		 * @param T The type of the block component extending WKBaseComponent.
		 * @param S The type of the system extending ISystem.
		 * @param componentClass The class of the block component to register.
		 * @param systemClass The class of the system to register.
		 * @throws IllegalArgumentException if instantiation of the system fails.
		 */
		fun <T : WKBaseComponent<ChunkStore>, S : ISystem<ChunkStore>> registerBlockBoth(
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

		/**
		 * Retrieves a registered component type by its ID.
		 *
		 * @param T The type of the component extending Component.
		 * @param componentId The ID of the component to retrieve.
		 * @return The ComponentType associated with the given ID, or null if not found.
		 */
		fun <T : Component<EntityStore>> getType(
			componentId: String
		): ComponentType<EntityStore, T>? {
			@Suppress("UNCHECKED_CAST")
			return types[componentId] as ComponentType<EntityStore, T>?
		}

		/**
		 * Retrieves a registered block component type by its ID.
		 *
		 * @param T The type of the block component extending Component.
		 * @param componentId The ID of the block component to retrieve.
		 * @return The ComponentType associated with the given ID, or null if not found.
		 */
		fun <T : Component<ChunkStore>> getBlockType(
			componentId: String
		): ComponentType<ChunkStore, T>? {
			@Suppress("UNCHECKED_CAST")
			return blockTypes[componentId] as ComponentType<ChunkStore, T>?
		}
	}
}
