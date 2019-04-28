package pl.edu.pollub.parser.domain

import pl.edu.pollub.dependencyinjection.Component

@Component
class ComputersChangedSubject {

    private val observers: MutableList<ComputersChangedObserver> = mutableListOf()

    fun subscribe(observer: ComputersChangedObserver) = observers.add(observer)

    fun notifyObservers() {
        for(observer in observers) {
            observer.receive()
        }
    }
}

interface ComputersChangedObserver {

    fun receive()

}