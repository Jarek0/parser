package pl.edu.pollub.parser.domain

interface ComputersFileParser {

    fun parseFrom(fileContent: String): Set<Computer>

    fun parseFrom(computers: Collection<Computer>): String

}