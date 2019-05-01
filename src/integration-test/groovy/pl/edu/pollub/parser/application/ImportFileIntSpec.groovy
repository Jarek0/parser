package pl.edu.pollub.parser.application

import pl.edu.pollub.dependencyinjection.DependencyInjection
import pl.edu.pollub.parser.Application

import pl.edu.pollub.parser.domain.ComputerRepository
import pl.edu.pollub.parser.domain.ComputersChangedObserver
import pl.edu.pollub.parser.domain.ComputersChangedSubject
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.parser.domain.assertions.ComputerAssert.assertComputer
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleComputer
import static pl.edu.pollub.parser.utils.FileReader.readFile

class ImportFileIntSpec extends Specification {

    ComputerRepository repository

    ComputersChangedSubject subject

    FakeComputersChangedObserver observer = new FakeComputersChangedObserver()

    @Subject
    ComputerApi computerApi

    def setup() {
        DependencyInjection.INSTANCE.start(Application.class)
        computerApi = DependencyInjection.INSTANCE.inject(ComputerApi.class)
        repository = DependencyInjection.INSTANCE.inject(ComputerRepository.class)
        subject = DependencyInjection.INSTANCE.inject(ComputersChangedSubject.class)
        subject.subscribe(observer)
    }

    def "should import txt file with some computers"() {
        given:
            def textFile = readFile("computers.txt")
        and:
            def expectedComputers = [
                    sampleComputer(
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
                    ),
                    sampleComputer(
                            manufacturer: "Huawei",
                            matrixSize: "13\"",
                            matrixType: "matowa",
                            touchscreen: "nie",
                            processor: "intel i7",
                            coresCount: "4",
                            timing: "2400",
                            ram: "12GB",
                            discCapacity: "24GB",
                            discType: "HDD",
                            graphicCard: "NVIDIA GeForce GTX 1050",
                            opticalDrive: "brak"
                    ),
                    sampleComputer(
                            manufacturer: "Dell",
                            matrixSize: "12\"",
                            matrixType: "matowa",
                            touchscreen: "nie",
                            processor: "intel i7",
                            coresCount: "4",
                            timing: "2800",
                            ram: "8GB",
                            discCapacity: "240GB",
                            discType: "SSD",
                            graphicCard: "intel HD Graphics 4000",
                            graphicCardMemory: "1GB",
                            operationSystem: "Windows 7 Home"
                    ),
                    sampleComputer(
                            manufacturer: "Asus",
                            matrixSize: "14\"",
                            resolution: "1600x900",
                            matrixType: "matowa",
                            touchscreen: "nie",
                            processor: "intel i5",
                            coresCount: "4",
                            ram: "16GB",
                            discCapacity: "120GB",
                            discType: "SSD",
                            graphicCard: "intel HD Graphics 5000",
                            graphicCardMemory: "1GB",
                            opticalDrive: "brak"
                    )
            ]
        when:
            computerApi.import(new ImportFileCommand(textFile))
        then:
            def addedComputers = repository.getAll()
            addedComputers.size() == 4
            assertComputer(addedComputers[0]).isDataSame(expectedComputers[0])
            assertComputer(addedComputers[1]).isDataSame(expectedComputers[1])
            assertComputer(addedComputers[2]).isDataSame(expectedComputers[2])
            assertComputer(addedComputers[3]).isDataSame(expectedComputers[3])
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