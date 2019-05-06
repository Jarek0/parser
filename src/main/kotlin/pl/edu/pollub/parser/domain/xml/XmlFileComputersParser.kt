package pl.edu.pollub.parser.domain.xml

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.domain.*

@Component
class XmlFileComputersParser: ComputersFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        val tag = TagFactory.getTag(Format.XML, TagName.COMPUTERS)
        val computersTag = tag.parse(fileContent)
        val computerMatcher = XmlTagMatcher(computersTag, XmlTag.COMPUTER)
        return generateSequence { }
                .takeWhile { computerMatcher.isFound() }
                .map { computerMatcher.getTagValue() }
                .map { it.parseComputer(Format.XML) }
                .toSet()
    }

    override fun parseFrom(computers: Collection<Computer>): String {
        return ComputersXmlStructureBuilder(computers).build()
    }

}