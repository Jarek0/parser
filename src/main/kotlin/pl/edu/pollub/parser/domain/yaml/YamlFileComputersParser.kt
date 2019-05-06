package pl.edu.pollub.parser.domain.yaml

import pl.edu.pollub.parser.domain.*

class YamlFileComputersParser: ComputersFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        val tag = TagFactory.getTag(Format.YAML, TagName.COMPUTERS)
        val computersTag = tag.parse(fileContent)
        val computerMatcher = YamlTagMatcher(computersTag, YamlTag.COMPUTER)
        return generateSequence { }
                .takeWhile { computerMatcher.isFound() }
                .map { computerMatcher.getTagValue() }
                .map { it.parseComputer(Format.YAML) }
                .toSet()
    }

    override fun parseFrom(computers: Collection<Computer>): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}