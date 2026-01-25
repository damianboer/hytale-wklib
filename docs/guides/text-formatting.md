## Overview
- Tags use angle-bracket syntax similar to HTML: `<tag>` and `</tag>`.
- Tags can be nested. Inner tags override attributes of outer tags for their range.
- Closing tags are required (e.g. `</b>`).
- Color values may be: named color (`red`), hex (`#RRGGBB`), or RGB tuple (`(R,G,B)`).
- Custom named colors may be registered at runtime (see *Custom Colors*).

## Using Text Formatting

You can use it using the `TextFormat.format("string here")` method:

```kotlin
fun onPlayerChat(event: WKPlayerChatEvent) {
	event.sender.sendMessage(TextFormat.format("<c=red>Red Text</c> Normal Text"))
	// You can also use the helper function
	event.sender.sendFormattedMessage("<c=red>Red Text</c> Normal Text")
}
```

## Color tags

Syntax:
- Long form: ``<color=COLOR>text</color>``
- Short form: ``<c=COLOR>text</c>``

COLOR formats:
- Named color: `red`, `green`, `blue`, `customcolor` (if registered)
- Hex: `#FFA500`
- RGB tuple: `(128,0,128)`

Examples:
- ``<color=red>Red text</color>``
- ``<c=#FFA500>Orange</c>``
- ``<c=(128,0,128)>Purple</c>``

Notes:
- Use the same closing tag name as opening (`</color>` or `</c>`).
- If you registered a custom name (see below) you can use it as `COLOR`.

## Text style tags

- Bold: ``<b>Bold text</b>``
- Italic: ``<i>Italic text</i>``
- Underline: ``<u>Underlined</u>``
- Monospace: ``<m>Monospace</m>``
- Obfuscated (scrambled): ``<o>Obfuscated</o>``

Examples:
- ``<b><i>Bold & Italic</i></b>``
- ``<m>console.log("hi")</m>``

## Gradient

Several forms supported:

1. Named attributes:
- ``<gradient from=COLOR to=COLOR>text</gradient>``

2. Comma list with attribute:
- ``<gradient colors=COLOR1,COLOR2,...>text</gradient>``

3. Shorthand assignment:
- ``<gradient=COLOR1,COLOR2,...>text</gradient>``

Examples:
- ``<gradient from=red to=blue>Gradient Text</gradient>``
- ``<gradient colors=red,blue>Gradient Text</gradient>``
- ``<gradient=red,yellow,cyan,blue,purple,#FF00FF>Gradient Text</gradient>``

Notes:
- Any accepted COLOR format may be used.
- The gradient interpolates across the provided colors in order.

## Rainbow

Syntax:
- ``<rainbow>text</rainbow>``

Behavior:
- Applies a multi-color rainbow across `text`. Can be nested inside other tags.

Example:
- ``<rainbow>This is rainbow</rainbow>``

## Links

Syntax:
- ``<a=URL>Clickable text</a>``

Example:
- ``<a=https://google.com>Click here to open Google!</a>``


## Nesting & Precedence

- Tags may be nested arbitrarily, e.g.:
  ``<b>Bold <i>Italic <c=green>Green</c></i></b>``
- Inner tags take precedence for their span.
- Always close tags in the correct order to avoid unexpected formatting.

## Custom Colors

Register anywhere before you use it:
```kotlin
TextFormat.addColor("customcolor", Color(123, 45, 67))
```

Once registered, use:
- ``<c=customcolor>Custom colored text</c>``

## Quick examples

- ``<b>Bold</b>``
- ``<i><c=blue>Italic blue</c></i>``
- ``<gradient=red,blue>Red-to-blue</gradient>``
- ``<rainbow><b>Bold rainbow</b></rainbow>``
- ``<a=https://example.com>Open example</a>``
