package pl.edu.pollub.parser.domain.xml

import pl.edu.pollub.parser.domain.TagMatcher
import pl.edu.pollub.parser.domain.isNotFount
import java.util.regex.Pattern

class XmlTagMatcher(private val text: String, xmlTag: XmlTag): TagMatcher {

    private val startTagMatcher = Pattern.compile(xmlTag.start).matcher(text)
    private val endTagMatcher = Pattern.compile(xmlTag.end).matcher(text)

    override fun isFound() = startTagMatcher.find() && endTagMatcher.find()

    override fun isNotFound() = startTagMatcher.isNotFount() || endTagMatcher.isNotFount()

    override fun getTagValue() = text.substring(startTagMatcher.end(), endTagMatcher.start())

}
