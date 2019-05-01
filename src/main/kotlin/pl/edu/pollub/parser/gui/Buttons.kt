package pl.edu.pollub.parser.gui

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.ComputerApi
import pl.edu.pollub.parser.application.ExportFileCommand
import pl.edu.pollub.parser.application.ImportFileCommand
import pl.edu.pollub.parser.application.RemoveComputerCommand
import pl.edu.pollub.parser.domain.ComputerId
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JPanel
import java.awt.FlowLayout
import javax.swing.JFileChooser

@Component
class ComputerButtonsPanel(
        importFileButton: ImportFileButton,
        exportFileButton: ExportFileButton,
        editComputerButton: EditComputerButton
) {

    val body = JPanel(FlowLayout(FlowLayout.CENTER))

    init {
        body.add(importFileButton.body)
        body.add(exportFileButton.body)
        body.add(editComputerButton.body)
    }
}

@Component
class ImportFileButton(private val api: ComputerApi, private val fileChooser: FileChooser) {

    val body: JButton = JButton(IMPORT_BUTTON_TEXT)

    init {
        body.preferredSize = Dimension(150, 30)
        body.addActionListener { handleFile() }
    }

    private fun handleFile() {
        val chosenOption = fileChooser.showOpenDialog()
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.getSelectedFile()
            api.import(ImportFileCommand(file))
        }
    }
}

@Component
class ExportFileButton(private val api: ComputerApi, private val fileChooser: FileChooser) {

    val body: JButton = JButton(EXPORT_BUTTON_TEXT)

    init {
        body.preferredSize = Dimension(150, 30)
        body.addActionListener { handleFileExport() }
    }

    private fun handleFileExport() {
        val chosenOption = fileChooser.showSaveDialog()
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.getSelectedFile()
            api.export(ExportFileCommand(file))
        }
    }
}

@Component
class AddComputerButton(private val computerModal: ComputerModal) {

    val body: JButton = JButton(ADD_BUTTON_TEST)

    init {
        body.preferredSize = Dimension(150, 30)
        body.addActionListener { handleAddComputer() }
    }

    private fun handleAddComputer() {
        computerModal.addComputer()
    }
}

@Component
class EditComputerButton(private val computerModal: ComputerModal, subject: ComputerSelectedSubject): ComputerSelectedObserver {

    val body: JButton = JButton(EDIT_BUTTON_TEST)
    var computerToEditId: ComputerId? = null

    init {
        body.preferredSize = Dimension(150, 30)
        subject.subscribe(this)
        body.addActionListener { handleSelectedComputerToEdit() }
    }

    override fun receive(event: ComputerSelectedEvent) {
        computerToEditId = event.id
    }

    private fun handleSelectedComputerToEdit() {
        val computerToEditId = computerToEditId ?: return
        computerModal.editComputer(computerToEditId)
    }

}

@Component
class RemoveComputerButton(private val api: ComputerApi, subject: ComputerSelectedSubject): ComputerSelectedObserver {

    val body: JButton = JButton(REMOVE_BUTTON_TEST)
    var computerToRemoveId: ComputerId? = null

    init {
        body.preferredSize = Dimension(150, 30)
        subject.subscribe(this)
        body.addActionListener { handleSelectedComputerToRemove() }
    }

    override fun receive(event: ComputerSelectedEvent) {
        computerToRemoveId = event.id
    }

    private fun handleSelectedComputerToRemove() {
        val computerToRemoveId = computerToRemoveId ?: return
        api.remove(RemoveComputerCommand(computerToRemoveId))
    }
}

@Component
class FileChooser(private val mainFrame: MainFrame) {

    fun showOpenDialog() = fileChooser.showOpenDialog(mainFrame.body)

    fun showSaveDialog() = fileChooser.showSaveDialog(mainFrame.body)

    fun getSelectedFile() = fileChooser.selectedFile

    private val fileChooser = JFileChooser()

}

const val IMPORT_BUTTON_TEXT = "Import"
const val EXPORT_BUTTON_TEXT = "Export"
const val ADD_BUTTON_TEST = "Add"
const val EDIT_BUTTON_TEST = "Edit"
const val REMOVE_BUTTON_TEST = "Remove"