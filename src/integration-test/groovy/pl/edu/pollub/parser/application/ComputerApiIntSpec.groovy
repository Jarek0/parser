package pl.edu.pollub.parser.application

import pl.edu.pollub.dependencyinjection.DependencyInjection
import pl.edu.pollub.parser.Application

import pl.edu.pollub.parser.domain.ComputerRepository
import pl.edu.pollub.parser.domain.ComputersChangedObserver
import pl.edu.pollub.parser.domain.ComputersChangedSubject
import pl.edu.pollub.parser.instrastructure.InMemoryComputerRepository
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.parser.application.SampleComputer.sampleComputer
import static pl.edu.pollub.parser.utils.FileReader.readFile

class ComputerApiIntSpec extends Specification {

    ComputerRepository repository

    ComputersChangedSubject subject

    FakeComputersChangedObserver observer = new FakeComputersChangedObserver()

    @Subject
    ComputerApi computerApi

    def synchronized setup() {
        DependencyInjection.INSTANCE.start(Application.class)
        computerApi = DependencyInjection.INSTANCE.inject(ComputerApi.class)
        repository = DependencyInjection.INSTANCE.inject(InMemoryComputerRepository.class)
        subject = DependencyInjection.INSTANCE.inject(ComputersChangedSubject.class)
        subject.subscribe(observer)
    }

    def "should import simple computer from txt file"() {
        given:
            def textFile = readFile("simpleComputer.txt")
        and:
            def expectedComputerDto = sampleComputer(
                    manufacturer: "Fujitsu",
                    matrixSize: "14\"",
                    resolution: "1920x1080",
                    matrixType: "blyszczaca",
                    touchscreen: "tak",
                    processor: "intel i7",
                    coresCount: 8,
                    timing: 1900,
                    ram: "24GB",
                    discCapacity: "500GB",
                    discType: "HDD",
                    graphicCard: "intel HD Graphics 520",
                    graphicCardMemory: "1GB",
                    operationSystem: "brak systemu",
                    opticalDrive: "Blu-Ray"
            )
        when:
            computerApi.import(textFile)
        then:
            def addedComputers = repository.getAll()
            addedComputers.size() == 1
            addedComputers.contains(expectedComputerDto)
        and:
            observer.isNotified()
    }

}

class FakeComputersChangedObserver implements ComputersChangedObserver {

    private boolean notified = false

    @Override
    void receive() {
        notified = true
    }

    boolean isNotified() {
        notified
    }

}