package pl.edu.pollub.parser.application

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.domain.Computer
import java.io.File

@Component
class ComputerApi(private val computerService: ComputerService) {

    fun import(command: ImportFileCommand) {
        computerService.import(command.fileToImport)
    }

    fun export(command: ExportFileCommand): File {
        return computerService.export(command.fileHint)
    }

    fun add(command: AddComputerCommand) {
        computerService.add(command.computerToAdd)
    }

    fun remove(command: RemoveComputerCommand) {
        computerService.remove(command.computerToRemoveId)
    }

    fun get(query: GetComputerQuery) = computerService.get(query.id)

    fun getAll() = computerService.getAll()

}