package pl.edu.pollub.parser.domain

interface ComputerRepository {

    fun add(computer: Computer): Boolean

    fun addAll(computers: Collection<Computer>): Boolean

    fun getAll(): Set<Computer>

    fun removeAll()

    fun removeById(id: ComputerId): Boolean

    fun getById(id: ComputerId): Computer

}