package pl.edu.pollub.parser.application

import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.DEFAULT_VALUE
import pl.edu.pollub.parser.gui.COLUMNS

typealias ComputerTableRows = Array<Array<String>>
typealias ComputerTableRow = Array<String>

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
            computer.manufacturer,
            computer.matrixSize,
            computer.resolution,
            computer.matrixType,
            computer.touchscreen,
            computer.processor,
            computer.coresCount,
            computer.timing,
            computer.ram,
            computer.discCapacity,
            computer.discType,
            computer.graphicCard,
            computer.graphicCardMemory,
            computer.operationSystem,
            computer.opticalDrive
    )
}

fun convert(row: ComputerTableRow, computer: Computer = Computer()): Computer {
    computer.manufacturer = row.getOrDefault(0)
    computer.matrixSize = row.getOrDefault(1)
    computer.resolution = row.getOrDefault(2)
    computer.matrixType = row.getOrDefault(3)
    computer.touchscreen = row.getOrDefault(4)
    computer.processor = row.getOrDefault(5)
    computer.coresCount = row.getOrDefault(6)
    computer.timing = row.getOrDefault(7)
    computer.ram = row.getOrDefault(8)
    computer.discCapacity = row.getOrDefault(9)
    computer.discType = row.getOrDefault(10)
    computer.graphicCard = row.getOrDefault(11)
    computer.graphicCardMemory = row.getOrDefault(12)
    computer.operationSystem = row.getOrDefault(13)
    computer.opticalDrive = row.getOrDefault(14)
    return computer
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
