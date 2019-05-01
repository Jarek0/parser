package pl.edu.pollub.parser.gui

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.AddComputerCommand
import pl.edu.pollub.parser.application.ComputerApi
import pl.edu.pollub.parser.application.GetComputerQuery
import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.ComputerId
import pl.edu.pollub.parser.domain.ComputersChangedSubject
import pl.edu.pollub.parser.domain.DEFAULT_VALUE
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.*

typealias ComputerFields = MutableMap<String, JTextField>

@Component
class ComputerModal(mainFrame: MainFrame, private val api: ComputerApi, private val computersChangedSubject: ComputersChangedSubject) {

    private val dialog = JDialog(mainFrame.body, ADD_COMPUTER_TITLE, true)
    private val panel = JPanel()
    private val subPanel = JPanel()
    private val okButton = JButton(OK)
    private val computerFields: ComputerFields = mutableMapOf()

    init {
        with(panel) {
            layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
            subPanel.layout = GridLayout(COLUMNS.size, COLUMNS_NUMBER)
            COLUMNS.filterNot { it == COLUMNS[0] }.forEach { addTextInput(it) }
            add(subPanel)
            add(createOkPanel())
        }

        with(dialog) {
            contentPane.add(panel)
            pack()
            isVisible = false
        }
    }

    private fun createOkPanel(): JPanel {
        val panel = JPanel()
        val layout = FlowLayout()
        panel.layout = layout
        panel.add(okButton)
        return panel
    }

    private fun addTextInput(name: String) {

        val label = JLabel("$name:")
        val field = JTextField()

        subPanel.add(label)
        subPanel.add(field)
        computerFields[name] = field
    }

    fun addComputer() {
        dialog.title = ADD_COMPUTER_TITLE
        okButton.addActionListener { handleAddComputer() }
        dialog.isVisible = true
    }

    fun editComputer(computerToEditId: ComputerId) {
        dialog.title = EDIT_COMPUTER_TITLE
        val computerToEdit = api.get(GetComputerQuery(computerToEditId))
        computerFields.fromComputer(computerToEdit)
        okButton.addActionListener { handleEditComputer(computerToEdit) }
        dialog.isVisible = true
    }

    private fun handleAddComputer() {
        val addedComputer = computerFields.toComputer()
        api.add(AddComputerCommand(addedComputer))
        close()
    }

    private fun handleEditComputer(editedComputer: Computer) {
        computerFields.toComputer(editedComputer)
        computersChangedSubject.notifyObservers()
        close()
    }

    private fun close() {
        dialog.isVisible = false
        computerFields.forEach { _, field -> field.text = DEFAULT_VALUE }
    }

    private fun ComputerFields.toComputer(computer: Computer = Computer()): Computer {
        val fieldValues = (1..15).map { getFieldValue(it) }.toTypedArray()
        return convert(fieldValues, computer)
    }

    private fun ComputerFields.fromComputer(computer: Computer) {
        val computerArray = convert(computer)
        (1..15).forEach { setFieldValue(it, computerArray[it]) }
    }

    private fun ComputerFields.getFieldValue(index: Int) = get(COLUMNS[index])?.text ?: DEFAULT_VALUE

    private fun ComputerFields.setFieldValue(index: Int, value: String) {
        val textField = get(COLUMNS[index]) ?: return
        textField.text = value
    }
}

const val ADD_COMPUTER_TITLE = "Add computer"
const val EDIT_COMPUTER_TITLE = "Edit computer"
const val OK = "OK"
const val COLUMNS_NUMBER = 2