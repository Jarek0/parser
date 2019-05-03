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
            def command = new ExportFileQuery(fileHint)
        when:
            def resultFile = computerApi.export(command)
        then:
            resultFile.getText() == expectedContent
            resultFile.name == fileHint.name
            resultFile.path == fileHint.path
    }

    def "should add computer"() {
        given:
            String[] computerData = ["Fujitsu", "14\"", "1920x1080",
                                     "blyszczaca", "tak", "intel i7",
                                     "8", "1900", "24GB", "500GB",
                                     "HDD", "intel HD Graphics 520",
                                     "1GB", "brak systemu", "Blu-Ray"]
            def command = new AddComputerCommand(computerData)
        when:
            computerApi.add(command)
        then:
            def addedComputers = repository.getAll()
            addedComputers.size() == 1
            assertComputer(addedComputers[0]).hasData(computerData)
        and:
            observer.isNotified()
    }

    def "should edit computer"() {
        given:
            def computerToEdit = sampleComputer(
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
            repository.add(computerToEdit)
            def computerToEditId = computerToEdit.id
            String[] computerToEditData = [
                    "Fujitsu", "15\"", "1400x1080",
                    "matowa", "tak", "intel i8",
                    "8", "1900", "24GB", "600GB",
                    "HDD", "intel HD Graphics 520",
                    "1GB", "brak systemu", "Blu-Ray"
            ]
            def command = new EditComputerCommand(computerToEditId, computerToEditData)
        when:
            computerApi.edit(command)
        then:
            def editedComputers = repository.getAll()
            editedComputers.size() == 1
            def editedComputer = editedComputers[0]
            editedComputer.id == computerToEditId
            assertComputer(editedComputer).hasData(computerToEditData)
        and:
            observer.isNotified()
    }

    def "should remove computer"() {
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
                    )
            ]
            repository.addAll(computers)
        when:
            computerApi.removeAll()
        then:
            def addedComputers = repository.getAll()
            addedComputers.size() == 0
    }

    def "should remove all computers"() {
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
        when:
            computerApi.removeAll()
        then:
            def computers = repository.getAll()
            computers.size() == 0
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
            def foundComputerData = computerApi.get(query)
        then:
            assertComputer(computer).hasDataAndId(foundComputerData)
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
            def foundComputersData = computerApi.getAll()
        then:
            foundComputersData.length == 4
            assertComputer(computers[0]).hasDataAndId(foundComputersData[0])
            assertComputer(computers[1]).hasDataAndId(foundComputersData[1])
            assertComputer(computers[2]).hasDataAndId(foundComputersData[2])
            assertComputer(computers[3]).hasDataAndId(foundComputersData[3])
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

