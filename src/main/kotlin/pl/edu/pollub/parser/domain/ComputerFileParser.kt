package pl.edu.pollub.parser.domain

interface ComputerFileParser {

    fun parse(fileContent: List<String>): List<Computer>

}