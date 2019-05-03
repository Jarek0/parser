package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.convert
import java.lang.StringBuilder

@Component
class TxtFileComputerParser: ComputerFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        return fileContent.lines().filter { it.isNotBlank() }.map { parseSingle(it) }.toSet()
    }

    private fun parseSingle(line: String): Computer {
        val splittedLine = line.split(DELIMITER).toTypedArray()
        return convert(splittedLine)
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