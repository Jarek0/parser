package pl.edu.pollub.parser.domain

interface ComputerRepository {

    fun add(computer: Computer): Boolean

    fun addAll(computers: Collection<Computer>): Boolean

    fun getAll(): Set<Computer>

    fun removeAll()

    fun remove(id: ComputerId): Boolean

    fun get(id: ComputerId): Computer

}