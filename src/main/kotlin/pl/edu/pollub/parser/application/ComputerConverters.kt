package pl.edu.pollub.parser.application

import pl.edu.pollub.parser.domain.*
import pl.edu.pollub.parser.gui.COLUMNS

typealias ComputerTableRows = Array<Array<String>>
typealias ComputerTableRow = Array<String>

class ComputerConverters {
    companion object {
        fun convert(computers: Collection<Computer>): ComputerTableRows {
            return computers.fold(array2dOf(computers.size, COLUMNS.size))
            { tableRows, computer ->
                tableRows[computers.indexOf(computer)] = convert(computer)
                tableRows
            }
        }

        fun convert(computer: Computer): ComputerTableRow {
            return arrayOf(
                    computer.id.value,
                    computer.manufacturer.value,
                    computer.screen.size,
                    computer.screen.resolution,
                    computer.screen.type,
                    computer.screen.touchscreen,
                    computer.processor.name,
                    computer.processor.physicalCores,
                    computer.processor.clockSpeed,
                    computer.ram.value,
                    computer.disc.storage,
                    computer.disc.type,
                    computer.graphicCard.name,
                    computer.graphicCard.memory,
                    computer.operationSystem.value,
                    computer.discReader.value
            )
        }

        fun convert(row: ComputerTableRow, computer: Computer = Computer()): Computer {
            computer.manufacturer = Manufacturer(row.getOrDefault(0))
            computer.screen = Screen(
                    row.getOrDefault(1),
                    row.getOrDefault(2),
                    row.getOrDefault(3),
                    row.getOrDefault(4)
            )
            computer.processor = Processor(
                    row.getOrDefault(5),
                    row.getOrDefault(6),
                    row.getOrDefault(7)
            )
            computer.ram = Ram(row.getOrDefault(8))
            computer.disc = Disc(row.getOrDefault(9), row.getOrDefault(10))
            computer.graphicCard = GraphicCard(row.getOrDefault(11), row.getOrDefault(12))
            computer.operationSystem = OperationSystem(row.getOrDefault(13))
            computer.discReader = DiscReader(row.getOrDefault(14))
            return computer
        }
    }
}


private fun ComputerTableRow.getOrDefault(index: Int): String {
    val value = getOrNull(index)
    return when {
        value.isNullOrEmpty() -> DEFAULT_VALUE
        else -> value
    }
}

fun array2dOf(sizeOuter: Int, sizeInner: Int): Array<Array<String>>
        = Array(sizeOuter) { Array(sizeInner) { DEFAULT_VALUE } }
