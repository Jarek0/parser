package pl.edu.pollub.parser.instrastructure

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.ComputerRepository

@Component
class InMemoryComputerRepository: ComputerRepository {

    private val persistedComputers = mutableListOf<Computer>()

    override fun addAll(computers: List<Computer>) = persistedComputers.addAll(persistedComputers)

    override fun getAll() = persistedComputers.toList()

    override fun clear() = persistedComputers.clear()

}