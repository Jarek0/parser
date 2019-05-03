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
            Format.TXT.value -> txtFileComputersComputerParser
            Format.XML.value -> xmlFileComputersComputerParser
            else -> invalidComputerParser
        }
    }

}

enum class Format(val value: String) {
    TXT("txt"),
    XML("xml")
}

@Component
class InvalidFileComputerParser: ComputerFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        return emptySet()
    }

    override fun parseFrom(computers: Collection<Computer>): String {
        return ""
    }

}