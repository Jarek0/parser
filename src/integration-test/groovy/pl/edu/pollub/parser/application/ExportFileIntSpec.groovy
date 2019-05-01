package pl.edu.pollub.parser.application

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import pl.edu.pollub.dependencyinjection.DependencyInjection
import pl.edu.pollub.parser.Application
import pl.edu.pollub.parser.domain.ComputerRepository
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.parser.domain.samples.SampleComputer.sampleComputer
import static pl.edu.pollub.parser.utils.FileReader.readFile

class ExportFileIntSpec extends Specification {

    @Rule
    TemporaryFolder temporaryFolder = new TemporaryFolder()

    ComputerRepository repository

    @Subject
    ComputerApi computerApi

    def setup() {
        DependencyInjection.INSTANCE.start(Application.class)
        computerApi = DependencyInjection.INSTANCE.inject(ComputerApi.class)
        repository = DependencyInjection.INSTANCE.inject(ComputerRepository.class)
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
        when:
            def resultFile = computerApi.export(new ExportFileQuery(fileHint))
        then:
            resultFile.getText() == expectedContent
            resultFile.name == fileHint.name
            resultFile.path == fileHint.path
    }
}
