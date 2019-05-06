package pl.edu.pollub.parser.domain

import pl.edu.pollub.parser.domain.xml.XmlTag
import java.lang.StringBuilder

fun String.parseComputer(format: Format): Computer {
    return Computer(
            this.parseManufacturer(format),
            this.parseScreen(format),
            this.parseProcessor(format),
            this.parseRam(format),
            this.parseDisc(format),
            this.parseGraphicCard(format),
            this.parseOperationSystem(format),
            this.parseDiscReader(format)
    )
}

private fun String.parseManufacturer(format: Format): Manufacturer {
    val tag = TagFactory.getTag(format, TagName.MANUFACTURER)
    val manufacturerTagValue = tag.parse(this)
    return Manufacturer(manufacturerTagValue)
}

private fun String.parseScreen(format: Format): Screen {
    val tag = TagFactory.getTag(format, TagName.SCREEN)
    val screenTagValue = tag.parse(this)
    return Screen(
            screenTagValue.parseSimpleValue(format, TagName.SIZE),
            screenTagValue.parseSimpleValue(format, TagName.RESOLUTION),
            screenTagValue.parseSimpleValue(format, TagName.TYPE),
            screenTagValue.parseSimpleValue(format, TagName.TOUCHSCREEN)
    )
}

private fun String.parseProcessor(format: Format): Processor {
    val tag = TagFactory.getTag(format, TagName.PROCESSOR)
    val processorTagValue = tag.parse(this)
    return Processor(
            processorTagValue.parseSimpleValue(format, TagName.NAME),
            processorTagValue.parseSimpleValue(format, TagName.PHYSICAL_CORES),
            processorTagValue.parseSimpleValue(format, TagName.CLOCK_SPEED)
    )
}

private fun String.parseRam(format: Format): Ram {
    val tag = TagFactory.getTag(format, TagName.RAM)
    val ramTagValue = tag.parse(this)
    return Ram(ramTagValue)
}

private fun String.parseDisc(format: Format): Disc {
    val tag = TagFactory.getTag(format, TagName.DISC)
    val discTagValue = tag.parse(this)
    return Disc(
            discTagValue.parseSimpleValue(format, TagName.STORAGE),
            discTagValue.parseSimpleValue(format, TagName.TYPE)
    )
}

private fun String.parseGraphicCard(format: Format): GraphicCard {
    val tag = TagFactory.getTag(format, TagName.GRAPHIC_CARD)
    val graphicCardTag = tag.parse(this)
    return GraphicCard(
            graphicCardTag.parseSimpleValue(format, TagName.NAME),
            graphicCardTag.parseSimpleValue(format, TagName.MEMORY)
    )
}

private fun String.parseOperationSystem(format: Format): OperationSystem {
    val tag = TagFactory.getTag(format, TagName.OPERATION_SYSTEM)
    val operationSystemTag = tag.parse(this)
    return OperationSystem(operationSystemTag)
}

private fun String.parseDiscReader(format: Format): DiscReader {
    val tag = TagFactory.getTag(format, TagName.DISC_READER)
    val discReaderTag = tag.parse(this)
    return DiscReader(discReaderTag)
}

private fun String.parseSimpleValue(format: Format, tagName: TagName): String {
    val tag = TagFactory.getTag(format, tagName)
    return tag.parse(this)
}

fun StringBuilder.appendNextLines(count: Int, value: String? = null) {
    (0 until count).fold(this){ _, _ -> append(System.getProperty("line.separator")) }
    if(value != null) append(value)
}

fun StringBuilder.appendNextLine(value: String? = null) {
    append(getLineSeparator())
    if(value != null) append(value)
}

fun StringBuilder.appendStartTag(xmlTag: XmlTag) {
    xmlTag.levelXml.appendStartTag(this)
    append(xmlTag.start)
}

fun StringBuilder.appendTagValue(xmlTag: XmlTag, value: String) {
    xmlTag.levelXml.appendTagValue(this)
    append(value)
}

fun StringBuilder.appendEndTag(xmlTag: XmlTag) {
    xmlTag.levelXml.appendEndTag(this)
    append(xmlTag.end)
}

fun StringBuilder.appendTabs(count: Int) {
    (0 until count).fold(this){ _, _ -> append("\t") }
}

fun getLineSeparator(): String {
    return System.getProperty("line.separator")
}