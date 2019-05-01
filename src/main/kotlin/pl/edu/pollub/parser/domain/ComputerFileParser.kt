package pl.edu.pollub.parser.domain

interface ComputerFileParser {

    fun parseFrom(fileContent: String): Set<Computer>

    fun parseFrom(computers: Collection<Computer>): String

}