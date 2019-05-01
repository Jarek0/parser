package pl.edu.pollub.parser.instrastructure

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.ComputerRepository

@Component
class InMemoryComputerRepository: ComputerRepository {

    private val persistedComputers = mutableSetOf<Computer>()

    override fun addAll(computers: Collection<Computer>) = persistedComputers.addAll(computers)

    override fun getAll() = persistedComputers.toSet()

    override fun clear() = persistedComputers.clear()

}