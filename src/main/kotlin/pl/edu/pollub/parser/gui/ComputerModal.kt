package pl.edu.pollub.parser.gui

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.AddComputerCommand
import pl.edu.pollub.parser.application.ComputerApi
import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.DEFAULT_VALUE
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.*

typealias ComputerFields = MutableMap<String, JTextField>

@Component
class ComputerModal(mainFrame: MainFrame, private val api: ComputerApi) {

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

    private fun handleAddComputer() {
        val addedComputer = computerFields.toComputer()
        api.add(AddComputerCommand(addedComputer))
        close()
    }

    private fun close() {
        dialog.isVisible = false
        computerFields.forEach { name, field -> field.text = DEFAULT_VALUE }
    }

    fun ComputerFields.toComputer() = Computer(
            getFieldValue(0),
            getFieldValue(1),
            getFieldValue(2),
            getFieldValue(3),
            getFieldValue(4),
            getFieldValue(5),
            getFieldValue(6),
            getFieldValue(7),
            getFieldValue(8),
            getFieldValue(9),
            getFieldValue(10),
            getFieldValue(11),
            getFieldValue(12),
            getFieldValue(13),
            getFieldValue(14)
    )

    private fun ComputerFields.getFieldValue(index: Int) = get(COLUMNS[index])?.text ?: DEFAULT_VALUE
}

const val ADD_COMPUTER_TITLE = "Add computer"
const val EDIT_COMPUTER_TITLE = "Edit computer"
const val OK = "OK"
const val COLUMNS_NUMBER = 2