package com.walrusking.wklib.utilities

import com.hypixel.hytale.protocol.MaybeBool
import com.hypixel.hytale.server.core.Message
import java.awt.Color
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * TextFormat is a utility class for formatting text with various styles and colors.
 * It supports tags for bold, italic, underline, color, links, gradients, rainbows, and obfuscation.
 */
class TextFormat {
	companion object {
		val colors = mutableMapOf(
			"black" to Color(0, 0, 0),
			"white" to Color(255, 255, 255),
			"red" to Color(255, 0, 0),
			"green" to Color(0, 255, 0),
			"blue" to Color(0, 0, 255),
			"yellow" to Color(255, 255, 0),
			"cyan" to Color(0, 255, 255),
			"magenta" to Color(255, 0, 255),
			"gray" to Color(128, 128, 128),
			"orange" to Color(255, 165, 0),
			"purple" to Color(128, 0, 128),
			"pink" to Color(255, 192, 203),
			"brown" to Color(165, 42, 42)
		)

		/**
		 * Formats the input string into a Message with styles and colors.
		 *
		 * Supported tags:
		 * <b> or <bold> - Bold text
		 * <i> or <italic> - Italic text
		 * <u> or <underline> - Underlined text
		 * <c=value> or <color=value> - Colored text (hex, rgb, or named color)
		 * <a=value> or <link=value> - Hyperlink text
		 * <gradient colors=value1,value2,...> - Gradient colored text
		 * <rainbow> - Rainbow colored text
		 * <o>, <obfuscated>, or <obf> - Obfuscated text
		 *
		 * @param input The input string with formatting tags.
		 * @return A Message object with the applied formatting.
		 */
		fun format(input: String): Message {
			val lexer = TextLexer()
			val tokens = lexer.tokenize(input)

			val parser = TextParser()
			return parser.parse(tokens)
		}

		/**
		 * Adds a named color to the color map.
		 *
		 * @param name The name of the color.
		 * @param color The Color object.
		 */
		fun addColor(name: String, color: Color) {
			colors[name.lowercase()] = color
		}
	}
}

class TextLexer {
	fun tokenize(input: String): List<TextToken> {
		val tokens = mutableListOf<TextToken>()
		var i = 0
		while (i < input.length) {
			if (input[i] == '<') {
				val end = input.indexOf('>', i + 1)
				if (end == -1) {
					tokens.add(TextToken("TEXT", input.substring(i)))
					break
				}
				val content = input.substring(i + 1, end).trim()
				if (content.startsWith("/")) {
					val raw = content.substring(1).trim()
					val name = raw.split(Regex("\\s+"))[0]
					tokens.add(TextToken("TAG_CLOSE", name))
				} else {
					tokens.add(TextToken("TAG_OPEN", content))
				}
				i = end + 1
			} else {
				val nextTag = input.indexOf('<', i)
				val end = if (nextTag == -1) input.length else nextTag
				tokens.add(TextToken("TEXT", input.substring(i, end)))
				i = end
			}
		}
		return tokens
	}
}


class TextParser {
	private lateinit var tokens: List<TextToken>
	private var index = 0

	fun parse(tokens: List<TextToken>): Message {
		this.tokens = tokens
		index = 0
		return parseRecursive(null).message
	}

	private data class ParseResult(val message: Message, val text: String)

