package pl.edu.pollub.parser.application

import org.junit.Rule
import org.junit.rules.TemporaryFolder
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

class ComputerApiIntSpec extends Specification {

    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder()

    ComputerRepository repository

    ComputersChangedSubject subject

    FakeComputersChangedObserver observer = new FakeComputersChangedObserver()

    @Subject
    ComputerApi computerApi

    def setupSpec() {
        DependencyInjection.INSTANCE.start(Application.class)
    }

    def setup() {
        computerApi = DependencyInjection.INSTANCE.inject(ComputerApi.class)
        repository = DependencyInjection.INSTANCE.inject(ComputerRepository.class)
        subject = DependencyInjection.INSTANCE.inject(ComputersChangedSubject.class)
        temporaryFolder.create()
        subject.subscribe(observer)
    }

    def cleanup() {
        temporaryFolder.delete()
        repository.removeAll()
        observer.clear()
        subject.unsubscribe(observer)
    }

    def "should import txt file with some computers"() {
        given:
            def command = new ImportFileCommand(readFile("computers.txt"))
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
            computerApi.import(command)
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


    def "should export some computers to txt file"() {
        given:
            def computers = [
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
        and:
            repository.addAll(computers)
        and:
            def expectedContent = readFile("computers.txt").getText()
        and:
            def fileHint = temporaryFolder.newFile("exportedComputers.txt")
            def command = new ExportFileCommand(fileHint)
        when:
            def resultFile = computerApi.export(command)
        then:
            resultFile.getText() == expectedContent
            resultFile.name == fileHint.name
            resultFile.path == fileHint.path
        and:
            repository.getAll().isEmpty()
        and:
            observer.isNotified()
    }

    def "should add computer"() {
        given:
            def computer = sampleComputer(
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
            def command = new AddComputerCommand(computer)
        when:
            computerApi.add(command)
        then:
            def addedComputers = repository.getAll()
            addedComputers.size() == 1
            assertComputer(addedComputers[0]).isDataSame(computer)
        and:
            observer.isNotified()
    }

    def "should remove computer"() {
        given:
            def computer = sampleComputer(
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
            repository.add(computer)
            def command = new RemoveComputerCommand(computer.id)
        when:
            computerApi.remove(command)
        then:
            def addedComputers = repository.getAll()
            addedComputers.size() == 0
    }

    def "should get computer"() {
        given:
            def computer = sampleComputer(
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
            repository.add(computer)
            def query = new GetComputerQuery(computer.id)
        when:
            def foundComputer = computerApi.get(query)
        then:
            foundComputer == computer
    }

    def "should get all computers"() {
        given:
            def computers = [
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
            repository.addAll(computers)
        when:
            def foundComputers = computerApi.getAll()
        then:
            foundComputers.size() == 4
            foundComputers.containsAll(computers)
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

        void clear() {
            notified = false
        }
    }
}

