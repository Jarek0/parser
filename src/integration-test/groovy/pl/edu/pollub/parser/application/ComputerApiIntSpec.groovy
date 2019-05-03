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
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleDisc
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleDiscReader
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleGraphicCard
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleManufacturer
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleOperationSystem
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleProcessor
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleRam
import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleScreen
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
                            manufacturer: sampleManufacturer(value: "Fujitsu"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1920x1080",
                                    type: "blyszczaca",
                                    touchscreen: "tak"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "8",
                                    clockSpeed: "1900"
                            ),
                            ram: sampleRam(value: "24GB"),
                            disc: sampleDisc(
                                    storage: "500GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 520",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "brak systemu"),
                            discReader: sampleDiscReader(value: "Blu-Ray")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Huawei"),
                            screen: sampleScreen(
                                    size: "13\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2400"
                            ),
                            ram: sampleRam(value: "12GB"),
                            disc: sampleDisc(
                                    storage: "24GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Dell"),
                            screen: sampleScreen(
                                    size: "12\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2800"
                            ),
                            ram: sampleRam(value: "8GB"),
                            disc: sampleDisc(
                                    storage: "240GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 4000",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "Windows 7 Home")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Asus"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1600x900",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i5",
                                    physicalCores: "4",
                            ),
                            ram: sampleRam(value: "16GB"),
                            disc: sampleDisc(
                                    storage: "120GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 5000",
                                    memory: "1GB",
                            ),
                            discReader: sampleDiscReader(value: "brak")
                    )
            ]
        when:
            computerApi.import(command)
        then:
            def importedComputers = repository.getAll()
            importedComputers.size() == 4
            assertComputer(importedComputers[0]).isDataSame(expectedComputers[0])
            assertComputer(importedComputers[1]).isDataSame(expectedComputers[1])
            assertComputer(importedComputers[2]).isDataSame(expectedComputers[2])
            assertComputer(importedComputers[3]).isDataSame(expectedComputers[3])
        and:
            observer.isNotified()
    }


    def "should import xml file with some computers"() {
        given:
            def command = new ImportFileCommand(readFile("computers.xml"))
        and:
            def expectedComputers = [
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Fujitsu"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1920x1080",
                                    type: "blyszczaca",
                                    touchscreen: "tak"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "8",
                                    clockSpeed: "1900"
                            ),
                            ram: sampleRam(value: "24GB"),
                            disc: sampleDisc(
                                    storage: "500GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 520",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "brak systemu"),
                            discReader: sampleDiscReader(value: "Blu-Ray")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Huawei"),
                            screen: sampleScreen(
                                    size: "13\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2400"
                            ),
                            ram: sampleRam(value: "12GB"),
                            disc: sampleDisc(
                                    storage: "24GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Dell"),
                            screen: sampleScreen(
                                    size: "12\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2800"
                            ),
                            ram: sampleRam(value: "8GB"),
                            disc: sampleDisc(
                                    storage: "240GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 4000",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "Windows 7 Home")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Asus"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1600x900",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i5",
                                    physicalCores: "4",
                            ),
                            ram: sampleRam(value: "16GB"),
                            disc: sampleDisc(
                                    storage: "120GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 5000",
                                    memory: "1GB",
                            ),
                            discReader: sampleDiscReader(value: "brak")
                    )
            ]
        when:
            computerApi.import(command)
        then:
            def importedComputers = repository.getAll()
            importedComputers.size() == 4
            assertComputer(importedComputers[0]).isDataSame(expectedComputers[0])
            assertComputer(importedComputers[1]).isDataSame(expectedComputers[1])
            assertComputer(importedComputers[2]).isDataSame(expectedComputers[2])
            assertComputer(importedComputers[3]).isDataSame(expectedComputers[3])
        and:
            observer.isNotified()
    }


    def "should export some computers to txt file"() {
        given:
            def computers = [
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Fujitsu"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1920x1080",
                                    type: "blyszczaca",
                                    touchscreen: "tak"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "8",
                                    clockSpeed: "1900"
                            ),
                            ram: sampleRam(value: "24GB"),
                            disc: sampleDisc(
                                    storage: "500GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 520",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "brak systemu"),
                            discReader: sampleDiscReader(value: "Blu-Ray")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Huawei"),
                            screen: sampleScreen(
                                    size: "13\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2400"
                            ),
                            ram: sampleRam(value: "12GB"),
                            disc: sampleDisc(
                                    storage: "24GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Dell"),
                            screen: sampleScreen(
                                    size: "12\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2800"
                            ),
                            ram: sampleRam(value: "8GB"),
                            disc: sampleDisc(
                                    storage: "240GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 4000",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "Windows 7 Home")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Asus"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1600x900",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i5",
                                    physicalCores: "4",
                            ),
                            ram: sampleRam(value: "16GB"),
                            disc: sampleDisc(
                                    storage: "120GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 5000",
                                    memory: "1GB",
                            ),
                            discReader: sampleDiscReader(value: "brak")
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
            def existingComputers = repository.getAll()
            existingComputers.size() == 1
            assertComputer(existingComputers[0]).hasData(computerData)
        and:
            observer.isNotified()
    }

    def "should add computer after computer with id"() {
        given:
            def computers = [
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Fujitsu"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1920x1080",
                                    type: "blyszczaca",
                                    touchscreen: "tak"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "8",
                                    clockSpeed: "1900"
                            ),
                            ram: sampleRam(value: "24GB"),
                            disc: sampleDisc(
                                    storage: "500GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 520",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "brak systemu"),
                            discReader: sampleDiscReader(value: "Blu-Ray")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Huawei"),
                            screen: sampleScreen(
                                    size: "13\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2400"
                            ),
                            ram: sampleRam(value: "12GB"),
                            disc: sampleDisc(
                                    storage: "24GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    )
            ]
            repository.addAll(computers)
        and:
            def computerId = computers[0].id
            String[] computerData = ["Fujitsu", "14\"", "1920x1080",
                                     "blyszczaca", "tak", "intel i7",
                                     "8", "1900", "24GB", "500GB",
                                     "HDD", "intel HD Graphics 520",
                                     "1GB", "brak systemu", "Blu-Ray"]
            def command = new AddComputerAfterIdCommand(computerData, computerId)
        when:
            computerApi.add(command)
        then:
            def existingComputers = repository.getAll()
            existingComputers.size() == 3
            assertComputer(existingComputers[1]).hasData(computerData)
        and:
            observer.isNotified()
    }

    def "should edit computer"() {
        given:
            def computerToEdit = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
            repository.add(computerToEdit)
        and:
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
            def existingComputers = repository.getAll()
            existingComputers.size() == 1
            def editedComputer = existingComputers[0]
            editedComputer.id == computerToEditId
            assertComputer(editedComputer).hasData(computerToEditData)
        and:
            observer.isNotified()
    }

    def "should remove computer"() {
        given:
            def computers = [
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Fujitsu"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1920x1080",
                                    type: "blyszczaca",
                                    touchscreen: "tak"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "8",
                                    clockSpeed: "1900"
                            ),
                            ram: sampleRam(value: "24GB"),
                            disc: sampleDisc(
                                    storage: "500GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 520",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "brak systemu"),
                            discReader: sampleDiscReader(value: "Blu-Ray")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Huawei"),
                            screen: sampleScreen(
                                    size: "13\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2400"
                            ),
                            ram: sampleRam(value: "12GB"),
                            disc: sampleDisc(
                                    storage: "24GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    )
            ]
            repository.addAll(computers)
        and:
            def command = new RemoveComputerCommand(computers[0].id)
        when:
            computerApi.remove(command)
        then:
            def existingComputers = repository.getAll()
            existingComputers.size() == 1
    }

    def "should remove all computers"() {
        given:
            def computers = [
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Fujitsu"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1920x1080",
                                    type: "blyszczaca",
                                    touchscreen: "tak"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "8",
                                    clockSpeed: "1900"
                            ),
                            ram: sampleRam(value: "24GB"),
                            disc: sampleDisc(
                                    storage: "500GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 520",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "brak systemu"),
                            discReader: sampleDiscReader(value: "Blu-Ray")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Huawei"),
                            screen: sampleScreen(
                                    size: "13\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2400"
                            ),
                            ram: sampleRam(value: "12GB"),
                            disc: sampleDisc(
                                    storage: "24GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    )
            ]
            repository.addAll(computers)
        when:
            computerApi.removeAll()
        then:
            def existingComputers = repository.getAll()
            existingComputers.size() == 0
    }

    def "should get computer"() {
        given:
            def computer = sampleComputer(
                    manufacturer: sampleManufacturer(value: "Fujitsu"),
                    screen: sampleScreen(
                            size: "14\"",
                            resolution: "1920x1080",
                            type: "blyszczaca",
                            touchscreen: "tak"
                    ),
                    processor: sampleProcessor(
                            name: "intel i7",
                            physicalCores: "8",
                            clockSpeed: "1900"
                    ),
                    ram: sampleRam(value: "24GB"),
                    disc: sampleDisc(
                            storage: "500GB",
                            type: "HDD"
                    ),
                    graphicCard: sampleGraphicCard(
                            name: "intel HD Graphics 520",
                            memory: "1GB",
                    ),
                    operationSystem: sampleOperationSystem(value: "brak systemu"),
                    discReader: sampleDiscReader(value: "Blu-Ray")
            )
            repository.add(computer)
        and:
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
                            manufacturer: sampleManufacturer(value: "Fujitsu"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1920x1080",
                                    type: "blyszczaca",
                                    touchscreen: "tak"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "8",
                                    clockSpeed: "1900"
                            ),
                            ram: sampleRam(value: "24GB"),
                            disc: sampleDisc(
                                    storage: "500GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 520",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "brak systemu"),
                            discReader: sampleDiscReader(value: "Blu-Ray")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Huawei"),
                            screen: sampleScreen(
                                    size: "13\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2400"
                            ),
                            ram: sampleRam(value: "12GB"),
                            disc: sampleDisc(
                                    storage: "24GB",
                                    type: "HDD"
                            ),
                            graphicCard: sampleGraphicCard(name: "NVIDIA GeForce GTX 1050"),
                            discReader: sampleDiscReader(value: "brak")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Dell"),
                            screen: sampleScreen(
                                    size: "12\"",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i7",
                                    physicalCores: "4",
                                    clockSpeed: "2800"
                            ),
                            ram: sampleRam(value: "8GB"),
                            disc: sampleDisc(
                                    storage: "240GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 4000",
                                    memory: "1GB",
                            ),
                            operationSystem: sampleOperationSystem(value: "Windows 7 Home")
                    ),
                    sampleComputer(
                            manufacturer: sampleManufacturer(value: "Asus"),
                            screen: sampleScreen(
                                    size: "14\"",
                                    resolution: "1600x900",
                                    type: "matowa",
                                    touchscreen: "nie"
                            ),
                            processor: sampleProcessor(
                                    name: "intel i5",
                                    physicalCores: "4",
                            ),
                            ram: sampleRam(value: "16GB"),
                            disc: sampleDisc(
                                    storage: "120GB",
                                    type: "SSD"
                            ),
                            graphicCard: sampleGraphicCard(
                                    name: "intel HD Graphics 5000",
                                    memory: "1GB",
                            ),
                            discReader: sampleDiscReader(value: "brak")
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

