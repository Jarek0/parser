package pl.edu.pollub.parser.application

import pl.edu.pollub.dependencyinjection.Component
import pl.edu.pollub.dependencyinjection.DependencyInjection
import pl.edu.pollub.dependencyinjection.Inject
import java.io.File

@Component
class ComputerApi(private val computersService: ComputerService) {

    fun import(file: File) {
        computersService.import(file)
    }

}