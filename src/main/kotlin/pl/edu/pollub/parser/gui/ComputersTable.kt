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

@Component
class ComputersTable(private val api: ComputerApi,
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
        table.selectionModel.addListSelectionListener { handleElementSelected() }
        computersChangedSubject.subscribe(this)
    }

    override fun receive() {
        val newContent = api.getAll()
        val model = DefaultTableModel(newContent, COLUMNS)
        table.model = model
        handleElementSelected()
    }

    private fun handleElementSelected() {
        if(table.selectedRow < 0 || table.selectedRow >= table.rowCount) return
        val selectedElementId = ComputerId(table.getValueAt(table.selectedRow, 0).toString())
        computerSelectedSubject.notifyObservers(ComputerSelectedEvent(selectedElementId))
    }
}

val COLUMNS = arrayOf(
        "Id",
        "Manufacturer",
        "Screen Size", "Screen Resolution", "Screen Type", "Screen Touchscreen",
        "Processor Name", "Processor Physical cores", "Processor Clock Speed",
        "RAM",
        "Disc Storage", "Disc Type",
        "Graphic Card Name", "Graphic Card Memory",
        "Operation System",
        "Optical Drive"
)

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

data class ComputerSelectedEvent(val id: ComputerId? = null)

class ForcedListSelectionModel : DefaultListSelectionModel() {
    init {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
    }

    override fun clearSelection() {}

    override fun removeSelectionInterval(index0: Int, index1: Int) {}

}
