package pl.edu.pollub.parser.application

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.domain.ComputerRepository
import pl.edu.pollub.parser.domain.ComputersChangedSubject
import pl.edu.pollub.parser.domain.ComputersParserProvider
import java.io.File

@Component
class ComputerService(
        private val parserProvider: ComputersParserProvider,
        private val computerRepository: ComputerRepository,
        private val computersChangedSubject: ComputersChangedSubject
) {

    fun import(file: File) {
        val fileContent = file.bufferedReader().readText()
        val parser = parserProvider.provide(file.extension)
        val computers = parser.parseFrom(fileContent)
        computerRepository.addAll(computers)
        computersChangedSubject.notifyObservers()
    }

    fun export(fileHint: File): File {
        val computers = computerRepository.getAll()
        val parser = parserProvider.provide(fileHint.extension)
        val fileContent = parser.parseFrom(computers)
        val importFile = File(fileHint.toURI())
        importFile.writeText(fileContent)
        return importFile
    }

}