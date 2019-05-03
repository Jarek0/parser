package pl.edu.pollub.parser.application

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.application.ComputerConverters.Companion.convert
import pl.edu.pollub.parser.domain.*
import java.io.File

@Component
class ComputerService(
        private val parserProvider: ComputersParserProvider,
        private val computerRepository: ComputerRepository,
        private val computersChangedSubject: ComputersChangedSubject
) {

    fun import(file: File) {
        computerRepository.removeAll()
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

    fun add(computerToAddData: ComputerTableRow) {
        val computer = convert(computerToAddData)
        computerRepository.add(computer)
        computersChangedSubject.notifyObservers()
    }

    fun add(computerToAddData: ComputerTableRow, computerId: ComputerId) {
        val computer = convert(computerToAddData)
        val computers = computerRepository.getAll().toMutableList()
        computers.add(computers.findIndexById(computerId) + 1, computer)
        computerRepository.removeAll()
        computerRepository.addAll(computers)
        computersChangedSubject.notifyObservers()
    }

    private fun MutableList<Computer>.findIndexById(computerId: ComputerId) = indexOf(first { it.id == computerId })

    fun edit(id: ComputerId, computerToEditData: ComputerTableRow) {
        val computerToEdit = computerRepository.getById(id)
        convert(computerToEditData, computerToEdit)
        computersChangedSubject.notifyObservers()
    }

    fun remove(computerToRemoveId: ComputerId) {
        computerRepository.removeById(computerToRemoveId)
        computersChangedSubject.notifyObservers()
    }

    fun removeAll() {
        computerRepository.removeAll()
    }

    fun get(id: ComputerId): Computer = computerRepository.getById(id)

    fun getAll(): Collection<Computer> = computerRepository.getAll()

}