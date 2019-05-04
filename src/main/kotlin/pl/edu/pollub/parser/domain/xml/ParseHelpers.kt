package pl.edu.pollub.parser.domain.xml

import pl.edu.pollub.parser.domain.*
import pl.edu.pollub.parser.domain.csv.appendNextLine
import pl.edu.pollub.parser.domain.csv.appendNextLines
import java.lang.StringBuilder
import java.util.regex.Matcher
import java.util.regex.Pattern

private fun String.parseTag(tag: Tag): String {
    val computerMatcher = TagMatcher(this, tag)
    if(computerMatcher.isNotFound()) return ""
    return computerMatcher.substringExclusive()
}

fun String.parseComputers(): Set<Computer> {
    val computersTag = parseTag(Tag.COMPUTERS)
    val computerMatcher = TagMatcher(computersTag, Tag.COMPUTER)
    return generateSequence { }
            .takeWhile { computerMatcher.isFound() }
            .map { computerMatcher.substringInclusive() }
            .map { it.parseComputer() }
            .toSet()
}

fun String.parseComputer(): Computer {
    val computerTag = parseTag(Tag.COMPUTER)
    return Computer(
            computerTag.parseManufacturer(),
            computerTag.parseScreen(),
            computerTag.parseProcessor(),
            computerTag.parseRam(),
            computerTag.parseDisc(),
            computerTag.parseGraphicCard(),
            computerTag.parseOperationSystem(),
            computerTag.parseDiscReader()
    )
}

private fun String.parseManufacturer() = Manufacturer(parseTag(Tag.MANUFACTURER))

private fun String.parseScreen(): Screen {
    val screenTag = parseTag(Tag.SCREEN)
    return Screen(
            screenTag.parseSize(),
            screenTag.parseResolution(),
            screenTag.parseType(),
            screenTag.parseTouchscreen()
    )
}

private fun String.parseSize() = parseTag(Tag.SIZE)

private fun String.parseResolution() = parseTag(Tag.RESOLUTION)

private fun String.parseType() = parseTag(Tag.TYPE)

private fun String.parseTouchscreen() = parseTag(Tag.TOUCHSCREEN)

private fun String.parseProcessor(): Processor {
    val processorTag = parseTag(Tag.PROCESSOR)
    return Processor(
            processorTag.parseName(),
            processorTag.parsePhysicalCores(),
            processorTag.parseClockSpeed()
    )
}

private fun String.parseName() = parseTag(Tag.NAME)

private fun String.parsePhysicalCores() = parseTag(Tag.PHYSICAL_CORES)

private fun String.parseClockSpeed() = parseTag(Tag.CLOCK_SPEED)

private fun String.parseRam() = Ram(parseTag(Tag.RAM))

private fun String.parseDisc(): Disc {
    val discTag = parseTag(Tag.DISC)
    return Disc(
            discTag.parseStorage(),
            discTag.parseType()
    )
}

private fun String.parseStorage() = parseTag(Tag.STORAGE)

private fun String.parseGraphicCard(): GraphicCard {
    val graphicCardTag = parseTag(Tag.GRAPHIC_CARD)
    return GraphicCard(
            graphicCardTag.parseName(),
            graphicCardTag.parseMemory()
    )
}

private fun String.parseMemory() = parseTag(Tag.MEMORY)

private fun String.parseOperationSystem() = OperationSystem(parseTag(Tag.OPERATION_SYSTEM))

private fun String.parseDiscReader() = DiscReader(parseTag(Tag.DISC_READER))

fun StringBuilder.appendStartTag(tag: Tag) {
    tag.level.appendStartTag(this)
    append(tag.start)
}

fun StringBuilder.appendTagValue(tag: Tag, value: String) {
    tag.level.appendTagValue(this)
    append(value)
}

fun StringBuilder.appendEndTag(tag: Tag) {
    tag.level.appendEndTag(this)
    append(tag.end)
}

fun StringBuilder.appendTabs(count: Int) {
    (0 until count).fold(this){ _, _ -> append("\t") }
}

class TagMatcher(private val string: String, tag: Tag) {

    private val startTagMatcher = Pattern.compile(tag.start).matcher(string)
    private val endTagMatcher = Pattern.compile(tag.end).matcher(string)

    fun isFound() = startTagMatcher.find() && endTagMatcher.find()

    fun isNotFound() = startTagMatcher.isNotFount() || endTagMatcher.isNotFount()

    fun substringInclusive() = string.substring(startTagMatcher.start(), endTagMatcher.end())

    fun substringExclusive() = string.substring(startTagMatcher.end(), endTagMatcher.start())

}

fun Matcher.isNotFount() = !find()
