package pl.edu.pollub.parser.domain.yaml

import pl.edu.pollub.parser.domain.TagMatcher
import pl.edu.pollub.parser.domain.appendNextLine
import pl.edu.pollub.parser.domain.isNotFount
import java.lang.StringBuilder
import java.util.regex.Pattern

class YamlTagMatcher(private val text: String, private val tag: YamlTag): TagMatcher{

    private val bodyTagMatcher = Pattern.compile(tag.body).matcher(text)

    override fun isFound(): Boolean = bodyTagMatcher.find()

    override fun isNotFound(): Boolean = bodyTagMatcher.isNotFount()

    override fun getTagValue(): String {
        val substringAfterTagExclusive = text.substring(bodyTagMatcher.end())
        val substringTagExclusive = substringAfterTagExclusive.substringToNextTag(tag.level)
        return substringTagExclusive.formatYamlValue()
    }

    private fun String.formatYamlValue(): String {
        return tag.level.normalize(this)
    }

    private fun String.substringToNextTag(tagLevel: YamlTagLevel): String {
        val lines = this.lines().toMutableList()
        val resultBuilder = StringBuilder()
        val firstLine = lines.first()
        resultBuilder.append(firstLine)
        lines.remove(firstLine)
        for (line in lines) {
            if(line.startsWithTagWithSameHigherLevel(tagLevel)) break
            resultBuilder.appendNextLine(line)
        }
        return resultBuilder.toString()
    }

    private fun String.startsWithTagWithSameHigherLevel(tagLevel: YamlTagLevel): Boolean {
        val tagsWithHigherOrEqualLevel = YamlTag.values().filter { it.level.isHigherThanOrEqualTo(tagLevel) }
        val tagsBodiesWithHigherOrEqualLevel = tagsWithHigherOrEqualLevel.map { it.body }
        return tagsBodiesWithHigherOrEqualLevel.any { this.trimStart().startsWith(it) }
    }
}