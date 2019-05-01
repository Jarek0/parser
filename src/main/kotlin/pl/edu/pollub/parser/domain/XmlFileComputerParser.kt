package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component

@Component
class XmlFileComputerParser: ComputerFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun parseFrom(computers: Collection<Computer>): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}