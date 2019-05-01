package pl.edu.pollub.parser.instrastructure

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.parser.domain.Computer
import pl.edu.pollub.parser.domain.ComputerId
import pl.edu.pollub.parser.domain.ComputerRepository

@Component
class InMemoryComputerRepository: ComputerRepository {

    private val persistedComputers = mutableSetOf<Computer>()

    override fun add(computer: Computer) = persistedComputers.add(computer)

    override fun addAll(computers: Collection<Computer>) = persistedComputers.addAll(computers)

    override fun getAll() = persistedComputers.toSet()

    override fun removeAll() = persistedComputers.clear()

    override fun remove(id: ComputerId) = persistedComputers.removeIf { it.id == id }

    override fun get(id: ComputerId) = persistedComputers.firstOrNull { it.id == id } ?: Computer()

}