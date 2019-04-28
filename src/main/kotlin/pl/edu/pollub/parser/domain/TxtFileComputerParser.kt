package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component

@Component
class TxtFileComputerParser: ComputerFileParser {

    override fun parse(fileContent: List<String>): List<Computer> {
        return fileContent.filter { it.isNotBlank() }.map { parse(it) }
    }

    private fun parse(line: String): Computer {
        val splittedLine = line.split(DELIMITER).toList()
        return Computer(
                splittedLine.getOrDefault(0),
                splittedLine.getOrDefault(1),
                splittedLine.getOrDefault(2),
                splittedLine.getOrDefault(3),
                splittedLine.getOrDefault(4),
                splittedLine.getOrDefault(5),
                splittedLine.getOrDefault(6),
                splittedLine.getOrDefault(7),
                splittedLine.getOrDefault(8),
                splittedLine.getOrDefault(9),
                splittedLine.getOrDefault(10),
                splittedLine.getOrDefault(11),
                splittedLine.getOrDefault(12),
                splittedLine.getOrDefault(13),
                splittedLine.getOrDefault(14)
        )
    }

    private fun List<String>.getOrDefault(index: Int): String {
        val value = getOrNull(index)
        return when {
            value.isNullOrEmpty() -> DEFAULT_VALUE
            else -> value
        }
    }

}

const val DELIMITER = ";"