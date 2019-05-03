package pl.edu.pollub.parser.gui

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.*
import pl.edu.pollub.parser.domain.ComputerId
import pl.edu.pollub.parser.domain.DEFAULT_VALUE
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.*

typealias ComputerFields = MutableMap<String, JTextField>

@Component
class ComputerModal(private val api: ComputerApi, mainFrame: MainFrame) {

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

    fun addComputer(computerId: ComputerId?) {
        dialog.title = ADD_COMPUTER_TITLE
        okButton.removeAllActionListeners()
        okButton.addActionListener { handleAddComputer(computerId) }
        dialog.isVisible = true
    }

    fun editComputer(computerToEditId: ComputerId) {
        dialog.title = EDIT_COMPUTER_TITLE
        val computerToEdit = api.get(GetComputerQuery(computerToEditId))
        computerFields.getValuesFromComputerData(computerToEdit)
        okButton.removeAllActionListeners()
        okButton.addActionListener { handleEditComputer(computerToEditId) }
        dialog.isVisible = true
    }

    private fun JButton.removeAllActionListeners() {
        actionListeners.forEach { removeActionListener(it) }
    }

    private fun handleAddComputer(computerId: ComputerId?) {
        val addedComputer = computerFields.toComputerData()
        when {
            computerId == null -> api.add(AddComputerCommand(addedComputer))
            else -> api.add(AddComputerAfterIdCommand(addedComputer, computerId))
        }
        close()
    }

    private fun handleEditComputer(editedComputerId: ComputerId) {
        val editedComputerData = computerFields.toComputerData()
        api.edit(EditComputerCommand( editedComputerId, editedComputerData))
        close()
    }

    private fun close() {
        dialog.isVisible = false
        computerFields.forEach { _, field -> field.text = DEFAULT_VALUE }
    }

    private fun ComputerFields.toComputerData() = (1 until COLUMNS.size).map { getFieldValue(it) }.toTypedArray()

    private fun ComputerFields.getValuesFromComputerData(computerData: ComputerTableRow) =
            (1 until COLUMNS.size).forEach { setFieldValue(it, computerData[it]) }

    private fun ComputerFields.getFieldValue(index: Int) = get(COLUMNS[index])?.text ?: DEFAULT_VALUE

    private fun ComputerFields.setFieldValue(index: Int, value: String) {
        val textField = get(COLUMNS[index]) ?: return
        textField.text = value
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
}

const val ADD_COMPUTER_TITLE = "Add computer"
const val EDIT_COMPUTER_TITLE = "Edit computer"
const val OK = "OK"
const val COLUMNS_NUMBER = 2