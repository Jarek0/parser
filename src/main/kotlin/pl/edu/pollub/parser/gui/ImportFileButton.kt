package pl.edu.pollub.parser.gui

import pl.edu.pollub.parser.application.ComputerApi
import pl.edu.pollub.parser.application.ImportFileCommand
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JFileChooser

class ImportFileButton(private val mainFrame: MainFrame, private val api: ComputerApi) {

    private val fileChooser = JFileChooser()
    val body: JButton = JButton(IMPORT_BUTTON_TEXT)

    init {
        body.preferredSize = Dimension(150, 30)
        body.addActionListener { handleFile() }
    }

    private fun handleFile() {
        val chosenOption = fileChooser.showOpenDialog(mainFrame.body)
        if (chosenOption == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            api.import(ImportFileCommand(file))
        }
    }
}

const val IMPORT_BUTTON_TEXT = "Import"