package pl.edu.pollub.parser.domain.xml

import pl.edu.pollub.parser.domain.Tag
import pl.edu.pollub.parser.domain.appendNextLine
import pl.edu.pollub.parser.domain.appendNextLines
import pl.edu.pollub.parser.domain.appendTabs
import java.lang.StringBuilder


enum class XmlTag(val start: String, val end: String, val levelXml: XmlTagLevel = XmlTagLevel.SUB_FIELD): Tag {
    COMPUTERS("<laptops>", "</laptops>", XmlTagLevel.MAIN),
    COMPUTER("<laptop>", "</laptop>", XmlTagLevel.ELEMENT),
    MANUFACTURER("<manufacturer>", "</manufacturer>", XmlTagLevel.SIMPLE_FIELD),
    SCREEN("<screen>", "</screen>", XmlTagLevel.COMPLEX_FIELD),
    SIZE("<size>", "</size>"),
    RESOLUTION("<resolution>", "</resolution>"),
    TYPE("<type>", "</type>"),
    TOUCHSCREEN("<touchscreen>", "</touchscreen>"),
    PROCESSOR("<processor>", "</processor>", XmlTagLevel.COMPLEX_FIELD),
    NAME("<name>", "</name>"),
    PHYSICAL_CORES("<physical_cores>", "</physical_cores>"),
    CLOCK_SPEED("<clock_speed>", "</clock_speed>"),
    RAM("<ram>", "</ram>", XmlTagLevel.SIMPLE_FIELD),
    DISC("<disc>", "</disc>", XmlTagLevel.COMPLEX_FIELD),
    STORAGE("<storage>", "</storage>"),
    GRAPHIC_CARD("<graphic_card>", "</graphic_card>", XmlTagLevel.COMPLEX_FIELD),
    MEMORY("<memory>", "</memory>"),
    OPERATION_SYSTEM("<os>", "</os>", XmlTagLevel.SIMPLE_FIELD),
    DISC_READER("<disc_reader>", "</disc_reader>", XmlTagLevel.SIMPLE_FIELD);

    override fun getName() = name

    override fun parse(text: String): String {
        val tagMatcher = XmlTagMatcher(text, this)
        if(tagMatcher.isNotFound()) return ""
        return tagMatcher.getTagValue()
    }
}

enum class XmlTagLevel(val tabsCount: Int, val verticalGapsCount: Int) {
    MAIN(0, 0) {
        override fun appendEndTag(builder: StringBuilder) {
            builder.appendNextLines(2)
        }
    },
    ELEMENT(1, 2) {
        override fun appendEndTag(builder: StringBuilder) {
            builder.appendNextLines(verticalGapsCount)
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
        builder.appendNextLines(verticalGapsCount)
        builder.appendTabs(tabsCount)
    }

    open fun appendTagValue(builder: StringBuilder) {
        builder.appendNextLine()
        builder.appendTabs(tabsCount)
    }

    open fun appendEndTag(builder: StringBuilder) {}
}