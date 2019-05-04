package pl.edu.pollub.parser.domain.xml

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.domain.*

@Component
class XmlFileComputerParser: ComputerFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        return fileContent.parseComputers()
    }

    override fun parseFrom(computers: Collection<Computer>): String {
        return ComputersXmlStructureBuilder(computers).build()
    }

}