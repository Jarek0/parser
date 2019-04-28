package pl.edu.pollub.parser

import pl.edu.pollub.dependencyinjection.DependencyInjection

class Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            DependencyInjection.start(Application::class.java)
        }
    }

}