	private fun parseRecursive(endTag: String?): ParseResult {
		var messageBuilder = Message.empty()
		val textSb = StringBuilder()

		while (index < tokens.size) {
			val t = tokens[index++]
			when (t.type) {
				"TEXT" -> {
					messageBuilder = messageBuilder.insert(escapeHtml(t.value))
					textSb.append(t.value)
				}

				"TAG_OPEN" -> {
					val (tag, rest) = splitTag(t.value)
					val attrs = parseAttrs(rest)
					when (tag.lowercase()) {
						"b", "bold" -> {
							val inner = parseRecursive(tag)
							messageBuilder = messageBuilder.insert(inner.message.bold(true))
							textSb.append(inner.text)
						}

						"i", "italic" -> {
							val inner = parseRecursive(tag)
							messageBuilder = messageBuilder.insert(inner.message.italic(true))
							textSb.append(inner.text)
						}

						"u", "underline" -> {
							val inner = parseRecursive(tag)
							inner.message.formattedMessage.underlined = MaybeBool.True
							messageBuilder = messageBuilder.insert(inner.message)
							textSb.append(inner.text)
						}

						"m", "mono" -> {
							val inner = parseRecursive(tag)
							inner.message.monospace(true)
							messageBuilder = messageBuilder.insert(inner.message)
							textSb.append(inner.text)
						}

						"c", "color" -> {
							val inner = parseRecursive(tag)
							val color = attrs["value"] ?: "#ffffff"

							inner.message.color(parseColor(color))

							messageBuilder = messageBuilder.insert(inner.message)
							textSb.append(inner.text)
						}

						"a", "link" -> {
							val inner = parseRecursive(tag)
							val url = attrs["value"] ?: inner.text

							inner.message.link(url)
							inner.message.formattedMessage.underlined = MaybeBool.True

							messageBuilder = messageBuilder.insert(inner.message)
							textSb.append(inner.text)
						}

						"gradient" -> {
							val inner = parseRecursive(tag)
							val innerStr = inner.text
							val length = innerStr.length

							val colorsList: List<Color> = when {
								attrs.containsKey("colors") || attrs.containsKey("value") -> {
									val raw = attrs["colors"] ?: attrs["value"]!!
									parseColors(raw)
								}

								attrs.containsKey("from") || attrs.containsKey("to") -> {
									val from = attrs["from"] ?: "#ffffff"
									val to = attrs["to"] ?: "#000000"
									listOf(parseColor(from), parseColor(to))
								}

								else -> listOf(Color.WHITE, Color.BLACK)
							}

							if (length > 0 && colorsList.isNotEmpty()) {
								var gradientMessage = Message.empty()
								for (i in innerStr.indices) {
									val progress = if (length == 1) 0f else i.toFloat() / (length - 1)
									val color = getGradientColor(colorsList, progress)
									gradientMessage = gradientMessage.insert(
										Message.empty().insert(escapeHtml(innerStr[i].toString())).color(color)
									)
								}
								messageBuilder = messageBuilder.insert(gradientMessage)
								textSb.append(innerStr)
							}
						}

						"rainbow" -> {
							val inner = parseRecursive(tag)
							val innerStr = inner.text
							val length = innerStr.length
							if (length > 0) {
								var rainbowMessage = Message.empty()
								for (i in innerStr.indices) {
									val progress = i.toFloat() / (length - 1).coerceAtLeast(1).toFloat()
									val hue = progress
									val color = Color.getHSBColor(hue, 1.0f, 1.0f)
									val hexColor = String.format("#%02x%02x%02x", color.red, color.green, color.blue)
									rainbowMessage = rainbowMessage.insert(
										Message.empty().insert(escapeHtml(innerStr[i].toString())).color(hexColor)
									)
								}
								messageBuilder = messageBuilder.insert(rainbowMessage)
								textSb.append(innerStr)
							}
						}

						"o", "obfuscated", "obf" -> {
							val inner = parseRecursive(tag)
							val obf = obfuscate(inner.text)
							messageBuilder = messageBuilder.insert(Message.empty().insert(escapeHtml(obf)))
							textSb.append(obf)
						}

						else -> {
							val inner = parseRecursive(tag)
							messageBuilder = messageBuilder.insert(inner.message)
							textSb.append(inner.text)
						}
					}
				}

				"TAG_CLOSE" -> {
					if (endTag != null && t.value.equals(endTag, ignoreCase = true)) {
						return ParseResult(messageBuilder, textSb.toString())
					}
				}
			}
		}

		return ParseResult(messageBuilder, textSb.toString())
	}

