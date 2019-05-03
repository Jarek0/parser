package pl.edu.pollub.parser.gui

import pl.edu.pollub.dependencyinjection.Component
import java.awt.Font
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JLabel

@Component
class ComputersGui(mainFrame: MainFrame, private val table: ComputersTable, private val buttonsPanel: ComputerButtonsPanel) {


    init {
        val lblHeading = JLabel("File content")
        lblHeading.font = Font("Arial", Font.TRUETYPE_FONT, 15)

        with(mainFrame.body) {
            with(contentPane) {
                layout = BoxLayout(contentPane, BoxLayout.PAGE_AXIS)
                contentPane.add(table.body)
                contentPane.add(buttonsPanel.body)
            }
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            setSize(1200, 500)
            isVisible = true
        }
    }

}

@Component
class MainFrame {

    val body = JFrame("File Parser")

}
