package pl.edu.pollub.parser.application

import pl.edu.pollub.parser.domain.ComputerId
import java.io.File

data class ImportFileCommand(val fileToImport: File)

data class RemoveComputerCommand(val computerToRemoveId: ComputerId)

class EditComputerCommand(val id: ComputerId, val computerToEditData: ComputerTableRow)

class AddComputerCommand(val computerToAddData: ComputerTableRow)

class AddComputerAfterIdCommand(val computerToAddData: ComputerTableRow, val computerId: ComputerId)