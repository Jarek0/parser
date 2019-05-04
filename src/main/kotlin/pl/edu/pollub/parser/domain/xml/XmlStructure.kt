package pl.edu.pollub.parser.domain.xml

import pl.edu.pollub.parser.domain.csv.appendNextLine
import pl.edu.pollub.parser.domain.csv.appendNextLines
import java.lang.StringBuilder

enum class Tag(val start: String, val end: String, val level: TagLevel = TagLevel.SUB_FIELD) {
    COMPUTERS("<laptops>", "</laptops>", TagLevel.MAIN),
    COMPUTER("<laptop>", "</laptop>", TagLevel.ELEMENT),
    MANUFACTURER("<manufacturer>", "</manufacturer>", TagLevel.SIMPLE_FIELD),
    SCREEN("<screen>", "</screen>", TagLevel.COMPLEX_FIELD),
    SIZE("<size>", "</size>"),
    RESOLUTION("<resolution>", "</resolution>"),
    TYPE("<type>", "</type>"),
    TOUCHSCREEN("<touchscreen>", "</touchscreen>"),
    PROCESSOR("<processor>", "</processor>", TagLevel.COMPLEX_FIELD),
    NAME("<name>", "</name>"),
    PHYSICAL_CORES("<physical_cores>", "</physical_cores>"),
    CLOCK_SPEED("<clock_speed>", "</clock_speed>"),
    RAM("<ram>", "</ram>", TagLevel.SIMPLE_FIELD),
    DISC("<disc>", "</disc>", TagLevel.COMPLEX_FIELD),
    STORAGE("<storage>", "</storage>"),
    GRAPHIC_CARD("<graphic_card>", "</graphic_card>", TagLevel.COMPLEX_FIELD),
    MEMORY("<memory>", "</memory>"),
    OPERATION_SYSTEM("<os>", "</os>", TagLevel.SIMPLE_FIELD),
    DISC_READER("<disc_reader>", "</disc_reader>", TagLevel.SIMPLE_FIELD);


    fun isComplexField() = level != TagLevel.SIMPLE_FIELD && level != TagLevel.SUB_FIELD
}

enum class TagLevel(val tabsCount: Int, val spacesCount: Int) {
    MAIN(0, 0) {
        override fun appendEndTag(builder: StringBuilder) {
            builder.appendNextLines(2)
        }
    },
    ELEMENT(1, 2) {
        override fun appendEndTag(builder: StringBuilder) {
            builder.appendNextLines(spacesCount)
            builder.appendTabs(tabsCount)
        }
    },
    COMPLEX_FIELD(2, 2) {
        override fun appendEndTag(builder: StringBuilder) {
            builder.appendNextLine()
            builder.appendTabs(tabsCount)
        }
    },
    SIMPLE_FIELD(2, 2) {
        override fun appendTagValue(builder: StringBuilder) {}
    },
    SUB_FIELD(3, 1) {
        override fun appendTagValue(builder: StringBuilder) {}
    };

    fun appendStartTag(builder: StringBuilder) {
        builder.appendNextLines(spacesCount)
        builder.appendTabs(tabsCount)
    }

    open fun appendTagValue(builder: StringBuilder) {
        builder.appendNextLine()
        builder.appendTabs(tabsCount)
    }

    open fun appendEndTag(builder: StringBuilder) {}
}

abstract class TagValue(open val builder: StringBuilder, open val tag: Tag) {

    abstract fun isEmpty(): Boolean

    abstract fun appendValue()

    fun appendTag() {
        if(isEmpty()) return
        builder.appendStartTag(tag)
        appendValue()
        builder.appendEndTag(tag)
    }

}

class SimpleField(override val builder: StringBuilder, override val tag: Tag, val value: String): TagValue(builder, tag) {

    override fun isEmpty() = value.isEmpty()

    override fun appendValue() = builder.appendTagValue(tag, value)

}

class ComplexField(override val builder: StringBuilder, override val tag: Tag, vararg val value: TagValue) : TagValue(builder, tag) {

    override fun isEmpty() = value.all { it.isEmpty() }

    override fun appendValue() = value.forEach { it.appendTag() }

}