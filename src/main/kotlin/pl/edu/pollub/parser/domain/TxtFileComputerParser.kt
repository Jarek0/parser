package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component
import java.lang.StringBuilder

@Component
class TxtFileComputerParser: ComputerFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        return fileContent.lines().filter { it.isNotBlank() }.map { parseSingle(it) }.toSet()
    }

    private fun parseSingle(line: String): Computer {
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

    override fun parseFrom(computers: Collection<Computer>): String {
        val builder = StringBuilder()
        for(computer in computers) {
            val parsedComputer = parseSingle((computer))
            builder.append(parsedComputer)
            if(computers.isNotLast(computer)) builder.append(System.getProperty("line.separator"))
        }
        return builder.toString()
    }

    private fun parseSingle(c: Computer): String {
        return "${c.manufacturer}$DELIMITER${c.matrixSize}$DELIMITER${c.resolution}$DELIMITER" +
                "${c.matrixType}$DELIMITER${c.touchscreen}$DELIMITER${c.processor}$DELIMITER" +
                "${c.coresCount}$DELIMITER${c.timing}$DELIMITER${c.ram}$DELIMITER" +
                "${c.discCapacity}$DELIMITER${c.discType}$DELIMITER${c.graphicCard}$DELIMITER" +
                "${c.graphicCardMemory}$DELIMITER${c.operationSystem}$DELIMITER${c.opticalDrive}$DELIMITER"
    }

    private fun Collection<Computer>.isNotLast(o: Computer): Boolean {
        return indexOf(o) < size - 1
    }

}

const val DELIMITER = ";"