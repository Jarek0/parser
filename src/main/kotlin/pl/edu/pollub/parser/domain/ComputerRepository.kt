package pl.edu.pollub.parser.domain

interface ComputerRepository {

    fun addAll(computers: List<Computer>): Boolean

    fun getAll(): List<Computer>

    fun clear()
}