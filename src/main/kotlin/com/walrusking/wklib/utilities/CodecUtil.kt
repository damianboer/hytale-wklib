package com.walrusking.wklib.utilities

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.walrusking.wklib.components.WKBaseComponent
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.function.Supplier

class CodecUtil {
	companion object {
		fun <T : WKBaseComponent<*>> buildComponentCodec(component: T): BuilderCodec<T> {
			val clazz = component.javaClass
			val builder = BuilderCodec.builder(clazz, getSupplierInstance(clazz))

			forEachField(clazz, includeSuper = false) { field ->
				val keyedCodec = getKeyedCodec(field) ?: return@forEachField

				val fieldBuilder = builder.append(keyedCodec, { instance, value ->
					try {
						field.set(instance, value)
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

		fun <T> buildConfigCodec(clazz: Class<T>): BuilderCodec<T> {
			val builder = BuilderCodec.builder(clazz, getSupplierInstance(clazz))

			forEachField(clazz, includeSuper = true) { field ->
				val keyedCodec = getKeyedCodec(field) ?: return@forEachField

				val fieldBuilder = builder.append(keyedCodec, { instance, value ->
					try {
						field.set(instance, value)
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

		@Suppress("UNCHECKED_CAST")
		private fun getKeyedCodec(field: Field): KeyedCodec<Any?>? {
			val fieldName = field.name.replaceFirstChar { it.uppercaseChar() }

			return when (field.type) {
				String::class.java -> KeyedCodec(fieldName, Codec.STRING) as KeyedCodec<Any?>
				Int::class.javaPrimitiveType, Int::class.javaObjectType -> KeyedCodec(
					fieldName,
					Codec.INTEGER
				) as KeyedCodec<Any?>

				Double::class.javaPrimitiveType, Double::class.javaObjectType -> KeyedCodec(
					fieldName,
					Codec.DOUBLE
				) as KeyedCodec<Any?>

				Float::class.javaPrimitiveType, Float::class.javaObjectType -> KeyedCodec(
					fieldName,
					Codec.FLOAT
				) as KeyedCodec<Any?>

				Long::class.javaPrimitiveType, Long::class.javaObjectType -> KeyedCodec(
					fieldName,
					Codec.LONG
				) as KeyedCodec<Any?>

				Boolean::class.javaPrimitiveType, Boolean::class.javaObjectType -> KeyedCodec(
					fieldName,
					Codec.BOOLEAN
				) as KeyedCodec<Any?>

				else -> null
			}
		}

		private fun <T> getSupplierInstance(clazz: Class<T>): Supplier<T> {
			return Supplier { clazz.getDeclaredConstructor().newInstance() }
		}

		private fun <T> getComponentSupplierInstance(component: WKBaseComponent<*>): Supplier<T> {
			@Suppress("UNCHECKED_CAST")
			val clazz = component.javaClass as Class<T>
			return Supplier { clazz.getDeclaredConstructor().newInstance() }
		}
	}
}