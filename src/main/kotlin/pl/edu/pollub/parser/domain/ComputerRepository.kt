package pl.edu.pollub.parser.domain

interface ComputerRepository {

    fun addAll(computers: Collection<Computer>): Boolean

    fun getAll(): Set<Computer>

    fun clear()
}