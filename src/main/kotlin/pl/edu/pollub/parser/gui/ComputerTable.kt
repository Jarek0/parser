package pl.edu.pollub.parser.gui

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.ComputerApi
import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.ComputersChangedObserver
import pl.edu.pollub.parser.domain.ComputersChangedSubject
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel
import java.awt.Component as GuiComponent

typealias TableRows = Array<Array<String>>
typealias TableRow = Array<String>

@Component
class ComputerTable(val api: ComputerApi, subject: ComputersChangedSubject): ComputersChangedObserver {

    private val table: JTable
    val body: JScrollPane

    init {
        val model = DefaultTableModel(0, COLUMNS.size)
        model.setColumnIdentifiers(COLUMNS)
        table = JTable(model)
        body = JScrollPane(table)
        table.fillsViewportHeight = true
        subject.subscribe(this)
    }

    override fun receive() {
        val newContent = api.getAll()
        val newContentConverted = convert(newContent)
        val model = DefaultTableModel(newContentConverted, COLUMNS)
        table.model = model
    }
}

fun convert(computers: Collection<Computer>): TableRows {
    return computers.fold(array2dOf(computers.size, COLUMNS.size))
    { tableRows, computer ->
        tableRows[computers.indexOf(computer)] = convert(computer)
        tableRows
    }
}

fun convert(computer: Computer): TableRow {
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

val COLUMNS = arrayOf(
        "Id",
        "Manufacturer",
        "Matrix\nSize", "Resolution", "Matrix\nType", "Touchscreen",
        "Processor", "Cores\nCount", "Timing", "RAM", "Disc\nCapacity", "Disc\nType",
        "Graphic\nCard", "Graphic\nCard\nMemory", "Operation\nSystem", "Optical\nDrive"
)

fun array2dOf(sizeOuter: Int, sizeInner: Int): Array<Array<String>>
        = Array(sizeOuter) { Array(sizeInner) { "" } }
