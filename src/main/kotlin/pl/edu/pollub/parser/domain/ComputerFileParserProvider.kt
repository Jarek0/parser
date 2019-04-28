package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component

@Component
class ComputersParserProvider(
        private val txtFileComputersComputerParser: TxtFileComputerParser,
        private val xmlFileComputersComputerParser: XmlFileComputerParser,
        private val invalidComputerParser: InvalidFileComputerParser
) {

    fun provide(fileType: String): ComputerFileParser {
        return when(fileType) {
            TXT_FORMAT -> txtFileComputersComputerParser
            XML_FORMAT -> xmlFileComputersComputerParser
            else -> invalidComputerParser
        }
    }

}

const val TXT_FORMAT = "txt"
const val XML_FORMAT = "xml"

@Component
class InvalidFileComputerParser: ComputerFileParser {
    override fun parse(fileContent: List<String>): List<Computer> {
        return emptyList()
    }

}