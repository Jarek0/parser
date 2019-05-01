package pl.edu.pollub.parser.gui

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.ComputerApi
import pl.edu.pollub.parser.domain.*
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel
import java.awt.Component as GuiComponent
import javax.swing.ListSelectionModel
import javax.swing.DefaultListSelectionModel
import javax.swing.event.ListSelectionEvent


typealias TableRows = Array<Array<String>>
typealias TableRow = Array<String>

@Component
class ComputerTable(private val api: ComputerApi,
                    computersChangedSubject: ComputersChangedSubject,
                    private val computerSelectedSubject: ComputerSelectedSubject
): ComputersChangedObserver {

    private val table: JTable
    val body: JScrollPane

    init {
        val model = DefaultTableModel(0, COLUMNS.size)
        model.setColumnIdentifiers(COLUMNS)
        table = JTable(model)
        table.selectionModel = ForcedListSelectionModel()
        body = JScrollPane(table)
        table.fillsViewportHeight = true
        table.selectionModel.addListSelectionListener { handleElementSelected(it) }
        computersChangedSubject.subscribe(this)
    }

    override fun receive() {
        val newContent = api.getAll()
        val newContentConverted = convert(newContent)
        val model = DefaultTableModel(newContentConverted, COLUMNS)
        table.model = model
    }

    private fun handleElementSelected(event: ListSelectionEvent) {
        val selectedElementId = ComputerId(table.getValueAt(event.firstIndex, 0).toString())
        computerSelectedSubject.notifyObservers(ComputerSelectedEvent(selectedElementId))
    }
}

@Component
class ComputerSelectedSubject {

    private val observers: MutableSet<ComputerSelectedObserver> = mutableSetOf()

    fun subscribe(observer: ComputerSelectedObserver) = observers.add(observer)

    fun unsubscribe(observer: ComputerSelectedObserver) = observers.remove(observer)

    fun notifyObservers(event: ComputerSelectedEvent) {
        for(observer in observers) {
            observer.receive(event)
        }
    }
}

interface ComputerSelectedObserver {

    fun receive(event: ComputerSelectedEvent)

}

data class ComputerSelectedEvent(val id: ComputerId)

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
        "Id", "Manufacturer", "Matrix Size", "Resolution", "Matrix Type", "Touchscreen",
        "Processor", "Cores Count", "Timing", "RAM", "Disc Capacity", "Disc Type",
        "Graphic Card", "Graphic Card Memory", "Operation System", "Optical Drive"
)

fun array2dOf(sizeOuter: Int, sizeInner: Int): Array<Array<String>>
        = Array(sizeOuter) { Array(sizeInner) { DEFAULT_VALUE } }

class ForcedListSelectionModel : DefaultListSelectionModel() {
    init {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }

    override fun clearSelection() {}

    override fun removeSelectionInterval(index0: Int, index1: Int) {}

}
