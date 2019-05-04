package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.ComputerConverters.Companion.convert
import pl.edu.pollub.parser.domain.xml.ComplexField
import pl.edu.pollub.parser.domain.xml.SimpleField
import pl.edu.pollub.parser.domain.xml.Tag
import pl.edu.pollub.parser.domain.xml.appendTabs
import java.lang.StringBuilder

@Component
class TxtFileComputerParser: ComputerFileParser {

    override fun parseFrom(fileContent: String): Set<Computer> {
        return fileContent.lines().filter { it.isNotBlank() }.map { parseSingle(it) }.toSet()
    }

    override fun parseFrom(computers: Collection<Computer>): String {
        val builder = StringBuilder()
        for(computer in computers) {
            val parsedComputer = parseSingle((computer))
            when {
                computers.isFirst(computer) -> builder.append(parsedComputer)
                else -> builder.appendNextLine(parsedComputer)
            }
        }
        return builder.toString()
    }

    private fun parseSingle(line: String): Computer {
        val splittedLine = line.split(DELIMITER).toTypedArray()
        return convert(splittedLine)
    }

    private fun parseSingle(c: Computer): String {
        return "${c.manufacturer.value}$DELIMITER" +
                "${c.screen.size}$DELIMITER${c.screen.resolution}$DELIMITER${c.screen.type}$DELIMITER${c.screen.touchscreen}$DELIMITER" +
                "${c.processor.name}$DELIMITER${c.processor.physicalCores}$DELIMITER${c.processor.clockSpeed}$DELIMITER" +
                "${c.ram.value}$DELIMITER" +
                "${c.disc.storage}$DELIMITER${c.disc.type}$DELIMITER" +
                "${c.graphicCard.name}$DELIMITER${c.graphicCard.memory}$DELIMITER" +
                "${c.operationSystem.value}$DELIMITER" +
                "${c.discReader.value}$DELIMITER"
    }

    private fun Collection<Computer>.isFirst(o: Computer): Boolean {
        return indexOf(o) == 1
    }

}

const val DELIMITER = ";"

fun StringBuilder.appendNextLines(count: Int, value: String? = null) {
    (0 until count).fold(this){ _, _ -> append(System.getProperty("line.separator")) }
    if(value != null) append(value)
}

fun StringBuilder.appendNextLine(value: String? = null) {
    append(System.getProperty("line.separator"))
    if(value != null) append(value)
}