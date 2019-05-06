package pl.edu.pollub.parser.domain

import pl.edu.pollub.parser.domain.xml.XmlTag
import pl.edu.pollub.parser.domain.yaml.YamlTag
import java.lang.IllegalArgumentException

object TagFactory {

    fun getTag(format: Format, tagName: TagName): Tag {
        val tags: List<Tag> = when(format) {
            Format.XML -> XmlTag.values().toList()
            Format.YAML -> YamlTag.values().toList()
            else -> throw IllegalArgumentException("Illegal format: $format")
        }
        return tags.first { it.getName() == tagName.name }
    }

}

enum class TagName {
    COMPUTERS,
    COMPUTER,
    MANUFACTURER,
    SCREEN,
    SIZE,
    RESOLUTION,
    TYPE,
    TOUCHSCREEN,
    PROCESSOR,
    NAME,
    PHYSICAL_CORES,
    CLOCK_SPEED,
    RAM,
    DISC,
    STORAGE,
    GRAPHIC_CARD,
    MEMORY,
    OPERATION_SYSTEM,
    DISC_READER;
}

interface Tag {

    fun getName(): String

    fun parse(text: String): String

}