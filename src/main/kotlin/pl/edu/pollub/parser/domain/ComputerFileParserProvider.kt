package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.domain.csv.CsvFileComputersParser
import pl.edu.pollub.parser.domain.xml.XmlFileComputersParser
import pl.edu.pollub.parser.domain.yaml.YamlFileComputersParser

@Component
class ComputersParserProvider(
        private val csvFileComputersParser: CsvFileComputersParser,
        private val xmlFileComputersParser: XmlFileComputersParser,
        private val yamlFileComputersParser: YamlFileComputersParser,
        private val invalidComputerParser: InvalidFileComputersParser
) {

    fun provide(fileType: String): ComputersFileParser {
        return when(fileType) {
            Format.CSV.value -> csvFileComputersParser
            Format.XML.value -> xmlFileComputersParser
            Format.YAML.value -> yamlFileComputersParser
            else -> invalidComputerParser
        }
    }

}

enum class Format(val value: String) {
    CSV("csv"),
    XML("xml"),
    YAML("yaml")
}

@Component
class InvalidFileComputersParser: ComputersFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        return emptySet()
    }

    override fun parseFrom(computers: Collection<Computer>): String {
        return ""
    }

}