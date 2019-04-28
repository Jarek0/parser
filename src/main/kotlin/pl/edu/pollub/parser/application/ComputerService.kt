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
        val fileContent = file.bufferedReader().readLines()
        val computers = parserProvider.provide(file.extension).parse(fileContent)
        computerRepository.addAll(computers)
        computersChangedSubject.notifyObservers()
    }

}