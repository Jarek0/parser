package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component
import java.io.File

@Component
class XmlFileComputerParser: ComputerFileParser {

    override fun parse(fileContent: List<String>): List<Computer> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}