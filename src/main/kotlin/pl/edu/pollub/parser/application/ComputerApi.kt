package pl.edu.pollub.parser.application

import pl.edu.pollub.dependencyinjection.Component
import java.io.File

@Component
class ComputerApi(private val computerService: ComputerService) {

    fun import(command: ImportFileCommand) {
        computerService.import(command.fileToImport)
    }

    fun export(query: ExportFileQuery): File {
        return computerService.export(query.fileHint)
    }

    fun add(command: AddComputerCommand) {
        computerService.add(command.computerToAddData)
    }

    fun edit(command: EditComputerCommand) {
        computerService.edit(command.id, command.computerToEditData)
    }

    fun remove(command: RemoveComputerCommand) {
        computerService.remove(command.computerToRemoveId)
    }

    fun removeAll() {
        computerService.removeAll()
    }

    fun get(query: GetComputerQuery): ComputerTableRow {
        return convert(computerService.get(query.id))
    }

    fun getAll(): ComputerTableRows {
        return convert(computerService.getAll())
    }

}