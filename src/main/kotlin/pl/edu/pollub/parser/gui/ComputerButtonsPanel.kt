package pl.edu.pollub.parser.gui

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.ComputerApi
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JPanel
import java.awt.FlowLayout

@Component
class ComputerButtonsPanel(importFileButton: ImportFileButton) {

    val body = JPanel(FlowLayout(FlowLayout.CENTER))

    init {
        body.add(importFileButton.body)
    }
}

@Component
class ExportFileButton(private val api: ComputerApi) {

    val body: JButton = JButton(EXPORT_BUTTON_TEXT)

    init {
        body.preferredSize = Dimension(150, 30)
    }
}

@Component
class AddComputerButton(private val api: ComputerApi) {

    val body: JButton = JButton(ADD_BUTTON_TEST)

    init {
        body.preferredSize = Dimension(150, 30)
    }
}

@Component
class EditComputerButton(private val api: ComputerApi) {

    val body: JButton = JButton(EDIT_BUTTON_TEST)

    init {
        body.preferredSize = Dimension(150, 30)
    }
}

@Component
class RemoveComputerButton(private val api: ComputerApi) {

    val body: JButton = JButton(REMOVE_BUTTON_TEST)

    init {
        body.preferredSize = Dimension(150, 30)
    }
}

const val EXPORT_BUTTON_TEXT = "Export"
const val ADD_BUTTON_TEST = "Add"
const val EDIT_BUTTON_TEST = "Edit"
const val REMOVE_BUTTON_TEST = "Remove"