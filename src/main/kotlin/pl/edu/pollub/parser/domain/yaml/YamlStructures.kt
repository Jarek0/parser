package pl.edu.pollub.parser.domain.yaml

import pl.edu.pollub.parser.domain.Tag

enum class YamlTag(val body: String, val level: YamlTagLevel = YamlTagLevel.SUB_FIELD): Tag {
    COMPUTERS("laptops:", YamlTagLevel.MAIN),
    COMPUTER("laptop:", YamlTagLevel.ELEMENT),
    MANUFACTURER("manufacturer:", YamlTagLevel.SIMPLE_FIELD),
    SCREEN("screen:", YamlTagLevel.COMPLEX_FIELD),
    SIZE("size:"),
    RESOLUTION("resolution:"),
    TYPE("type:"),
    TOUCHSCREEN("touchscreen:"),
    PROCESSOR("processor:", YamlTagLevel.COMPLEX_FIELD),
    NAME("name:"),
    PHYSICAL_CORES("physical_cores:"),
    CLOCK_SPEED("clock_speed:"),
    RAM("ram:", YamlTagLevel.SIMPLE_FIELD),
    DISC("disc:", YamlTagLevel.COMPLEX_FIELD),
    STORAGE("storage:"),
    GRAPHIC_CARD("graphic_card:", YamlTagLevel.COMPLEX_FIELD),
    MEMORY("memory:"),
    OPERATION_SYSTEM("os:", YamlTagLevel.SIMPLE_FIELD),
    DISC_READER("disc_reader:", YamlTagLevel.SIMPLE_FIELD),
    MANY_APPEARANCE("-", YamlTagLevel.ELEMENT);

    override fun getName() = name

    override fun parse(text: String): String {
        val tagMatcher = YamlTagMatcher(text, this)
        if(tagMatcher.isNotFound()) return ""
        return tagMatcher.getTagValue()
    }
}

enum class YamlTagLevel(val defaultSpacesCount: Int) {
    MAIN(0),
    ELEMENT(1),
    COMPLEX_FIELD(2),
    SIMPLE_FIELD(2){
        override fun normalize(text: String): String {
            val trimmedValue = text.trimStart().trimEnd()
            return super.normalize(trimmedValue)
        }
    },
    SUB_FIELD(4){
        override fun normalize(text: String): String {
            val trimmedValue = text.trimStart().trimEnd()
            return super.normalize(trimmedValue)
        }
    };

    open fun normalize(text: String): String {
        if(text.isSurroundBy("\"")) {
            return text.removeSurrounding("\"")
        }
        if(text.isSurroundBy("'")) {
            return text.removeSurrounding("'")
        }
        return text
    }

    private fun String.isSurroundBy(prefixSuffix: String) = this.startsWith(prefixSuffix) && this.endsWith(prefixSuffix)

    fun isHigherThanOrEqualTo(other: YamlTagLevel) = defaultSpacesCount <= other.defaultSpacesCount

}