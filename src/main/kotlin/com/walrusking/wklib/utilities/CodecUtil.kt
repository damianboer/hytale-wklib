package com.walrusking.wklib.utilities

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.codecs.map.MapCodec
import com.walrusking.wklib.components.WKBaseComponent
import com.walrusking.wklib.logging.WKLogger
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*
import java.util.function.Supplier

/*
 * Utility class for building codecs for WKBaseComponent and configuration classes.
 */
class CodecUtil {
	companion object {
		/**
		 * Builds a BuilderCodec for the given WKBaseComponent instance.
		 *
		 * @param T The type of WKBaseComponent
		 * @param component The WKBaseComponent instance to build the codec for
		 * @return A BuilderCodec for the specified WKBaseComponent
		 */
		fun <T : WKBaseComponent<*>> buildComponentCodec(component: T): BuilderCodec<T> {
			val clazz = component.javaClass
			val builder = BuilderCodec.builder(clazz, getSupplierInstance(clazz))

			forEachField(clazz, includeSuper = false) { field ->
				val fieldType = getFieldType(field)
				if (fieldType == CodecFieldType.NONE) {
					return@forEachField
				}

				val keyedCodec = getKeyedCodec(field, fieldType) ?: return@forEachField

				val fieldBuilder = builder.append(keyedCodec, { instance, value ->
					try {
						val toSet = ensureMutableMap(value)
						field.set(instance, toSet)
					} catch (e: IllegalAccessException) {
						e.printStackTrace()
					}
				}, { instance ->
					try {
						field.get(instance)
					} catch (e: IllegalAccessException) {
						e.printStackTrace()
						null
					}
				})

				component.buildCodec(field.name, fieldBuilder)

				fieldBuilder.add()
			}

			return builder.build()
		}

		/**
		 * Builds a BuilderCodec for the given configuration class.
		 *
		 * @param T The type of the configuration class
		 * @param clazz The Class object of the configuration class
		 * @return A BuilderCodec for the specified configuration class
		 */
		fun <T> buildConfigCodec(clazz: Class<T>): BuilderCodec<T> {
			val builder = BuilderCodec.builder(clazz, getSupplierInstance(clazz))

			forEachField(clazz, includeSuper = true) { field ->
				val fieldType = getFieldType(field)
				if (fieldType == CodecFieldType.NONE) {
					return@forEachField
				}

				val keyedCodec = getKeyedCodec(field, fieldType) ?: return@forEachField

				val fieldBuilder = builder.append(keyedCodec, { instance, value ->
					try {
						val toSet = ensureMutableMap(value)
						field.set(instance, toSet)
					} catch (e: IllegalAccessException) {
						e.printStackTrace()
					}
				}, { instance ->
					try {
						field.get(instance)
					} catch (e: IllegalAccessException) {
						e.printStackTrace()
						null
					}
				})

				fieldBuilder.add()
			}

			return builder.build()
		}

		private fun ensureMutableMap(value: Any?): Any? {
			if (value is Map<*, *>) {
				val implName = value.javaClass.name
				if (implName.contains("EmptyMap") || implName.contains("Unmodifiable") || implName.contains("Collections\$")) {
					@Suppress("UNCHECKED_CAST")
					return HashMap(value as Map<Any?, Any?>)
				}
			}
			return value
		}

		private fun forEachField(clazz: Class<*>, includeSuper: Boolean = false, action: (Field) -> Unit) {
			var current: Class<*>? = clazz
			while (current != null && current != Any::class.java) {
				for (field in current.declaredFields) {
					if (Modifier.isStatic(field.modifiers)) {
						continue
					}
					field.trySetAccessible()
					action(field)
				}

				if (!includeSuper) break
				current = current.superclass
			}
		}

		private fun getFieldType(field: Field): CodecFieldType {
			val type = field.type
			val codecFieldType = getType(type)

			if (codecFieldType == CodecFieldType.NONE) {
				val codec = getCodecFromType(field)
				if (codec != null) {
					return CodecFieldType.CLASS
				}
			}

			return codecFieldType
		}

		private fun getType(type: Class<*>): CodecFieldType {
			return when {
				type == UUID::class.java -> CodecFieldType.UUID
				type == String::class.java -> CodecFieldType.STRING
				type == Int::class.javaPrimitiveType || type == Int::class.javaObjectType -> CodecFieldType.INT
				type == Double::class.javaPrimitiveType || type == Double::class.javaObjectType -> CodecFieldType.DOUBLE
				type == Float::class.javaPrimitiveType || type == Float::class.javaObjectType -> CodecFieldType.FLOAT
				type == Long::class.javaPrimitiveType || type == Long::class.javaObjectType -> CodecFieldType.LONG
				type == Boolean::class.javaPrimitiveType || type == Boolean::class.javaObjectType -> CodecFieldType.BOOLEAN
				Map::class.java.isAssignableFrom(type) || type.name == "kotlin.collections.MutableMap" -> CodecFieldType.MAP
				else -> CodecFieldType.NONE
			}
		}

		@Suppress("UNCHECKED_CAST")
		private fun getCodecType(fieldType: CodecFieldType): Codec<Any?>? {
			return when (fieldType) {
				CodecFieldType.UUID -> Codec.STRING as Codec<Any?>
				CodecFieldType.STRING -> Codec.STRING as Codec<Any?>
				CodecFieldType.INT -> Codec.INTEGER as Codec<Any?>
				CodecFieldType.DOUBLE -> Codec.DOUBLE as Codec<Any?>
				CodecFieldType.FLOAT -> Codec.FLOAT as Codec<Any?>
				CodecFieldType.LONG -> Codec.LONG as Codec<Any?>
				CodecFieldType.BOOLEAN -> Codec.BOOLEAN as Codec<Any?>
				else -> null
			}
		}

		@Suppress("UNCHECKED_CAST")
		private fun getKeyedCodec(field: Field, fieldType: CodecFieldType): KeyedCodec<Any?>? {
			val fieldName = field.name.replaceFirstChar { it.uppercaseChar() }

			return when (fieldType) {
				CodecFieldType.CLASS -> {
					val codec = getCodecFromType(field) ?: return null
					KeyedCodec(fieldName, codec)
				}

				CodecFieldType.MAP -> {
					val codec = getMapCodec(field) ?: return null
					KeyedCodec(fieldName, codec)
				}

				else -> {
					val codec = getCodecType(fieldType) ?: return null
					KeyedCodec(fieldName, codec)
				}
			} as KeyedCodec<Any?>?
		}

		@Suppress("UNCHECKED_CAST")
		private fun getCodecFromType(fieldType: Field): Codec<Any?>? {
			val clazz = fieldType.type

			return buildConfigCodec(clazz) as Codec<Any?>?
		}

		@Suppress("UNCHECKED_CAST")
		private fun getMapCodec(field: Field): MapCodec<Any?, HashMap<String, Any?>?>? {
			val parameterized = field.genericType as? java.lang.reflect.ParameterizedType ?: return null
			val keyType = parameterized.actualTypeArguments.getOrNull(0)
			val valueType = parameterized.actualTypeArguments.getOrNull(1)

			val keyClass = keyType as? Class<*> ?: return null
			val valueClass = valueType as? Class<*> ?: return null

			if (keyClass != String::class.java) {
				WKLogger("WKLib:CodecUtil").error("Map key type is not String for field ${field.name}")
				return null
			}

			val valueCodecType = getType(valueClass)
			val simpleCodec = getCodecType(valueCodecType)
			val valueCodec: Codec<Any?>? = when {
				simpleCodec != null -> simpleCodec
				valueCodecType == CodecFieldType.MAP -> {
					WKLogger("WKLib:CodecUtil").warn("Nested collection types not supported for map value for field ${field.name}")
					return null
				}

				else -> {
					buildConfigCodec(valueClass) as Codec<Any?>?
				}
			}

			return MapCodec(valueCodec) {
				try {
					HashMap<String, Any?>()
				} catch (e: Exception) {
					HashMap<String, Any?>()
				}
			}
		}

		private fun <T> getSupplierInstance(clazz: Class<T>): Supplier<T> {
			if (clazz.isInterface || Modifier.isAbstract(clazz.modifiers)) {
				val impl: Class<out Any>? = when {
					List::class.java.isAssignableFrom(clazz) -> ArrayList::class.java
					Map::class.java.isAssignableFrom(clazz) -> HashMap::class.java
					Set::class.java.isAssignableFrom(clazz) -> HashSet::class.java
					Collection::class.java.isAssignableFrom(clazz) -> ArrayList::class.java
					else -> null
				}

				if (impl != null) {
					@Suppress("UNCHECKED_CAST")
					return Supplier {
						try {
							(impl as Class<T>).getDeclaredConstructor().newInstance()
						} catch (e: Exception) {
							throw RuntimeException(
								"Failed to instantiate implementation ${impl.name} for ${clazz.name}",
								e
							)
						}
					}
				}

				return Supplier { throw IllegalStateException("No default implementation for ${clazz.name}") }
			}

			return Supplier {
				try {
					val instance = clazz.getDeclaredConstructor().newInstance()

					var current: Class<*>? = clazz
					while (current != null && current != Any::class.java) {
						for (field in current.declaredFields) {
							if (Modifier.isStatic(field.modifiers)) continue
							try {
								field.trySetAccessible()
								val fType = field.type
								if (Map::class.java.isAssignableFrom(fType) || fType.name == "kotlin.collections.MutableMap") {
									val value = field.get(instance) as? Map<*, *>
									if (value == null) {
										@Suppress("UNCHECKED_CAST")
										field.set(instance, HashMap<Any?, Any?>())
									} else {
										val implName = value.javaClass.name
										if (implName.contains("EmptyMap") || implName.contains("Unmodifiable") || implName.contains(
												"Collections\$"
											)
										) {
											@Suppress("UNCHECKED_CAST")
											field.set(instance, HashMap(value))
										}
									}
								}
							} catch (_: Exception) {
							}
						}
						current = current.superclass
					}

					instance
				} catch (e: Exception) {
					throw RuntimeException("Failed to instantiate ${clazz.name}", e)
				}
			}
		}

		private fun <T> getComponentSupplierInstance(component: WKBaseComponent<*>): Supplier<T> {
			@Suppress("UNCHECKED_CAST")
			val clazz = component.javaClass as Class<T>
			return Supplier { clazz.getDeclaredConstructor().newInstance() }
		}
	}
}