	private fun parseColors(raw: String): List<Color> {
		// Match #hex, (r,g,b) or any non-comma token (named colors)
		val tokenRegex = Regex("#[A-Fa-f0-9]{3,6}|\\([^)]*\\)|[^,\\s]+")
		return tokenRegex.findAll(raw).map { parseColor(it.value.trim()) }.toList()
	}

	private fun getGradientColor(colors: List<Color>, progress: Float): Color {
		if (colors.isEmpty()) return Color.WHITE
		if (colors.size == 1) return colors[0]

		val p = progress.coerceIn(0f, 1f)
		val scaled = p * (colors.size - 1)
		val index = scaled.toInt().coerceIn(0, colors.size - 2)
		val local = scaled - index

		val c1 = colors[index]
		val c2 = colors[index + 1]

		val r = (c1.red + (c2.red - c1.red) * local).roundToInt().coerceIn(0, 255)
		val g = (c1.green + (c2.green - c1.green) * local).roundToInt().coerceIn(0, 255)
		val b = (c1.blue + (c2.blue - c1.blue) * local).roundToInt().coerceIn(0, 255)

		return Color(r, g, b)
	}

	private fun parseColor(from: String): Color {
		val isHex = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$").matches(from)
		if (isHex) {
			return Color.decode(from)
		}

		val rgbRegex = Regex("^\\((\\d{1,3}),(\\d{1,3}),(\\d{1,3})\\)$")
		val rgbMatch = rgbRegex.find(from)
		if (rgbMatch != null) {
			val r = rgbMatch.groupValues[1].toInt()
			val g = rgbMatch.groupValues[2].toInt()
			val b = rgbMatch.groupValues[3].toInt()
			return Color(r, g, b)
		}

		val namedColor = TextFormat.colors[from.lowercase()]
		if (namedColor != null) {
			return namedColor
		}

		return Color.WHITE
	}

	private fun splitTag(raw: String): Pair<String, String> {
		val m = Regex("^([a-zA-Z]+)(.*)$").find(raw)
		return if (m != null) {
			val tag = m.groupValues[1]
			val rest = m.groupValues[2].trim()
			Pair(tag, rest)
		} else {
			Pair(raw.trim(), "")
		}
	}

	private fun parseAttrs(raw: String): Map<String, String> {
		val map = mutableMapOf<String, String>()
		val s = raw.trim()
		if (s.isEmpty()) return map

		// Hex or named color shortcut
		if (s.startsWith("=")) {
			map["value"] = s.substring(1).trim()
			return map
		}
		// RGB values
		val regex = Regex("(\\w+)=((?:\\([^)]*\\))|[^\\s]+)")
		regex.findAll(s).forEach { mr ->
			val k = mr.groupValues[1]
			val v = mr.groupValues[2]
			map[k] = v
		}

		// String value shortcut
		if (map.isEmpty()) {
			map["value"] = s
		}
		return map
	}

	private fun escapeHtml(s: String): String {
		return s
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&#39;")
	}

	private fun obfuscate(s: String): String {
		val sb = StringBuilder()
		val rnd = Random(System.nanoTime())
		for (c in s) {
			if (c.isWhitespace()) {
				sb.append(c)
			} else {
				val choice = rnd.nextInt(3)
				val ch = when (choice) {
					0 -> ('a' + rnd.nextInt(26))
					1 -> ('0' + rnd.nextInt(10))
					else -> (33 + rnd.nextInt(15)).toChar()
				}
				if (c.isUpperCase()) sb.append(ch.uppercaseChar()) else sb.append(ch)
			}
		}
		return sb.toString()
	}
}

data class TextToken(
	val type: String,
	val value: String
) {
	override fun toString(): String = "$type:'$value'"
}