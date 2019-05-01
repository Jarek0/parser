package pl.edu.pollub.parser.application

import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.ComputerId
import java.io.File

data class ImportFileCommand(val fileToImport: File)

data class AddComputerCommand(val computerToAdd: Computer)

data class RemoveComputerCommand(val computerToRemoveId: ComputerId)

data class ExportFileCommand(val fileHint: File)
