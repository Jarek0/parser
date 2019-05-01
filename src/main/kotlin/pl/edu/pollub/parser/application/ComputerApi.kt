package pl.edu.pollub.parser.application

import pl.edu.pollub.dependencyinjection.Component
import java.io.File

@Component
class ComputerApi(private val computersService: ComputerService) {

    fun import(importFileCommand: ImportFileCommand) {
        computersService.import(importFileCommand.fileToImport)
    }

    fun export(importFileQuery: ExportFileQuery): File {
        return computersService.export(importFileQuery.fileHint)
    }

}